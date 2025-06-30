import api from "./axiosConfig";
import { type Character } from "../types/CharacterTypes";

const URL = "/characters";

/**
 * Obtiene todos los personajes.
 * @returns Los personajes.
 */
export const getCharacters = async () => {
    const response = await api.get<Character[]>(URL);
    return response.data;
};

/**
 * Obtiene todos los personajes (solo para admin).
 * @returns Los personajes.
 */
export const getAllCharacters = async () => {
    const response = await api.get<Character[]>(`${URL}/admin`);
    return response.data;
};

/**
 * Obtiene un personaje por su ID.
 * @param id El ID del personaje.
 * @returns El personaje.
 */
export const getCharacterById = async (id: number) => {
    const response = await api.get<Character>(`${URL}/${id}`);
    return response.data;
};

/**
 * Crea un nuevo personaje.
 * @param character El personaje a crear.
 * @returns El personaje creado.
 */
export const createCharacter = async (character: FormData) => {
    const response = await api.post(URL, character, {
        headers: { "Content-Type": "multipart/form-data" }
    });
    return response.data;
};

/**
 * Actualiza un personaje existente.
 * @param id El ID del personaje a actualizar.
 * @param character El personaje actualizado.
 * @returns El personaje actualizado.
 */
export const updateCharacter = async (id: number, character: FormData) => {
    const response = await api.put(`${URL}/${id}`, character, {
        headers: { "Content-Type": "multipart/form-data" }
    });
    return response.data;
};

/**
 * Elimina un personaje existente.
 * @param id El ID del personaje a eliminar.
 * @returns El personaje eliminado.
 */
export const deleteCharacter = async (id: number) => {
    const response = await api.delete(`${URL}/${id}`);
    return response.data;
};

/**
 * Obtiene los personajes destacados.
 * @returns Los personajes destacados.
 */
export const getFeaturedCharacters = async () => {
    const response = await api.get<Character[]>(`${URL}/featured`);
    return response.data;
};




