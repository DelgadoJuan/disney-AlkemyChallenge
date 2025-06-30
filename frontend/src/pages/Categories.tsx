import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { getCategories } from "@/services/categoryService";
import { useEffect, useState } from "react";
import type { Category } from "@/types/CategoryTypes";
import { Link } from "react-router-dom";
import { getImageUrl } from "@/lib/utils";

export default function Categories() {
  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    getCategories().then((categories) => {
      setCategories(categories);
    });
  }, []);

  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950 py-8">
      <div className="container mx-auto px-4">
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold mb-4 text-blue-700 dark:text-blue-300">
            Categorías de películas
          </h1>
          <p className="text-xl text-gray-600 dark:text-gray-400 max-w-2xl mx-auto">
            Explora nuestras diferentes categorías de películas Disney.
            Selecciona una categoría para ver todas las películas que pertenecen
            a ella.
          </p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {categories.map((category) => (
            <Link to={`/movies?category=${category.nombre}`}>
              <Card
                key={category.id}
                className="overflow-hidden border-4 relative w-full aspect-[4/3] border-blue-600 rounded-2xl shadow-xl hover:shadow-2xl transition-all duration-300 cursor-pointer group"
              >
                <img
                  src={getImageUrl(category.imagen)}
                  alt={category.nombre}
                  className="absolute inset-0 w-full h-full object-cover object-center group-hover:scale-105 transition-transform duration-300"
                />

                <div className="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent" />
                <div className="absolute bottom-4 left-4 text-white">
                  <h3 className="text-xl font-bold mb-1">{category.nombre}</h3>
                </div>
              </Card>
            </Link>
          ))}
        </div>

        <div className="mt-16 text-center">
          <div className="bg-white dark:bg-gray-900 rounded-lg shadow-md p-8 max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold mb-4 text-blue-700 dark:text-blue-300">
              ¿No encuentras lo que buscas?
            </h2>
            <p className="text-gray-600 dark:text-gray-400 mb-6">
              Explora todas nuestras películas sin filtros o utiliza la búsqueda
              para encontrar exactamente lo que quieres ver.
            </p>
            <Link to="/movies">
              <Button
                variant="outline"
                className="border-blue-600 text-blue-600 hover:bg-blue-50 dark:border-blue-400 dark:text-blue-400 dark:hover:bg-blue-950"
              >
                Ver todas las películas
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
}
