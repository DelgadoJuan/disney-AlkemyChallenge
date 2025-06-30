import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

/**
 * Función utilitaria para obtener la URL completa de una imagen
 * @param imagePath - Ruta de la imagen desde la base de datos
 * @param baseUrl - URL base del servidor (opcional, por defecto http://localhost:8080)
 * @returns URL completa de la imagen
 */
export function getImageUrl(imagePath: string | null | undefined, baseUrl: string = "http://localhost:8080"): string {
  if (!imagePath) {
    return "/placeholder.svg";
  }
  
  // Si ya es una URL completa, devolverla tal como está
  if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
    return imagePath;
  }
  
  // Si empieza con "images/", usar la ruta directamente
  if (imagePath.startsWith("images/")) {
    return `${baseUrl}/${imagePath}`;
  }
  
  // Si contiene "src/main/resources/static/", limpiar la ruta
  if (imagePath.includes("src/main/resources/static/")) {
    const cleanPath = imagePath.replace("src/main/resources/static/", "");
    return `${baseUrl}/${cleanPath}`;
  }
  
  // Si es solo un nombre de archivo, asumir que está en images/
  if (!imagePath.includes("/")) {
    return `${baseUrl}/images/${imagePath}`;
  }
  
  // En cualquier otro caso, usar la ruta tal como está
  return `${baseUrl}/${imagePath}`;
}
