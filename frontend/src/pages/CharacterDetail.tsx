import { useEffect, useState } from "react";
import { type Character } from "../types/CharacterTypes";
import { Button } from "../components/ui/button";
import { ArrowLeft } from "lucide-react";
import { Card, CardContent } from "../components/ui/card";
import { Link, useNavigate, useParams } from "react-router-dom";
import { getCharacterById } from "../services/charactersService";
import { getImageUrl } from "../lib/utils";

export const CharacterDetail = () => {
  const navigate = useNavigate();
  const [character, setCharacter] = useState<Character | null>(null);
  const { id } = useParams();
  const characterId = Number(id);

  // Cargar datos del personaje
  useEffect(() => {
    getCharacterById(characterId).then((character) => {
      setCharacter(character);
    });
  }, [characterId]);

  if (!character) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-700 dark:text-gray-300">
            Personaje no encontrado
          </h2>
          <Button className="mt-4" onClick={() => navigate("/")}>
            Volver al Inicio
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 py-8">
      <div className="container mx-auto px-4">
        {/* Botón de regreso */}
        <Button
          variant="ghost"
          className="mb-6 text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300"
          onClick={() => navigate("/")}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Volver al Inicio
        </Button>

        {/* Información principal del personaje */}
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg overflow-hidden mb-8">
          <div className="md:flex">
            <div className="md:w-1/3">
              <img
                src={getImageUrl(character.imagen)}
                alt={character.nombre}
                className="w-full h-96 md:h-full object-cover"
              />
            </div>
            <div className="md:w-2/3 p-8">
              <h1 className="text-4xl font-bold mb-6 text-blue-700 dark:text-blue-300">
                {character.nombre}
              </h1>

              {/* Información básica */}
              <div className="grid grid-cols-2 gap-6 mb-6">
                <div className="bg-blue-50 dark:bg-blue-900/30 rounded-lg p-4">
                  <h3 className="text-lg font-semibold text-blue-700 dark:text-blue-300 mb-2">
                    Edad
                  </h3>
                  <p className="text-2xl font-bold text-gray-800 dark:text-gray-200">
                    {character.edad} años
                  </p>
                </div>
                <div className="bg-purple-50 dark:bg-purple-900/30 rounded-lg p-4">
                  <h3 className="text-lg font-semibold text-purple-700 dark:text-purple-300 mb-2">
                    Peso
                  </h3>
                  <p className="text-2xl font-bold text-gray-800 dark:text-gray-200">
                    {character.peso} kg
                  </p>
                </div>
              </div>

              {/* Historia del personaje */}
              <div className="mb-6">
                <h3 className="text-xl font-semibold text-gray-800 dark:text-gray-200 mb-3">
                  Historia
                </h3>
                <p className="text-gray-600 dark:text-gray-400 leading-relaxed text-justify">
                  {character.historia}
                </p>
              </div>
            </div>
          </div>
        </div>

        {/* Sección de películas */}
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6">
          <h2 className="text-3xl font-bold mb-6 text-blue-700 dark:text-blue-300">
            Películas ({character.audiovisuales?.length})
          </h2>

          <p className="text-gray-600 dark:text-gray-400 mb-6">
            Descubre todas las películas en las que {character.nombre} ha
            participado a lo largo de los años.
          </p>

          {character.audiovisuales?.length === 0 ? (
            <div className="text-center py-8">
              <h3 className="text-xl font-medium text-gray-700 dark:text-gray-300">
                No hay películas registradas
              </h3>
              <p className="text-gray-500 dark:text-gray-400 mt-2">
                Este personaje aún no tiene películas asociadas.
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {character.audiovisuales?.map((movie) => (
                <Link to={`/movie/${movie.id}`} key={movie.id}>
                  <Card
                    key={movie.id}
                    className="overflow-hidden hover:shadow-lg transition-all duration-300 border-2 border-blue-100 dark:border-blue-900 hover:border-blue-300 dark:hover:border-blue-700 cursor-pointer group"
                  >
                    <div className="relative overflow-hidden">
                      <img
                        src={getImageUrl(movie.imagen)}
                        alt={movie.titulo}
                        className="w-full h-64 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                      <div className="absolute top-2 right-2 bg-black/70 text-white px-2 py-1 rounded text-sm">
                        ⭐ {movie.calificacion || 0}
                      </div>
                      <div className="absolute bottom-2 left-2 bg-black/70 text-white px-2 py-1 rounded text-sm">
                        {movie.fechaCreacion.split("-")[0] || 0}
                      </div>
                    </div>

                    <CardContent className="p-4">
                      <h3 className="text-lg font-bold text-blue-600 dark:text-blue-400 line-clamp-2">
                        {movie.titulo}
                      </h3>
                      <Button
                        variant="outline"
                        className="w-full hover:cursor-pointer mt-3 border-blue-600 text-blue-600 hover:bg-blue-50 dark:border-blue-400 dark:text-blue-400 dark:hover:bg-blue-950"
                        onClick={() => navigate(`/movie/${movie.id}`)}
                      >
                        Ver Película
                      </Button>
                    </CardContent>
                  </Card>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};
export default CharacterDetail;
