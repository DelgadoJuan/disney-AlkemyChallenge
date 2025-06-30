import type { Movie } from "./MovieTypes";

export type Character = {
    id: number;
    nombre: string;
    edad: number;
    peso: number;
    historia?: string;
    imagen: string;
    audiovisuales?: Movie[];
}