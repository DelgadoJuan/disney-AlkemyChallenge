import { useState, useEffect } from "react";
import { Button } from "../components/ui/button";
import { Card, CardContent } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "../components/ui/select";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "../components/ui/pagination";
import { MagnifyingGlassIcon } from "../components/icons";
import { getMovies } from "../services/movieService";
import { type Movie } from "../types/MovieTypes";
import { getCategories } from "../services/categoryService";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { getImageUrl } from "../lib/utils";

export default function MoviesList() {
  const navigate = useNavigate()
  const location = useLocation()
  const searchParams = new URLSearchParams(location.search)
  const initialCategory = searchParams.get("category") || "Todas las categorías"

  // Estados para manejar filtros y ordenamiento
  const [movies, setMovies] = useState<Movie[]>([]);
  const [filteredMovies, setFilteredMovies] = useState<Movie[]>([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [sortBy, setSortBy] = useState("title");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [currentPage, setCurrentPage] = useState(1);
  const moviesPerPage = 6;
  const [categories, setCategories] = useState<string[]>([]);

  useEffect(() => {
    const fetchCategories = async () => {
      const categories = await getCategories();
      setCategories(["Todas las categorías", ...categories.map((category) => category.nombre)]);
    };
    fetchCategories();
  }, []);

  useEffect(() => {
    getMovies().then((movies) => {
      setMovies(movies as Movie[]);
    });
  }, []);

  // Efecto para filtrar y ordenar películas
  useEffect(() => {
    let result = [...movies];

    // Filtrar por búsqueda
    if (searchQuery) {
      result = result.filter((movie) =>
        movie.titulo.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Filtrar por categoría
    if (selectedCategory !== "Todas las categorías") {
      result = result.filter(
        (movie) => movie.nombreGenero === selectedCategory
      );
    }

    // Ordenar
    result.sort((a, b) => {
      if (sortBy === "title") {
        return sortOrder === "asc"
          ? a.titulo.localeCompare(b.titulo)
          : b.titulo.localeCompare(a.titulo);
      } else if (sortBy === "year") {
        return sortOrder === "asc"
          ? a.duracion - b.duracion
          : b.duracion - a.duracion;
      } else if (sortBy === "rating") {
        return sortOrder === "asc"
          ? a.calificacion - b.calificacion
          : b.calificacion - a.calificacion;
      } else if (sortBy === "duration") {
        return sortOrder === "asc"
          ? a.duracion - b.duracion
          : b.duracion - a.duracion;
      }
      return 0;
    });

    setFilteredMovies(result);
    setCurrentPage(1);
  }, [movies, searchQuery, selectedCategory, sortBy, sortOrder]);

  // Calcular películas para la página actual
  const indexOfLastMovie = currentPage * moviesPerPage;
  const indexOfFirstMovie = indexOfLastMovie - moviesPerPage;
  const currentMovies = filteredMovies.slice(
    indexOfFirstMovie,
    indexOfLastMovie
  );
  const totalPages = Math.ceil(filteredMovies.length / moviesPerPage);

  // Cambiar página
  const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

  // Cambiar orden
  const toggleSortOrder = () => {
    setSortOrder(sortOrder === "asc" ? "desc" : "asc");
  };

  // Navegar a detalle de película
  const handleMovieClick = (movieId: number) => {
    navigate(`/movie/${movieId}`)
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-4xl font-bold text-center mb-8 text-blue-700 dark:text-blue-300">
          Películas de Disney
        </h1>

        {/* Filtros y Búsqueda */}
        <div className="bg-white dark:bg-gray-900 rounded-lg shadow-md p-6 mb-8">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            {/* Búsqueda */}
            <div className="space-y-2">
              <Label
                htmlFor="search"
                className="text-gray-700 dark:text-gray-300"
              >
                Buscar película
              </Label>
              <div className="relative">
                <Input
                  id="search"
                  type="text"
                  placeholder="Buscar por título..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                />
                <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-500 dark:text-gray-400" />
              </div>
            </div>

            {/* Filtro por categoría */}
            <div className="space-y-2">
              <Label
                htmlFor="category-filter"
                className="text-gray-700 dark:text-gray-300"
              >
                Filtrar por categoría
              </Label>
              <Select
                value={selectedCategory}
                onValueChange={setSelectedCategory}
              >
                <SelectTrigger id="category-filter" className="w-full">
                  <SelectValue placeholder="Selecciona una categoría" />
                </SelectTrigger>
                <SelectContent>
                  {categories.map((category) => (
                    <SelectItem key={category} value={category}>
                      {category}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Ordenamiento */}
            <div className="space-y-2">
              <Label
                htmlFor="sort-by"
                className="text-gray-700 dark:text-gray-300"
              >
                Ordenar por
              </Label>
              <div className="flex space-x-2">
                <Select value={sortBy} onValueChange={setSortBy}>
                  <SelectTrigger id="sort-by" className="w-full">
                    <SelectValue placeholder="Ordenar por" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="title">Título</SelectItem>
                    <SelectItem value="rating">Calificación</SelectItem>
                  </SelectContent>
                </Select>
                <Button
                  variant="outline"
                  onClick={toggleSortOrder}
                  className="px-3"
                  aria-label={
                    sortOrder === "asc"
                      ? "Orden ascendente"
                      : "Orden descendente"
                  }
                >
                  {sortOrder === "asc" ? "↑" : "↓"}
                </Button>
              </div>
            </div>
          </div>
        </div>

        {/* Resultados */}
        <div className="mb-8">
          <p className="text-gray-600 dark:text-gray-400 mb-4">
            Mostrando {currentMovies.length} de {filteredMovies.length}{" "}
            películas
          </p>

          {currentMovies.length === 0 ? (
            <div className="text-center py-12 bg-white dark:bg-gray-900 rounded-lg shadow">
              <h3 className="text-xl font-medium text-gray-700 dark:text-gray-300">
                No se encontraron películas
              </h3>
              <p className="text-gray-500 dark:text-gray-400 mt-2">
                Intenta con otros filtros de búsqueda
              </p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {currentMovies.map((movie) => (
                <Link to={`/movie/${movie.id}`} key={movie.id}>
                  <Card
                    key={movie.id}
                    className="overflow-hidden hover:shadow-lg transition-all duration-300 border-2 border-blue-100 dark:border-blue-900 hover:border-blue-300 dark:hover:border-blue-700 cursor-pointer group"
                    onClick={() => handleMovieClick(movie.id)}
                  >
                    <div className="relative overflow-hidden">
                      <img
                        src={getImageUrl(movie.imagen)}
                        alt={movie.titulo}
                        className="w-full h-64 object-cover group-hover:scale-105 transition-transform duration-300"
                      />
                      <div className="absolute top-2 right-2 bg-black/70 text-white px-2 py-1 rounded text-sm">
                        ⭐ {movie.calificacion}
                      </div>
                    </div>

                    <CardContent className="p-4">
                      <h3 className="text-xl font-bold mb-2 text-blue-600 dark:text-blue-400">
                        {movie.titulo}
                      </h3>
                      <div className="space-y-1 text-sm text-gray-600 dark:text-gray-400 mb-3">
                        <p>
                          <span className="font-medium">Año:</span>{" "}
                          {movie.fechaCreacion}
                        </p>
                        <p>
                          <span className="font-medium">Duración:</span>{" "}
                          {movie.duracion} min
                        </p>
                      </div>
                      <div className="flex flex-wrap gap-1 mb-3">
                        <span className="inline-block px-2 py-1 text-xs rounded-full bg-blue-100 dark:bg-blue-800 text-blue-700 dark:text-blue-300">
                          {movie.nombreGenero}
                        </span>
                      </div>
                      <Button
                        variant="outline"
                        className="w-full border-blue-600 text-blue-600 hover:bg-blue-50 dark:border-blue-400 dark:text-blue-400 dark:hover:bg-blue-950"
                        onClick={(e) => {
                          e.stopPropagation();
                          handleMovieClick(movie.id);
                        }}
                      >
                        Ver Detalles
                      </Button>
                    </CardContent>
                  </Card>
                </Link>
              ))}
            </div>
          )}
        </div>

        {/* Paginación */}
        {filteredMovies.length > 0 && (
          <Pagination className="mt-8">
            <PaginationContent>
              <PaginationItem>
                <PaginationPrevious
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    if (currentPage > 1) paginate(currentPage - 1);
                  }}
                  className={
                    currentPage === 1 ? "pointer-events-none opacity-50" : ""
                  }
                />
              </PaginationItem>

              {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                (number) => (
                  <PaginationItem key={number}>
                    <PaginationLink
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        paginate(number);
                      }}
                      isActive={currentPage === number}
                    >
                      {number}
                    </PaginationLink>
                  </PaginationItem>
                )
              )}

              <PaginationItem>
                <PaginationNext
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    if (currentPage < totalPages) paginate(currentPage + 1);
                  }}
                  className={
                    currentPage === totalPages
                      ? "pointer-events-none opacity-50"
                      : ""
                  }
                />
              </PaginationItem>
            </PaginationContent>
          </Pagination>
        )}
      </div>
    </div>
  );
}
