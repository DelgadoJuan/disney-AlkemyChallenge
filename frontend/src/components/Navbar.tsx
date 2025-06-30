import { Button } from "./ui/button";
import disneyLogo from "../assets/icons8-disney-1.svg";
import { useNavigate, Link } from "react-router-dom";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { User, LogOut, Shield } from "lucide-react";
import { useUsuario } from "../context/UsuarioContext";

export default function Navbar() {
  const { isAuthenticated, logout, user } = useUsuario();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/auth/login");
  }

  return (
    <header className="sticky top-0 z-50 back w-full border-b bg-blue-50 backdrop-blur shadow-sm">
      <div className="max-w-7xl mx-auto pr-4">
        <div className="flex h-16 items-center justify-between w-full">
          {/* Logo y nombre completamente a la izquierda */}
          <div className="flex items-center min-w-0 flex-shrink-0 absolute left-0 pl-0 ml-0 h-16">
            <Link to="/" className="flex items-center gap-2 group h-16 px-4">
              <img src={disneyLogo} alt="Disney Logo" className="h-9 w-9 drop-shadow-md group-hover:scale-105 transition-transform" />
              <span className="text-2xl font-extrabold text-blue-700 tracking-tight group-hover:text-blue-900 transition-colors whitespace-nowrap">
                Disney World
              </span>
            </Link>
          </div>

          {/* Centro: navegación */}
          <div className="flex-1 flex flex-col items-center justify-center">
            <nav className="flex items-center gap-6">
              <Link
                to="/categories"
                className="px-3 py-2 rounded-md text-base font-semibold text-blue-700 hover:text-blue-900 hover:bg-blue-100 transition-colors"
              >
                Categorías
              </Link>
              <Link
                to="/movies"
                className="px-3 py-2 rounded-md text-base font-semibold text-blue-700 hover:text-blue-900 hover:bg-blue-100 transition-colors"
              >
                Películas
              </Link>
            </nav>
          </div>

          {/* Usuario/Avatar completamente a la derecha */}
          <div className="flex items-center gap-4 min-w-0 flex-shrink-0 absolute right-0 pr-0 h-16">
            {isAuthenticated ? (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button
                    variant="ghost"
                    className="relative h-10 w-10 rounded-full border border-blue-200 hover:border-blue-400 shadow"
                  >
                    <Avatar className="h-10 w-10">
                      <AvatarImage
                        src="/placeholder-user.jpg"
                        alt="Avatar de usuario"
                      />
                      <AvatarFallback>
                        {user?.email?.[0]?.toUpperCase() ||
                          user?.username?.[0]?.toUpperCase() ||
                          "U"}
                      </AvatarFallback>
                    </Avatar>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-56" align="end" forceMount>
                  <DropdownMenuLabel className="font-normal">
                    <div className="flex flex-col space-y-1">
                      <p className="text-sm font-medium leading-none">
                        {user?.username || "Usuario"}
                      </p>
                      <p className="text-xs leading-none text-muted-foreground">
                        {user?.email || "usuario@ejemplo.com"}
                      </p>
                    </div>
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  {user?.role === "ADMIN" && (
                    <DropdownMenuItem onClick={() => navigate("/admin")}> 
                      <Shield className="mr-2 h-4 w-4" />
                      <span>Admin</span>
                    </DropdownMenuItem>
                  )}
                  <DropdownMenuItem onClick={() => navigate("/profile")}> 
                    <User className="mr-2 h-4 w-4" />
                    <span>Perfil</span>
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem onClick={handleLogout}>
                    <LogOut className="mr-2 h-4 w-4" />
                    <span>Cerrar Sesión</span>
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            ) : (
              <Link to="/auth/login">
                <Button
                  variant="default"
                  className="bg-blue-600 hover:bg-blue-700 text-white shadow"
                >
                  Iniciar Sesión
                </Button>
              </Link>
            )}
          </div>
        </div>
      </div>
    </header>
  );
}
