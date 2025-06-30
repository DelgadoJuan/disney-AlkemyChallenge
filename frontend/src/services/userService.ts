import api from "./axiosConfig";
import { type PasswordData, type Usuario, type RegisterData } from "../types/UsuarioTypes";

const API_BASE_URL = '/user';

/**
 * Obtiene todos los usuarios.
 * @returns Los usuarios.
 */
export const getUsers = async (): Promise<Usuario[]> => {
    const response = await api.get(`${API_BASE_URL}/`);
    return response.data;
};

/**
 * Obtiene un usuario por su ID.
 * @param id El ID del usuario.
 * @returns El usuario.
 */
export const getUserById = async (id: number): Promise<Usuario> => {
    const response = await api.get(`${API_BASE_URL}/${id}`);
    return response.data;
};

/**
 * Crea un nuevo usuario.
 * @param userData Los datos del usuario a crear.
 * @returns El usuario creado.
 */
export const createUser = async (userData: RegisterData): Promise<Usuario> => {
    const response = await api.post(`${API_BASE_URL}/`, userData);
    return response.data;
};

/**
 * Actualiza un usuario existente.
 * @param id El ID del usuario a actualizar.
 * @param userData Los datos del usuario actualizado.
 * @returns El usuario actualizado.
 */
export const updateUser = async (id: number, userData: Partial<RegisterData>): Promise<Usuario> => {
    const response = await api.put(`${API_BASE_URL}/update/${id}`, userData);
    return response.data;
};

/**
 * Elimina un usuario existente.
 * @param id El ID del usuario a eliminar.
 * @returns El usuario eliminado.
 */
export const deleteUser = async (id: number): Promise<void> => {
    await api.delete(`${API_BASE_URL}/${id}`);
};

/**
 * Actualiza la contraseña de un usuario.
 * @param userId El ID del usuario.
 * @param passwordData Los datos de la contraseña actualizada.
 * @returns La contraseña actualizada.
 */
export const updatePassword = async (userId: number, passwordData: PasswordData) => {
    const response = await api.put(
        `${API_BASE_URL}/${userId}`,
        {
            oldPassword: passwordData.currentPassword,
            newPassword: passwordData.newPassword,
            confirmNewPassword: passwordData.confirmNewPassword
        }
    );
    return response.data;
};