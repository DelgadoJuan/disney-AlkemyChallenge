import axios from 'axios';
import { type BackendResponse, type RegisterData } from '../types/UsuarioTypes';
import api from "./axiosConfig";

const URL_BASE = "/auth";

/**
 * Registra un nuevo usuario.
 * @param usuario Los datos del usuario para el registro.
 * @returns La respuesta del backend.
 */
export const registerUser = async (usuario: RegisterData): Promise<BackendResponse> => {
  const response = await api.post(`${URL_BASE}/register`, usuario, { withCredentials: true });
  return response.data;
};

/**
 * Inicia sesión en la aplicación.
 * @param email El email del usuario.
 * @param password La contraseña del usuario.
 * @returns La respuesta del backend.
 */
export const login = async (email: string, password: string) => {
  const response = await api.post(`${URL_BASE}/login`, { email, password }, { withCredentials: true });
  return response.data;
};

/**
 * Refresca el token de acceso.
 * @returns La respuesta del backend.
 */
export const refreshToken = async () => {
  const response = await axios.post(`${URL_BASE}/refresh`, {}, { 
    withCredentials: true 
  });
  return response.data;
};

/**
 * Cierra sesión en la aplicación.
 */
export const logout = async () => {
  await api.post(`${URL_BASE}/logout`, {}, { withCredentials: true });
};