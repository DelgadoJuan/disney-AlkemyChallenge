import api from "./axiosConfig";
import { type Movie, type MovieDetail, type CreateAudiovisualData } from "../types/MovieTypes";

const URL_BASE = "/audiovisuals"

/**
 * Obtiene una película por su ID.
 * @param id El ID de la película.
 * @returns La película.
 */
export const getMovieById = async (id: number): Promise<MovieDetail> => {
    const response = await api.get(`${URL_BASE}/${id}`);
    return response.data;
}

/**
 * Obtiene todas las películas.
 * @returns Las películas.
 */
export const getMovies = async (): Promise<Movie[]> => {
    const response = await api.get(`${URL_BASE}`);
    return response.data;
}

/**
 * Obtiene todos los audiovisuales.
 * @returns Los audiovisuales.
 */
export const getAudiovisuals = async (): Promise<Movie[]> => {
    const response = await api.get(`${URL_BASE}`);
    return response.data;
}

/**
 * Obtiene todos los audiovisuales (solo para admin).
 * @returns Los audiovisuales.
 */
export const getAudiovisualsForAdmin = async (): Promise<Movie[]> => {
    const response = await api.get(`${URL_BASE}/admin`);
    return response.data;
}

/**
 * Obtiene un audiovisual por su ID.
 * @param id El ID del audiovisual.
 * @returns El audiovisual.
 */
export const getAudiovisualById = async (id: number): Promise<MovieDetail> => {
    const response = await api.get(`${URL_BASE}/${id}`);
    return response.data;
}

/**
 * Crea un nuevo audiovisual.
 * @param data Los datos del audiovisual a crear.
 * @returns El audiovisual creado.
 */
export const createAudiovisual = async (data: CreateAudiovisualData): Promise<void> => {
    const formData = new FormData();
    formData.append("titulo", data.titulo);
    formData.append("descripcion", data.descripcion);
    formData.append("director", data.director);
    formData.append("duracion", data.duracion.toString());
    formData.append("calificacion", data.calificacion.toString());
    formData.append("generoId", data.generoId.toString());
    if (data.fechaCreacion) {
        formData.append("fechaCreacion", data.fechaCreacion);
    }
    formData.append("imagen", data.imagen);

    await api.post(`${URL_BASE}`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
    });
}

/**
 * Actualiza un audiovisual existente.
 * @param id El ID del audiovisual a actualizar.
 * @param data Los datos del audiovisual actualizado.
 * @returns El audiovisual actualizado.
 */
export const updateAudiovisual = async (id: number, data: Partial<CreateAudiovisualData>): Promise<void> => {
    const formData = new FormData();
    if (data.titulo) formData.append("titulo", data.titulo);
    if (data.descripcion) formData.append("descripcion", data.descripcion);
    if (data.director) formData.append("director", data.director);
    if (data.duracion) formData.append("duracion", data.duracion.toString());
    if (data.calificacion) formData.append("calificacion", data.calificacion.toString());
    if (data.generoId) formData.append("generoId", data.generoId.toString());
    if (data.fechaCreacion) formData.append("fechaCreacion", data.fechaCreacion);
    if (data.imagen) formData.append("imagen", data.imagen);

    await api.put(`${URL_BASE}/${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
    });
}

/**
 * Elimina un audiovisual existente.
 * @param id El ID del audiovisual a eliminar.
 * @returns El audiovisual eliminado.
 */
export const deleteAudiovisual = async (id: number): Promise<void> => {
    await api.delete(`${URL_BASE}/${id}`);
}

/**
 * Agrega un personaje a un audiovisual.
 * @param audiovisualId El ID del audiovisual.
 * @param characterId El ID del personaje.
 * @returns El personaje agregado.
 */
export const addCharacterToAudiovisual = async (audiovisualId: number, characterId: number): Promise<void> => {
    await api.patch(`${URL_BASE}/${audiovisualId}/characters`, { personajeId: characterId }, {
        headers: { "Content-Type": "application/json" }
    });
}

/**
 * Elimina un personaje de un audiovisual.
 * @param audiovisualId El ID del audiovisual.
 * @param characterId El ID del personaje.
 * @returns El personaje eliminado.
 */
export const removeCharacterFromAudiovisual = async (audiovisualId: number, characterId: number): Promise<void> => {
    await api.patch(`${URL_BASE}/${audiovisualId}/characters/${characterId}`, {});
}

/**
 * Obtiene los audiovisuales destacados.
 * @returns Los audiovisuales destacados.
 */
export const getFeaturedAudiovisuals = async (): Promise<Movie[]> => {
    const response = await api.get(`${URL_BASE}/featured`);
    return response.data;
};



