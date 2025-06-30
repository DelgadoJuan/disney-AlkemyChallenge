import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import {
  getAudiovisualById,
  addCharacterToAudiovisual,
  removeCharacterFromAudiovisual,
} from "@/services/movieService";
import { getCharacters } from "@/services/charactersService";
import { type MovieDetail } from "@/types/MovieTypes";
import { type Character } from "@/types/CharacterTypes";
import { toast } from "sonner";
import { ArrowLeft } from "lucide-react";
import { getImageUrl } from "@/lib/utils";

export const AdminMovieCharacters = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [audiovisual, setAudiovisual] = useState<MovieDetail | null>(null);
  const [allCharacters, setAllCharacters] = useState<Character[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [selectedCharacterId, setSelectedCharacterId] = useState("");
  const [search, setSearch] = useState("");
  const [dialogSearch, setDialogSearch] = useState("");

  // Estados para paginación
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  useEffect(() => {
    if (id) {
      loadAudiovisual(parseInt(id));
      loadAllCharacters();
    }
  }, [id]);

  const loadAudiovisual = async (audiovisualId: number) => {
    try {
      const data = await getAudiovisualById(audiovisualId);
      setAudiovisual(data);
    } catch (error) {
      toast.error("Error al cargar el audiovisual");
      navigate("/admin/movies");
    } finally {
      setIsLoading(false);
    }
  };

  const loadAllCharacters = async () => {
    try {
      const data = await getCharacters();
      setAllCharacters(data);
    } catch (error) {
      toast.error("Error al cargar los personajes");
    }
  };

  const handleAddCharacter = async () => {
    if (!selectedCharacterId || !id) return;

    try {
      await addCharacterToAudiovisual(
        parseInt(id),
        parseInt(selectedCharacterId)
      );
      toast.success("Personaje agregado correctamente");
      loadAudiovisual(parseInt(id));
      setIsAddDialogOpen(false);
      setSelectedCharacterId("");
      setDialogSearch("");
    } catch (error) {
      toast.error("Error al agregar el personaje");
    }
  };

  const handleRemoveCharacter = async (characterId: number) => {
    if (!id) return;

    if (
      window.confirm(
        "¿Estás seguro de que deseas eliminar este personaje del audiovisual?"
      )
    ) {
      try {
        await removeCharacterFromAudiovisual(parseInt(id), characterId);
        toast.success("Personaje eliminado correctamente");
        loadAudiovisual(parseInt(id));
      } catch (error) {
        toast.error("Error al eliminar el personaje");
      }
    }
  };

  // Filtrar personajes que no están en el audiovisual
  const availableCharacters = allCharacters.filter(
    (character) =>
      !audiovisual?.personajes?.some((p) => p.id === character.id) &&
      character.nombre.toLowerCase().includes(dialogSearch.toLowerCase())
  );

  // Filtrar personajes del audiovisual
  const audiovisualCharacters =
    audiovisual?.personajes?.filter((character) =>
      character.nombre.toLowerCase().includes(search.toLowerCase())
    ) || [];

  // Paginación para personajes disponibles
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAvailableCharacters = availableCharacters.slice(
    indexOfFirstItem,
    indexOfLastItem
  );
  const totalPages = Math.ceil(availableCharacters.length / itemsPerPage);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const getPageNumbers = () => {
    const pages = [];
    const maxVisiblePages = 5;
    if (totalPages <= maxVisiblePages) {
      for (let i = 1; i <= totalPages; i++) {
        pages.push(i);
      }
    } else {
      if (currentPage <= 3) {
        for (let i = 1; i <= 4; i++) {
          pages.push(i);
        }
        pages.push(totalPages);
      } else if (currentPage >= totalPages - 2) {
        pages.push(1);
        for (let i = totalPages - 3; i <= totalPages; i++) {
          pages.push(i);
        }
      } else {
        pages.push(1);
        for (let i = currentPage - 1; i <= currentPage + 1; i++) {
          pages.push(i);
        }
        pages.push(totalPages);
      }
    }
    return pages;
  };

  if (isLoading) {
    return (
      <div className="flex justify-center items-center h-64">Cargando...</div>
    );
  }

  if (!audiovisual) {
    return (
      <div className="flex justify-center items-center h-64">
        Audiovisual no encontrado
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Cabecera */}
      <div className="flex flex-col items-center mb-2">
        <div className="w-full flex items-center gap-2 mb-2">
          <Link to="/admin/movies">
            <Button
              variant="ghost"
              className="flex justify-center items-center text-blue-600 hover:text-blue-700"
              size="icon"
              title="Volver"
            >
              <ArrowLeft className="h-4 w-4" />
            </Button>
          </Link>
          <h2 className="text-2xl font-bold text-blue-700 dark:text-blue-300">
            Gestión de Personajes - {audiovisual.titulo}
          </h2>
        </div>
        <Button
          onClick={() => setIsAddDialogOpen(true)}
          className="bg-blue-600 hover:bg-blue-700 text-white"
        >
          Agregar Personaje
        </Button>
      </div>

      {/* Buscador */}
      <div className="flex items-center gap-2 mb-2">
        <Input
          type="text"
          placeholder="Buscar personajes del audiovisual..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="max-w-xs"
        />
      </div>

      {/* Lista de personajes del audiovisual */}
      <div className="bg-white dark:bg-gray-900 rounded-lg shadow-lg p-6">
        <h3 className="text-xl font-bold mb-4 text-blue-700 dark:text-blue-300">
          Personajes del Audiovisual ({audiovisualCharacters.length})
        </h3>

        {audiovisualCharacters.length === 0 ? (
          <div className="text-center py-8 text-gray-400">
            {search
              ? "No se encontraron personajes con ese nombre"
              : "No hay personajes en este audiovisual"}
          </div>
        ) : (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Imagen</TableHead>
                <TableHead>Nombre</TableHead>
                <TableHead>Edad</TableHead>
                <TableHead>Peso</TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {audiovisualCharacters.map((character) => (
                <TableRow key={character.id}>
                  <TableCell>
                    <img
                      src={getImageUrl(character.imagen) + `?t=${new Date().getTime()}`}
                      alt={character.nombre}
                      className="w-16 h-16 object-cover rounded shadow"
                    />
                  </TableCell>
                  <TableCell className="font-medium text-gray-800">{character.nombre}</TableCell>
                  <TableCell>{character.edad} años</TableCell>
                  <TableCell>{character.peso} kg</TableCell>
                  <TableCell>
                    <Button
                      variant="destructive"
                      size="sm"
                      className="bg-red-600 cursor-pointer hover:bg-red-700 text-white px-3"
                      onClick={() => handleRemoveCharacter(character.id)}
                    >
                      Eliminar
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </div>

      {/* Dialog para agregar personajes */}
      <Dialog
        open={isAddDialogOpen}
        onOpenChange={(open) => {
          if (!open) {
            setIsAddDialogOpen(false);
            setSelectedCharacterId("");
            setDialogSearch("");
            setCurrentPage(1);
          }
        }}
      >
        <DialogContent
          className="max-w-2xl w-full bg-white rounded-2xl shadow-2xl border border-gray-200 p-8 animate-fadeIn"
          style={{ zIndex: 50 }}
        >
          <DialogHeader>
            <DialogTitle className="text-center text-2xl font-semibold mb-2">
              Agregar Personaje a {audiovisual.titulo}
            </DialogTitle>
          </DialogHeader>
          <div className="space-y-4 flex flex-col h-full">
            <div>
              <Label>Buscar personaje</Label>
              <Input
                placeholder="Buscar personajes disponibles..."
                value={dialogSearch}
                onChange={(e) => {
                  setDialogSearch(e.target.value);
                  setCurrentPage(1);
                }}
                className="mt-1 border-gray-300 shadow-sm"
              />
            </div>
            <div className="flex-1 overflow-y-auto min-h-0">
              {availableCharacters.length === 0 ? (
                <div className="text-center py-8 text-gray-400">
                  {dialogSearch
                    ? "No se encontraron personajes con ese nombre"
                    : "No hay personajes disponibles para agregar"}
                </div>
              ) : (
                <div className="space-y-2">
                  {currentAvailableCharacters.map((character) => (
                    <div
                      key={character.id}
                      className={`p-3 border rounded-lg cursor-pointer hover:bg-blue-50 transition-colors flex items-center gap-3 ${
                        selectedCharacterId === character.id.toString()
                          ? "border-blue-500 bg-blue-50"
                          : "border-gray-200"
                      }`}
                      onClick={() =>
                        setSelectedCharacterId(character.id.toString())
                      }
                    >
                      <img
                        src={getImageUrl(character.imagen) + `?t=${new Date().getTime()}`}
                        alt={character.nombre}
                        className="w-12 h-12 object-cover rounded shadow"
                      />
                      <div>
                        <p className="font-medium text-gray-800">{character.nombre}</p>
                        <p className="text-sm text-gray-600">
                          {character.edad} años • {character.peso} kg
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
            {/* Paginación */}
            {totalPages > 1 && (
              <div className="border-t pt-4">
                <Pagination>
                  <PaginationContent>
                    <PaginationItem>
                      <PaginationPrevious
                        onClick={() => handlePageChange(currentPage - 1)}
                        className={
                          currentPage === 1
                            ? "pointer-events-none opacity-50"
                            : "cursor-pointer"
                        }
                      />
                    </PaginationItem>
                    {getPageNumbers().map((page) => (
                      <PaginationItem key={page}>
                        <PaginationLink
                          onClick={() => handlePageChange(page)}
                          isActive={currentPage === page}
                          className="cursor-pointer"
                        >
                          {page}
                        </PaginationLink>
                      </PaginationItem>
                    ))}
                    <PaginationItem>
                      <PaginationNext
                        onClick={() => handlePageChange(currentPage + 1)}
                        className={
                          currentPage === totalPages
                            ? "pointer-events-none opacity-50"
                            : "cursor-pointer"
                        }
                      />
                    </PaginationItem>
                  </PaginationContent>
                </Pagination>
              </div>
            )}
            <div className="flex justify-between items-center pt-4 border-t">
              <div className="text-sm text-gray-600">
                Mostrando {indexOfFirstItem + 1} a {" "}
                {Math.min(indexOfLastItem, availableCharacters.length)} de {" "}
                {availableCharacters.length} personajes disponibles
              </div>
              <div className="flex justify-end space-x-2">
                <Button
                  variant="outline"
                  onClick={() => {
                    setIsAddDialogOpen(false);
                    setSelectedCharacterId("");
                    setDialogSearch("");
                    setCurrentPage(1);
                  }}
                >
                  Cancelar
                </Button>
                <Button
                  onClick={handleAddCharacter}
                  disabled={!selectedCharacterId}
                  className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white font-semibold px-6 py-2 rounded-lg shadow"
                >
                  Agregar Personaje
                </Button>
              </div>
            </div>
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};
