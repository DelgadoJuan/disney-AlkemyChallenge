import { Button } from "@/components/ui/button";
import { ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { MovieSection } from "@/components/admin/MovieSection";

export default function AdminMovies() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50 dark:from-blue-950 dark:to-purple-950">
      <div className="flex h-screen">
        {/* Header */}
        <div className="flex-1 flex flex-col">
          <div className="flex items-center h-16 px-6 bg-white dark:bg-gray-900 shadow">
            <Link to="/admin">
              <Button
                variant="ghost"
                className="flex justify-center cursor-pointer items-center text-blue-600 hover:text-blue-700 mr-2"
              >
                <ArrowLeft className="h-7 w-7" />
              </Button>
            </Link>
            <h1 className="text-xl font-bold text-blue-600 dark:text-blue-400 ml-2">
              Gestión de Audiovisuales
            </h1>
          </div>

          {/* Contenido */}
          <div className="flex-1 p-6">
            <MovieSection />
          </div>
        </div>
      </div>
    </div>
  );
}
