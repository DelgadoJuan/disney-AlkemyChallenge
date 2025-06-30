import api from "./axiosConfig";
import { type Category } from "../types/CategoryTypes";

const URL_BASE = "/genre"

/**
 * Obtiene todas las categorías.
 * @returns Las categorías.
 */
export const getCategories = async () => {
    const response = await api.get<Category[]>(URL_BASE);
    return response.data;
}

/**
 * Crea una nueva categoría.
 * @param category La categoría a crear.
 * @returns La categoría creada.
 */
export const createCategory = async (category: FormData) => {
    const response = await api.post<Category>(URL_BASE, category, {
        headers: { "Content-Type": "multipart/form-data" }
    });
    return response.data;
};

/**
 * Actualiza una categoría existente.
 * @param id El ID de la categoría a actualizar.
 * @param category La categoría actualizada.
 * @returns La categoría actualizada.
 */
export const updateCategory = async (id: number, category: FormData) => {
    const response = await api.put<Category>(`${URL_BASE}/${id}`, category, {
        headers: { "Content-Type": "multipart/form-data" }
    });
    return response.data;
};

/**
 * Elimina una categoría existente.
 * @param id El ID de la categoría a eliminar.
 * @returns La categoría eliminada.
 */
export const deleteCategory = async (id: number) => {
    const response = await api.delete(`${URL_BASE}/${id}`);
    return response.data;
};


