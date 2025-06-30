import React, { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import { useLocation } from "react-router-dom";
import { logout as logoutService, refreshToken } from "../services/authService";
import { jwtDecode } from "jwt-decode";

export interface UserData {
  id: number;
  username: string;
  email: string;
  role: string;
}

interface JwtPayload {
  sub: string;
  username: string;
  email: string;
  role: string;
}

interface UsuarioContextType {
  jwt: string | null;
  user: UserData | null;
  setJwt: (token: string | null) => void;
  setUser: (user: UserData | null) => void;
  limpiarUsuario: () => void;
  logout: () => Promise<void>;
  isAuthenticated: boolean;
}

const UsuarioContext = createContext<UsuarioContextType | undefined>(undefined);

const decodeJwtAndGetUser = (token: string): UserData | null => {
  try {
    const decoded = jwtDecode<JwtPayload>(token);
    console.log("🔍 JWT Decoded:", decoded); // Log temporal para debugging
    return {
      id: parseInt(decoded.sub),
      username: decoded.username,
      email: decoded.email,
      role: decoded.role,
    };
  } catch (error) {
    console.error("❌ Error decoding JWT:", error); // Log temporal para debugging
    return null;
  }
};

const isTokenValid = (token: string): boolean => {
  try {
    const decoded = jwtDecode<JwtPayload & { exp: number }>(token);
    const currentTime = Date.now() / 1000;
    return decoded.exp > currentTime;
  } catch {
    return false;
  }
};

let globalSetJwt: ((token: string | null) => void) | null = null;
let globalSetUser: ((user: UserData | null) => void) | null = null;

export const setGlobalJwtSetter = (setter: (token: string | null) => void) => {
  globalSetJwt = setter;
};
export const setGlobalUserSetter = (setter: (user: UserData | null) => void) => {
  globalSetUser = setter;
};
export const getGlobalJwtSetter = () => globalSetJwt;
export const getGlobalUserSetter = () => globalSetUser;

export const UsuarioProvider = ({ children }: { children: ReactNode }) => {
  const location = useLocation();
  
  const [jwt, setJwt] = useState<string | null>(() => {
    const storedToken = localStorage.getItem("jwtToken");
    if (storedToken && isTokenValid(storedToken)) {
      return storedToken;
    }
    if (storedToken) {
      localStorage.removeItem("jwtToken");
    }
    return null;
  });
  
  const [user, setUser] = useState<UserData | null>(() => {
    const storedToken = localStorage.getItem("jwtToken");
    if (storedToken && isTokenValid(storedToken)) {
      return decodeJwtAndGetUser(storedToken);
    }
    return null;
  });

  useEffect(() => {
    setGlobalJwtSetter(setJwt);
    setGlobalUserSetter(setUser);
  }, [setJwt, setUser]);

  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem("jwtToken");
      
      if (storedToken) {
        if (isTokenValid(storedToken)) {
          setJwt(storedToken);
          const userData = decodeJwtAndGetUser(storedToken);
          setUser(userData);
        } else {
          try {
            const data = await refreshToken();
            if (data.JWT) {
              localStorage.setItem("jwtToken", data.JWT);
              setJwt(data.JWT);
              const userData = decodeJwtAndGetUser(data.JWT);
              setUser(userData);
            }
          } catch (error) {
            localStorage.removeItem("jwtToken");
            setJwt(null);
            setUser(null);
          }
        }
      }
    };

    initializeAuth();
  }, []);

  useEffect(() => {
    const checkAndRefreshToken = async () => {
      const storedToken = localStorage.getItem("jwtToken");

      if (storedToken) {
        if (!isTokenValid(storedToken)) {
          try {
            const data = await refreshToken();
            if (data.JWT) {
              localStorage.setItem("jwtToken", data.JWT);
              setJwt(data.JWT);
              const userData = decodeJwtAndGetUser(data.JWT);
              setUser(userData);
            } else {
              limpiarUsuario();
            }
          } catch (error) {
            // Si el refresh falla, limpiar todo y redirigir al login
            limpiarUsuario();
            if (typeof window !== "undefined" && !window.location.pathname.includes('/auth/login')) {
              window.location.href = "/auth/login";
            }
          }
        }
      }
    };

    checkAndRefreshToken();
  }, [location]);
  
  const limpiarUsuario = () => {
    localStorage.removeItem("jwtToken");
    setJwt(null);
    setUser(null);
  };

  const logout = async () => {
    try {
      await logoutService();
    } catch (error) {
      // Silently handle logout errors
    } finally {
      limpiarUsuario();
    }
  };

  const isAuthenticated = !!jwt && !!user;

  return (
    <UsuarioContext.Provider value={{ jwt, user, setJwt, setUser, limpiarUsuario, logout, isAuthenticated }}>
      {children}
    </UsuarioContext.Provider>
  );
};

export const useUsuario = () => {
  const context = useContext(UsuarioContext);
  if (!context) throw new Error("useUsuario debe usarse dentro de UsuarioProvider");
  return context;
};