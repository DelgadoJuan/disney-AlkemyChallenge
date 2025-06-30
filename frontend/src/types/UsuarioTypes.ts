export interface UsuarioData {
  id: number;
  username: string;
  email: string;
}

export interface Usuario {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface RegisterData {
  username: string;
  email: string;
  password: string;
  confirmPassword?: string;
  role?: string;
}

export interface PasswordData {
  currentPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export interface LoginData {
  email: string;
  password: string;
}

// Tipo para la respuesta de login que puede incluir JWT o un error lógico
export interface LoginResponse {
  JWT: string;
  id: string;
  username: string;
  email: string;
  role: string;
  Error?: string;
}

// Tipo para la respuesta genérica de tu backend (ResponseDTO)
export interface BackendResponse {
  message?: string;
  Error?: string;
}

// Interfaz para el cuerpo de datos de un error de API
export interface ApiErrorData {
  Error?: string;
  message?: string;
}

// Interfaz para el objeto de error estructurado que lanzaremos
export interface ApiError extends Error {
  // Extiende Error para que siga siendo un error estándar
  status?: number; // Código de estado HTTP (e.g., 400, 401, 500)
  data?: ApiErrorData; // Cuerpo de la respuesta de error del backend
}
