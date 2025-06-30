import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Button } from "../components/ui/button";
import { registerUser } from "../services/authService"; // Importar el servicio

// Asegúrate de que esta interfaz coincida con la de authService.ts y tu UsuarioEntity
interface RegisterFormData {
  username: string; // O username, etc.
  email: string;
  password: string;
  confirmPassword?: string; // Opcional, para validación en el frontend
}

export const Register = () => {
  const [formData, setFormData] = useState<RegisterFormData>({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);

    if (formData.password !== formData.confirmPassword) {
      setError("Las contraseñas no coinciden.");
      return;
    }

    // Prepara los datos para enviar, excluyendo confirmPassword si no es parte de UsuarioEntity
    const { username, email, password } = formData;
    const userData = { username: username, email, password }; // Ajusta según tu UsuarioEntity

    try {
      const response = await registerUser(userData);
      // Esto sacar despues
      setSuccessMessage(response.message || "¡Registro exitoso! Ahora puedes iniciar sesión.");
      setTimeout(() => {
        navigate("/auth/login"); // Redirigir a la página de login
      }, 2000);
    } catch (err: any) {
      // err debería ser BackendResponse o Error
      if (err && err.message) {
        setError(err.message);
      } else if (typeof err === 'string') {
        setError(err);
      } else {
        setError("Error en el registro. Por favor, inténtalo de nuevo.");
      }
      console.error("Error en registro:", err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-green-50 to-teal-50 dark:from-green-950 dark:to-teal-950 p-4">
      <div className="absolute inset-0 z-0 bg-[url('/placeholder.svg?height=800&width=1600')] bg-cover bg-center opacity-10"></div>
      <Card className="w-full max-w-md relative z-10 border-2 border-green-100 dark:border-green-900">
        <CardHeader className="space-y-1 text-center">
          <CardTitle className="text-3xl font-bold text-green-700 dark:text-green-300">
            Crear una cuenta
          </CardTitle>
          <CardDescription className="text-gray-600 dark:text-gray-400">
            Ingresa tus datos para registrarte
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label
                htmlFor="username"
                className="text-gray-700 dark:text-gray-300"
              >
                Nombre de usuario
              </Label>
              <Input
                id="username"
                name="username"
                type="text"
                placeholder="Tu nombre de usuario"
                value={formData.username}
                onChange={handleChange}
                required
                className="border-green-200 dark:border-green-800 focus:border-green-500 dark:focus:border-green-500"
              />
            </div>

            <div className="space-y-2">
              <Label
                htmlFor="email"
                className="text-gray-700 dark:text-gray-300"
              >
                Email
              </Label>
              <Input
                id="email"
                name="email"
                type="email"
                placeholder="tu@email.com"
                value={formData.email}
                onChange={handleChange}
                required
                className="border-green-200 dark:border-green-800 focus:border-green-500 dark:focus:border-green-500"
              />
            </div>

            <div className="space-y-2">
              <Label
                htmlFor="password"
                className="text-gray-700 dark:text-gray-300"
              >
                Contraseña
              </Label>
              <Input
                id="password"
                name="password"
                type="password"
                placeholder="••••••••"
                value={formData.password}
                onChange={handleChange}
                required
                minLength={6} // Ejemplo de validación
                className="border-green-200 dark:border-green-800 focus:border-green-500 dark:focus:border-green-500"
              />
            </div>

            <div className="space-y-2">
              <Label
                htmlFor="confirmPassword"
                className="text-gray-700 dark:text-gray-300"
              >
                Confirmar Contraseña
              </Label>
              <Input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                placeholder="••••••••"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
                minLength={6}
                className="border-green-200 dark:border-green-800 focus:border-green-500 dark:focus:border-green-500"
              />
            </div>

            <Button
              type="submit"
              className="w-full bg-green-600 hover:bg-green-700 text-white"
            >
              Registrarse
            </Button>
          </form>
          {error && (
            <p className="mt-4 text-center text-red-600 dark:text-red-400">
              {error}
            </p>
          )}
          {successMessage && (
            <p className="mt-4 text-center text-green-600 dark:text-green-400">
              {successMessage}
            </p>
          )}
        </CardContent>

        <CardFooter className="flex justify-center">
          <p className="text-gray-600 dark:text-gray-400">
            ¿Ya tienes una cuenta?{" "}
            <Link
              to="/auth/login" // Ajusta si tu ruta de login es diferente (e.g., /auth/login)
              className="text-green-600 hover:text-green-800 dark:text-green-400 dark:hover:text-green-300 font-semibold"
            >
              Inicia Sesión
            </Link>
          </p>
        </CardFooter>
      </Card>
    </div>
  );
};
