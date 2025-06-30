import type { Character } from "./CharacterTypes"

// Tipo unificado para audiovisuales/películas
export type Movie = {
    id: number
    titulo: string
    descripcion: string
    director: string
    duracion: number
    imagen: string
    calificacion: number
    nombreGenero: string
    fechaCreacion: string
}

// Tipo para detalles completos de un audiovisual (incluye personajes)
export type MovieDetail = {
    id: number
    titulo: string
    duracion: number
    imagen: string
    director: string
    calificacion: number
    nombreGenero: string
    fechaCreacion: string
    descripcion: string
    personajes: Character[]
}

// Tipo para crear/actualizar audiovisuales
export type CreateAudiovisualData = {
    titulo: string
    descripcion: string
    director: string
    duracion: number
    calificacion: number
    generoId: number
    fechaCreacion?: string
    imagen: File
}