import { useState, useEffect } from "react"
import { useParams } from "react-router-dom";
import { Button } from "../components/ui/button"
import { Card, CardContent } from "../components/ui/card"
import { Input } from "../components/ui/input"
import { Label } from "../components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "../components/ui/select"
import { ArrowLeft } from "lucide-react"
import { MagnifyingGlassIcon } from "../components/icons"
import { type MovieDetail } from "../types/MovieTypes";
import { type Character } from "../types/CharacterTypes";
import { getMovieById } from "../services/movieService";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { getImageUrl } from "../lib/utils";

export default function MovieDetail() {
  const [movie, setMovie] = useState<MovieDetail | null>(null)
  const [filteredCharacters, setFilteredCharacters] = useState<Character[]>([])
  const [searchQuery, setSearchQuery] = useState("")
  const [sortBy, setSortBy] = useState("name")
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc")
  const { id } = useParams()
  const movieId = Number(id)
  const navigate = useNavigate()

  // Cargar datos de la película
  useEffect(() => {
    getMovieById(movieId).then((movie) => {
      setMovie(movie as MovieDetail)
      setFilteredCharacters((movie as MovieDetail).personajes)
    })
  }, [id])

  // Efecto para filtrar y ordenar personajes
  useEffect(() => {
    if (!movie) return

    let result = [...movie.personajes]

    // Filtrar por búsqueda
    if (searchQuery) {
      result = result.filter((character) => character.nombre.toLowerCase().includes(searchQuery.toLowerCase()))
    }

    // Ordenar
    result.sort((a, b) => {
      if (sortBy === "nombre") {
        return sortOrder === "asc" ? a.nombre.localeCompare(b.nombre) : b.nombre.localeCompare(a.nombre)
      } else if (sortBy === "edad") {
        return sortOrder === "asc" ? (a.edad || 0) - (b.edad || 0) : (b.edad || 0) - (a.edad || 0)
      } else if (sortBy === "peso") {
        return sortOrder === "asc" ? (a.peso || 0) - (b.peso || 0) : (b.peso || 0) - (a.peso || 0)
      }
      return 0
    })

    setFilteredCharacters(result)
  }, [movie, searchQuery, sortBy, sortOrder])

  // Cambiar orden
  const toggleSortOrder = () => {
    setSortOrder(sortOrder === "asc" ? "desc" : "asc")
  }

  if (!movie) {
    return (
      <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-700 dark:text-gray-300">Película no encontrada</h2>
          <Button className="mt-4" onClick={() => navigate("/movies")}>
            Volver a Películas
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 py-8">
      <div className="container mx-auto px-4">
        {/* Botón de regreso */}
        <Button
          variant="ghost"
          className="mb-6 text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300"
          onClick={() => navigate("/movies")}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Volver a Películas
        </Button>

        {/* Información de la película */}
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg overflow-hidden mb-8">
          <div className="md:flex">
            <div className="md:w-1/3">
              <img
                  src={getImageUrl(movie.imagen)}
                alt={movie.titulo}
                className="w-full h-96 md:h-full object-cover"
              />
            </div>
            <div className="md:w-2/3 p-8">
              <h1 className="text-4xl font-bold mb-4 text-blue-700 dark:text-blue-300">{movie.titulo}</h1>
              <div className="grid grid-cols-2 gap-4 mb-6 text-gray-600 dark:text-gray-400">
                <div>
                  <span className="font-medium">Año:</span> {movie.fechaCreacion}
                </div>
                <div>
                  <span className="font-medium">Duración:</span> {movie.duracion} min
                </div>
                <div>
                  <span className="font-medium">Calificación:</span> ⭐ {movie.calificacion}/5
                </div>
                <div>
                  <span className="font-medium">Director:</span> {movie.director}
                </div>
              </div>
              <p className="text-gray-600 dark:text-gray-400 leading-relaxed">{movie.descripcion}</p>
            </div>
          </div>
        </div>

        {/* Sección de personajes */}
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6">
          <h2 className="text-3xl font-bold mb-6 text-blue-700 dark:text-blue-300">
            Personajes ({movie.personajes.length})
          </h2>

          {/* Filtros para personajes */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            {/* Búsqueda */}
            <div className="space-y-2">
              <Label htmlFor="character-search" className="text-gray-700 dark:text-gray-300">
                Buscar personaje
              </Label>
              <div className="relative">
                <Input
                  id="character-search"
                  type="text"
                  placeholder="Buscar por nombre..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
                <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-500 dark:text-gray-400" />
              </div>
            </div>

            {/* Ordenamiento */}
            <div className="space-y-2">
              <Label htmlFor="character-sort" className="text-gray-700 dark:text-gray-300">
                Ordenar por
              </Label>
              <div className="flex space-x-2">
                <Select value={sortBy} onValueChange={setSortBy}>
                  <SelectTrigger id="character-sort" className="w-full">
                    <SelectValue placeholder="Ordenar por" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="nombre">Nombre</SelectItem>
                    <SelectItem value="edad">Edad</SelectItem>
                    <SelectItem value="peso">Peso</SelectItem>
                  </SelectContent>
                </Select>
                <Button
                  variant="outline"
                  onClick={toggleSortOrder}
                  className="px-3"
                  aria-label={sortOrder === "asc" ? "Orden ascendente" : "Orden descendente"}
                >
                  {sortOrder === "asc" ? "↑" : "↓"}
                </Button>
              </div>
            </div>
          </div>

          {/* Lista de personajes */}
          {filteredCharacters.length === 0 ? (
            <div className="text-center py-8">
              <h3 className="text-xl font-medium text-gray-700 dark:text-gray-300">No se encontraron personajes</h3>
              <p className="text-gray-500 dark:text-gray-400 mt-2">Intenta con otros filtros de búsqueda</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredCharacters.map((character) => (
                <Link to={`/characters/${character.id}`} key={character.id}>
                  <Card
                    className="overflow-hidden border-2 border-blue-100 dark:border-blue-900 hover:border-blue-300 dark:hover:border-blue-700 transition-all"
                  >
                    <div className="flex">
                      <div className="w-1/3">
                        <img
                          src={getImageUrl(character.imagen)}
                          alt={character.nombre}
                          className="w-full h-32 object-cover"
                        />
                      </div>
                      <CardContent className="w-2/3 p-4">
                        <h3 className="text-lg font-bold mb-2 text-blue-600 dark:text-blue-400">{character.nombre}</h3>
                        <div className="space-y-1 text-sm text-gray-600 dark:text-gray-400">
                          <p>
                            <span className="font-medium">Edad:</span> {character.edad} años
                          </p>
                          <p>
                            <span className="font-medium">Peso:</span> {character.peso} kg
                          </p>
                        </div>
                      </CardContent>
                    </div>
                  </Card>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
