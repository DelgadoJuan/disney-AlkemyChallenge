"use client"

import { Button } from "../components/ui/button"
import { Card, CardContent } from "../components/ui/card"
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from "../components/ui/carousel"
import { Input } from "../components/ui/input"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "../components/ui/tabs"
import { useEffect, useState } from "react";
import { getFeaturedCharacters } from "../services/charactersService";
import { getFeaturedAudiovisuals } from "../services/movieService";
import type { Character } from "../types/CharacterTypes";
import type { Movie } from "../types/MovieTypes";
import { Link } from "react-router-dom"
import { getImageUrl } from "../lib/utils";

export default function Home() {
  const [featuredCharacters, setFeaturedCharacters] = useState<Character[]>([]);
  const [featuredMovies, setFeaturedMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const [characters, movies] = await Promise.all([
          getFeaturedCharacters(),
          getFeaturedAudiovisuals()
        ]);
        setFeaturedCharacters(characters);
        setFeaturedMovies(movies);
      } catch (error) {
        // Manejo de error opcional
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  return (
    <main>
      {/* Hero Section */}
      <section className="relative py-20 overflow-hidden">
        <div className="absolute inset-0 z-0 bg-[url('/placeholder.svg?height=800&width=1600')] bg-cover bg-center opacity-20"></div>
        <div className="container relative z-10">
          <div className="mx-auto max-w-3xl text-center">
            <h1 className="mb-6 text-5xl font-bold tracking-tight text-blue-700 dark:text-blue-300 sm:text-6xl">
              Explora el mágico mundo de Disney
            </h1>
            <p className="mb-10 text-xl text-gray-700 dark:text-gray-300">
              Conoce a tus personajes favoritos, descubre películas clásicas y nuevas aventuras en un solo lugar.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Link to="/movies">
                <Button className="bg-blue-600 hover:bg-blue-700 text-white">Explorar películas</Button>
              </Link>
              <Link to="/categories">
                <Button
                  variant="outline"
                  className="border-blue-600 text-blue-600 hover:bg-blue-50 dark:border-blue-400 dark:text-blue-400 dark:hover:bg-blue-950"
                >
                  Ver categorías
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Content */}
      <section className="py-16 bg-blue-50 dark:bg-blue-950">
        <div className="container">
          <Tabs defaultValue="characters" className="w-full">
            <div className="flex justify-center mb-8">
              <TabsList>
                <TabsTrigger value="characters" className="text-lg px-6">
                  Personajes
                </TabsTrigger>
                <TabsTrigger value="movies" className="text-lg px-6">
                  Películas
                </TabsTrigger>
              </TabsList>
            </div>

            <TabsContent value="characters">
              <h2 className="text-3xl font-bold text-center mb-10 text-blue-700 dark:text-blue-300">
                Personajes Destacados
              </h2>
              {loading ? (
                <div className="text-center py-10 text-blue-700">Cargando...</div>
              ) : (
                <Carousel className="w-full max-w-5xl mx-auto">
                  <CarouselContent>
                    {featuredCharacters.map((character) => (
                      <CarouselItem key={character.id} className="md:basis-1/2 lg:basis-1/3">
                        <Card className="h-full overflow-hidden border-2 border-blue-100 dark:border-blue-900 hover:border-blue-300 dark:hover:border-blue-700 transition-all">
                          <img
                            src={getImageUrl(character.imagen)}
                            alt={character.nombre}
                            className="w-full h-64 object-cover"
                          />
                          <CardContent className="p-4">
                            <h3 className="text-xl font-bold mb-2 text-blue-600 dark:text-blue-400">{character.nombre}</h3>
                            <p className="text-gray-600 dark:text-gray-400">{character.historia || ''}</p>
                          </CardContent>
                        </Card>
                      </CarouselItem>
                    ))}
                  </CarouselContent>
                  <CarouselPrevious className="left-0" />
                  <CarouselNext className="right-0" />
                </Carousel>
              )}
            </TabsContent>

            <TabsContent value="movies">
              <h2 className="text-3xl font-bold text-center mb-10 text-blue-700 dark:text-blue-300">
                Películas Destacadas
              </h2>
              {loading ? (
                <div className="text-center py-10 text-blue-700">Cargando...</div>
              ) : (
                <Carousel className="w-full max-w-5xl mx-auto">
                  <CarouselContent>
                    {featuredMovies.map((movie) => (
                      <CarouselItem key={movie.id} className="md:basis-1/2 lg:basis-1/3">
                        <Card className="h-full overflow-hidden border-2 border-blue-100 dark:border-blue-900 hover:border-blue-300 dark:hover:border-blue-700 transition-all">
                          <img
                            src={getImageUrl(movie.imagen)}
                            alt={movie.titulo}
                            className="w-full h-48 object-cover"
                          />
                          <CardContent className="p-4">
                            <h3 className="text-xl font-bold mb-2 text-blue-600 dark:text-blue-400">{movie.titulo}</h3>
                            <p className="text-gray-600 dark:text-gray-400">Año: {movie.fechaCreacion ? movie.fechaCreacion.split("-")[0] : ''}</p>
                          </CardContent>
                        </Card>
                      </CarouselItem>
                    ))}
                  </CarouselContent>
                  <CarouselPrevious className="left-0" />
                  <CarouselNext className="right-0" />
                </Carousel>
              )}
            </TabsContent>
          </Tabs>
        </div>
      </section>

      {/* Newsletter Section */}
      <section className="py-16 bg-white dark:bg-gray-950">
        <div className="container">
          <div className="max-w-2xl mx-auto text-center">
            <h2 className="text-3xl font-bold mb-4 text-blue-700 dark:text-blue-300">Mantente Actualizado</h2>
            <p className="text-gray-600 dark:text-gray-400 mb-8">
              Suscríbete para recibir las últimas noticias sobre tus personajes y películas favoritas.
            </p>

            <div className="flex flex-col sm:flex-row gap-4 max-w-md mx-auto">
              <Input type="email" placeholder="Tu correo electrónico" className="flex-grow" />
              <Button className="bg-blue-600 hover:bg-blue-700 text-white whitespace-nowrap">Suscribirse</Button>
            </div>
          </div>
        </div>
      </section>
    </main>
  )
}
