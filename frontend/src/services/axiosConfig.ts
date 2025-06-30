import axios from "axios";
import { getGlobalJwtSetter, getGlobalUserSetter } from "../context/UsuarioContext";
import { jwtDecode } from "jwt-decode";

const api = axios.create({
  baseURL: "http://localhost:8080/",
  withCredentials: true,
});

/**
 * Instancia separada para refresh token (sin interceptores)
 */
const refreshApi = axios.create({
  baseURL: "http://localhost:8080/",
  withCredentials: true,
});

/**
 * Variable para evitar múltiples requests de refresh simultáneos
 */
let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value?: any) => void;
  reject: (reason?: any) => void;
}> = [];

/**
 * Procesa la cola de requests fallidos
 */
const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) {
      reject(error);
    } else {
      resolve(token);
    }
  });
  failedQueue = [];
};

/**
 * Función de refresh usando instancia separada
 */
const refreshTokenCall = async () => {
  const response = await refreshApi.post('/auth/refresh');
  return response.data;
};

api.interceptors.request.use((config) => {
  const jwtToken = localStorage.getItem("jwtToken");
  if (jwtToken) {
    config.headers.Authorization = `Bearer ${jwtToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if ((error.response?.status === 401 || error.response?.status === 403) && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then((token) => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return api(originalRequest);
        }).catch((err) => {
          return Promise.reject(err);
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const response = await refreshTokenCall();
        const newToken = response.JWT;
        
        localStorage.setItem("jwtToken", newToken);
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        
        /**
         * Actualizar el contexto de usuario
         */
        const setJwt = getGlobalJwtSetter();
        const setUser = getGlobalUserSetter();
        if (setJwt) setJwt(newToken);
        if (setUser) {
          try {
            const decoded: any = jwtDecode(newToken);
            setUser({
              id: parseInt(decoded.sub),
              username: decoded.username,
              email: decoded.email,
              role: decoded.role,
            });
          } catch (e) {
            setUser(null);
          }
        }
        
        processQueue(null, newToken);
        return api(originalRequest);
      } catch (refreshError: any) {
        processQueue(refreshError, null);
        
        /**
         * Solo eliminar JWT si el refresh token es inválido (401)
         */
        if (refreshError.response?.status === 401) {
          localStorage.removeItem("jwtToken");
          
          /**
           * Limpiar el contexto de usuario
           */
          const setJwt = getGlobalJwtSetter();
          const setUser = getGlobalUserSetter();
          if (setJwt) setJwt(null);
          if (setUser) setUser(null);
          
          /**
           * Redirigir al login si estamos en una página protegida
           */
          if (typeof window !== "undefined" && !window.location.pathname.includes('/auth/login')) {
            window.location.href = "/auth/login";
          }
        }
        
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default api; 