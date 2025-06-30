import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { ArrowLeft, Film, Users, Tag, BarChart3 } from "lucide-react";
import { Link } from "react-router-dom";
import { Card, CardContent } from "@/components/ui/card";

export default function Admin() {
  const [stats, setStats] = useState({
    movies: 0,
    characters: 0,
    genres: 0,
    users: 0,
  });

  // Aquí podrías hacer llamadas a la API para obtener las estadísticas reales
  useEffect(() => {
    // Simular datos - en producción esto vendría de tu API
    setStats({
      movies: 45,
      characters: 128,
      genres: 12,
      users: 1250,
    });
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950">
      <div className="flex h-screen">
        {/* Header */}
        <div className="flex-1 flex flex-col">
          <div className="flex items-center h-16 px-6 bg-white dark:bg-gray-900 shadow">
            <Link to="/">
              <Button
                variant="ghost"
                className="flex justify-center items-center text-blue-600 hover:text-blue-700 mr-2"
              >
                <ArrowLeft className="h-7 w-7" />
              </Button>
            </Link>
            <h1 className="text-xl font-bold text-blue-600 dark:text-blue-400 ml-2">
              Panel de Administración
            </h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 flex items-center justify-center p-6">
            <div className="w-full max-w-5xl">
              <div className="mb-8">
                <h2 className="text-3xl font-bold text-blue-700 dark:text-blue-300 mb-2">
                  Dashboard
                </h2>
                <p className="text-gray-600 dark:text-gray-400">
                  Gestiona el contenido de Disney World
                </p>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {/* Tarjeta de Audiovisuales */}
                <Link to="/admin/movies">
                  <Card className="cursor-pointer hover:shadow-lg transition-all duration-200 hover:scale-105 bg-white dark:bg-gray-800">
                    <CardContent className="p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                            Total Audiovisuales
                          </p>
                          <p className="text-2xl font-bold text-blue-600 dark:text-blue-400">
                            {stats.movies}
                          </p>
                        </div>
                        <Film className="h-8 w-8 text-blue-600" />
                      </div>
                    </CardContent>
                  </Card>
                </Link>
                {/* Tarjeta de Personajes */}
                <Link to="/admin/characters">
                  <Card className="cursor-pointer hover:shadow-lg transition-all duration-200 hover:scale-105 bg-white dark:bg-gray-800">
                    <CardContent className="p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                            Total Personajes
                          </p>
                          <p className="text-2xl font-bold text-purple-600 dark:text-purple-400">
                            {stats.characters}
                          </p>
                        </div>
                        <Users className="h-8 w-8 text-purple-600" />
                      </div>
                    </CardContent>
                  </Card>
                </Link>
                {/* Tarjeta de Géneros */}
                <Link to="/admin/genres">
                  <Card className="cursor-pointer hover:shadow-lg transition-all duration-200 hover:scale-105 bg-white dark:bg-gray-800">
                    <CardContent className="p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                            Total Géneros
                          </p>
                          <p className="text-2xl font-bold text-green-600 dark:text-green-400">
                            {stats.genres}
                          </p>
                        </div>
                        <Tag className="h-8 w-8 text-green-600" />
                      </div>
                    </CardContent>
                  </Card>
                </Link>

                {/* Tarjeta de Usuarios */}
                <Link to="/admin/users">
                  <Card className="cursor-pointer hover:shadow-lg transition-all duration-200 hover:scale-105 bg-white dark:bg-gray-800">
                    <CardContent className="p-6">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="text-sm font-medium text-gray-600 dark:text-gray-400">
                            Total Usuarios
                          </p>
                          <p className="text-2xl font-bold text-orange-600 dark:text-orange-400">
                            {stats.users}
                          </p>
                        </div>
                        <BarChart3 className="h-8 w-8 text-orange-600" />
                      </div>
                    </CardContent>
                  </Card>
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
