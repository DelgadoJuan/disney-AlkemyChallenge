import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Button } from "../components/ui/button";
import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useUsuario } from "../context/UsuarioContext";
import { login as loginService } from "../services/authService";

export const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const { setJwt, setUser, isAuthenticated } = useUsuario();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    try {
      const data = await loginService(email, password);
      if (data.JWT && data.id && data.username && data.email && data.role) {
        localStorage.setItem("jwtToken", data.JWT);
        setJwt(data.JWT);
        setUser({
          id: parseInt(data.id),
          username: data.username,
          email: data.email,
          role: data.role,
        });
        navigate("/");
      } else if (data.Error) {
        setError(data.Error);
      } else {
        setError("Error desconocido. Intenta nuevamente.");
      }
    } catch (err: any) {
      setError(err.response?.data?.Error || "Error de red o del servidor.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-blue-50 to-purple-50 p-4">
      <div className="absolute inset-0 bg-gradient-to-b from-blue-50 to-purple-50"></div>
      <Card className="w-full max-w-md relative z-10 border-2 border-blue-100">
        <CardHeader className="text-center">
          <CardTitle className="text-3xl font-bold text-blue-700">
            Iniciar Sesión
          </CardTitle>
          <CardDescription className="text-gray-600">
            Ingresa tus credenciales para acceder
          </CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label
              htmlFor="email"
              className="text-gray-700"
            >
              Email
            </Label>
            <Input
              id="email"
              type="email"
              placeholder="tu@email.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="border-blue-200 focus:border-blue-500"
            />
          </div>
          <div className="space-y-2">
            <Label
              htmlFor="password"
              className="text-gray-700"
            >
              Contraseña
            </Label>
            <Input
              id="password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="border-blue-200 focus:border-blue-500"
            />
          </div>
          {error && (
            <p className="mt-4 text-center text-red-600">
              {error}
            </p>
          )}
          <Button
            type="submit"
            onClick={handleLogin}
            className="w-full bg-blue-600 hover:bg-blue-700"
          >
            Iniciar Sesión
          </Button>
          <p className="text-gray-600">
            ¿No tienes una cuenta?{" "}
            <Link
              to="/auth/register"
              className="text-blue-600 hover:text-blue-800 font-semibold"
            >
              Regístrate aquí
            </Link>
          </p>
        </CardContent>
      </Card>
    </div>
  );
};
