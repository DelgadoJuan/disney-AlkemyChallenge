import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  getCharacters,
  createCharacter,
  updateCharacter,
  deleteCharacter,
} from "@/services/charactersService";
import { type Character } from "@/types/CharacterTypes";
import { toast } from "sonner";
import { getImageUrl } from "@/lib/utils";

export const CharacterSection = () => {
  const [characters, setCharacters] = useState<Character[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingCharacter, setEditingCharacter] = useState<Character | null>(
    null
  );
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  // Estados del formulario
  const [formData, setFormData] = useState({
    nombre: "",
    edad: "",
    peso: "",
    historia: "",
  });

  // Estados para paginación
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<keyof Character>("id");
  const [sortAsc, setSortAsc] = useState(true);

  const loadCharacters = async () => {
    try {
      const data = await getCharacters();
      setCharacters(data);
    } catch (error) {
      toast.error("Error al cargar los personajes");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadCharacters();
  }, []);

  const openDialog = (character?: Character) => {
    if (character) {
      setEditingCharacter(character);
      setFormData({
        nombre: character.nombre,
        edad: character.edad.toString(),
        peso: character.peso.toString(),
        historia: character.historia || "",
      });
    } else {
      setEditingCharacter(null);
      setFormData({
        nombre: "",
        edad: "",
        peso: "",
        historia: "",
      });
    }
    setSelectedFile(null);
    setError("");
    setIsDialogOpen(true);
  };

  const closeDialog = () => {
    setIsDialogOpen(false);
    setEditingCharacter(null);
    setFormData({
      nombre: "",
      edad: "",
      peso: "",
      historia: "",
    });
    setSelectedFile(null);
    setError("");
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError("");

    try {
      if (!selectedFile && !editingCharacter) {
        setError("Debes seleccionar una imagen");
        setIsSubmitting(false);
        return;
      }

      const formDataToSend = new FormData();
      formDataToSend.append("nombre", formData.nombre);
      formDataToSend.append("edad", formData.edad);
      formDataToSend.append("peso", formData.peso);
      formDataToSend.append("historia", formData.historia);
      if (selectedFile) {
        formDataToSend.append("imagen", selectedFile);
      }

      if (editingCharacter) {
        await updateCharacter(editingCharacter.id, formDataToSend);
        toast.success("Personaje actualizado correctamente");
      } else {
        await createCharacter(formDataToSend);
        toast.success("Personaje creado correctamente");
      }

      closeDialog();
      loadCharacters();
    } catch (error) {
      setError("Error al guardar el personaje");
      toast.error("Error al guardar el personaje");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (character: Character) => {
    openDialog(character);
  };

  const handleDelete = async (id: number) => {
    if (
      window.confirm("¿Estás seguro de que deseas eliminar este personaje?")
    ) {
      try {
        await deleteCharacter(id);
        toast.success("Personaje eliminado correctamente");
        loadCharacters();
      } catch (error) {
        toast.error("Error al eliminar el personaje");
      }
    }
  };

  // Filtrado y ordenamiento
  const filteredCharacters = characters.filter((c) =>
    c.nombre.toLowerCase().includes(search.toLowerCase())
  );

  const sortedCharacters = [...filteredCharacters].sort((a, b) => {
    const aValue = a[sortBy];
    const bValue = b[sortBy];
    
    if (typeof aValue === "string" && typeof bValue === "string") {
      return sortAsc ? aValue.localeCompare(bValue) : bValue.localeCompare(aValue);
    }
    
    if (typeof aValue === "number" && typeof bValue === "number") {
      return sortAsc ? aValue - bValue : bValue - aValue;
    }
    
    return 0;
  });

  // Paginación
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentCharacters = sortedCharacters.slice(
    indexOfFirstItem,
    indexOfLastItem
  );
  const totalPages = Math.ceil(sortedCharacters.length / itemsPerPage);

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

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Personajes</h2>
        <Button
          className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
          onClick={() => openDialog()}
        >
          Nuevo Personaje
        </Button>
      </div>

      {/* Buscador */}
      <div className="flex items-center gap-2 mb-2">
        <Input
          type="text"
          placeholder="Buscar por nombre..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setCurrentPage(1);
          }}
          className="max-w-xs"
        />
      </div>

      {/* Dialog para el formulario */}
      <Dialog
        open={isDialogOpen}
        onOpenChange={(open) => (!open ? closeDialog() : undefined)}
      >
        <DialogContent
          className="max-w-md w-full bg-white rounded-2xl shadow-2xl border border-gray-200 p-8 animate-fadeIn"
          style={{ zIndex: 50 }}
        >
          <DialogHeader>
            <DialogTitle className="text-center text-2xl font-semibold mb-2">
              {editingCharacter ? "Editar Personaje" : "Nuevo Personaje"}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-2">
              <Label htmlFor="nombre">Nombre</Label>
              <Input
                id="nombre"
                value={formData.nombre}
                onChange={(e) => setFormData({ ...formData, nombre: e.target.value })}
                required
                autoFocus
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="edad">Edad</Label>
              <Input
                id="edad"
                type="number"
                value={formData.edad}
                onChange={(e) => setFormData({ ...formData, edad: e.target.value })}
                required
                min="1"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="peso">Peso (kg)</Label>
              <Input
                id="peso"
                type="number"
                step="0.1"
                value={formData.peso}
                onChange={(e) => setFormData({ ...formData, peso: e.target.value })}
                required
                min="0.1"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="historia">Historia (opcional)</Label>
              <Textarea
                id="historia"
                value={formData.historia}
                onChange={(e) => setFormData({ ...formData, historia: e.target.value })}
                rows={3}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="imagen">Imagen</Label>
              <Input
                id="imagen"
                type="file"
                accept="image/*"
                onChange={handleFileChange}
              />
            </div>
            {error && (
              <div className="text-red-600 text-sm text-center">{error}</div>
            )}
            <div className="flex gap-2">
              <Button type="submit" className="w-full" disabled={isSubmitting}>
                {isSubmitting
                  ? "Guardando..."
                  : editingCharacter
                  ? "Actualizar"
                  : "Crear"}
              </Button>
            </div>
          </form>
        </DialogContent>
      </Dialog>

      {/* Tabla y paginación */}
      {isLoading ? (
        <div>Cargando...</div>
      ) : (
        <div className="space-y-4">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "id") setSortAsc((asc) => !asc);
                    else {
                      setSortBy("id");
                      setSortAsc(true);
                    }
                  }}
                >
                  ID {sortBy === "id" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "nombre") setSortAsc((asc) => !asc);
                    else {
                      setSortBy("nombre");
                      setSortAsc(true);
                    }
                  }}
                >
                  Nombre {sortBy === "nombre" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "edad") setSortAsc((asc) => !asc);
                    else {
                      setSortBy("edad");
                      setSortAsc(true);
                    }
                  }}
                >
                  Edad {sortBy === "edad" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "peso") setSortAsc((asc) => !asc);
                    else {
                      setSortBy("peso");
                      setSortAsc(true);
                    }
                  }}
                >
                  Peso {sortBy === "peso" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead>Historia</TableHead>
                <TableHead>Imagen</TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {currentCharacters.map((character) => (
                <TableRow key={character.id}>
                  <TableCell>{character.id}</TableCell>
                  <TableCell>{character.nombre}</TableCell>
                  <TableCell>{character.edad}</TableCell>
                  <TableCell>{character.peso} kg</TableCell>
                  <TableCell>
                    {character.historia ? (
                      <div
                        className="max-w-xs truncate"
                        title={character.historia}
                      >
                        {character.historia}
                      </div>
                    ) : (
                      <span className="text-gray-400">Sin historia</span>
                    )}
                  </TableCell>
                  <TableCell>
                    <img
                      src={getImageUrl(character.imagen) + `?t=${new Date().getTime()}`}
                      alt={character.nombre}
                      className="w-16 h-16 object-cover rounded"
                    />
                  </TableCell>
                  <TableCell>
                    <div className="space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
                        onClick={() => handleEdit(character)}
                      >
                        Editar
                      </Button>
                      <Button
                        variant="destructive"
                        size="sm"
                        className="bg-red-600 cursor-pointer hover:bg-red-700 text-white"
                        onClick={() => handleDelete(character.id)}
                      >
                        Eliminar
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>

          {/* Información de paginación */}
          <div className="flex items-center justify-between">
            <div className="text-sm text-gray-600 dark:text-gray-400">
              Mostrando {indexOfFirstItem + 1} a{" "}
              {Math.min(indexOfLastItem, characters.length)} de{" "}
              {characters.length} personajes
            </div>
          </div>

          {/* Componente de paginación */}
          {totalPages > 1 && (
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
          )}
        </div>
      )}
    </div>
  );
};

// Animación fadeIn para el Dialog
if (typeof window !== "undefined") {
  const style = document.createElement("style");
  style.innerHTML = `@keyframes fadeIn { from { opacity: 0; transform: scale(0.95);} to { opacity: 1; transform: scale(1);} } .animate-fadeIn { animation: fadeIn 0.2s ease; }`;
  document.head.appendChild(style);
}
