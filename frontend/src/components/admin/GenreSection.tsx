import { useState, useEffect } from "react";
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
  DialogTitle
} from "@/components/ui/dialog";
import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "@/services/categoryService";
import { type Category } from "@/types/CategoryTypes";
import { toast } from "sonner";
import { getImageUrl } from "@/lib/utils";

export function GenreSection() {
  const [genres, setGenres] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingGenre, setEditingGenre] = useState<Category | null>(null);
  const [nombre, setNombre] = useState("");
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Estados para paginación
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<keyof Category>("id");
  const [sortAsc, setSortAsc] = useState(true); // true = ascendente, false = descendente

  const loadGenres = async () => {
    try {
      const data = await getCategories();
      setGenres(data);
    } catch (error) {
      toast.error("Error al cargar los géneros");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadGenres();
  }, []);

  // Cuando abro el modal para editar o crear, seteo los datos
  const openDialog = (genre?: Category) => {
    if (genre) {
      setEditingGenre(genre);
      setNombre(genre.nombre);
    } else {
      setEditingGenre(null);
      setNombre("");
    }
    setSelectedFile(null);
    setError("");
    setIsDialogOpen(true);
  };

  // Cuando cierro el modal, limpio todo
  const closeDialog = () => {
    setIsDialogOpen(false);
    setEditingGenre(null);
    setNombre("");
    setSelectedFile(null);
    setError("");
    setIsSubmitting(false);
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    if (!nombre.trim()) {
      setError("El nombre es obligatorio");
      return;
    }
    if (!selectedFile && !editingGenre) {
      setError("La imagen es obligatoria");
      return;
    }
    setIsSubmitting(true);
    const data = new FormData();
    data.append("nombre", nombre);
    if (selectedFile) {
      data.append("imagen", selectedFile);
    }
    try {
      if (editingGenre) {
        await updateCategory(editingGenre.id, data);
        toast.success("Género actualizado correctamente");
      } else {
        await createCategory(data);
        toast.success("Género creado correctamente");
      }
      closeDialog();
      loadGenres();
    } catch (error) {
      setError("Error al guardar el género");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (genre: Category) => {
    openDialog(genre);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este género?")) {
      try {
        await deleteCategory(id);
        toast.success("Género eliminado correctamente");
        loadGenres();
      } catch (error) {
        toast.error("Error al eliminar el género");
      }
    }
  };

  // Filtrado y ordenamiento
  const filteredGenres = genres.filter((g) =>
    g.nombre.toLowerCase().includes(search.toLowerCase())
  );
  const sortedGenres = [...filteredGenres].sort((a, b) => {
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
  // Paginación sobre la lista ordenada y filtrada
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentGenres = sortedGenres.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(sortedGenres.length / itemsPerPage);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  // Generar array de páginas para mostrar
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
        <h2 className="text-2xl font-bold">Géneros</h2>
        <Button
          className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
          onClick={() => openDialog()}
        >
          Nuevo Género
        </Button>
      </div>
      {/* Buscador */}
      <div className="flex items-center gap-2 mb-2">
        <Input
          type="text"
          placeholder="Buscar por nombre..."
          value={search}
          onChange={e => {
            setSearch(e.target.value);
            setCurrentPage(1);
          }}
          className="max-w-xs"
        />
      </div>

      {/* Dialog SOLO para el formulario, fuera de la tabla y lista */}
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
              {editingGenre ? "Editar Género" : "Nuevo Género"}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-2">
              <Label htmlFor="nombre">Nombre</Label>
              <Input
                id="nombre"
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
                required
                autoFocus
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="imagen">Imagen</Label>
              <Input
                id="imagen"
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                required={!editingGenre}
              />
            </div>
            {error && (
              <div className="text-red-600 text-sm text-center">{error}</div>
            )}
            <div className="flex gap-2">
              <Button type="submit" className="w-full" disabled={isSubmitting}>
                {isSubmitting
                  ? "Guardando..."
                  : editingGenre
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
                    else { setSortBy("id"); setSortAsc(true); }
                  }}
                >
                  ID {sortBy === "id" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "nombre") setSortAsc((asc) => !asc);
                    else { setSortBy("nombre"); setSortAsc(true); }
                  }}
                >
                  Nombre {sortBy === "nombre" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead>Imagen</TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {currentGenres.map((genre) => (
                <TableRow key={genre.id}>
                  <TableCell>{genre.id}</TableCell>
                  <TableCell>{genre.nombre}</TableCell>
                  <TableCell>
                    <img
                      src={getImageUrl(genre.imagen) + `?t=${new Date().getTime()}`}
                      alt={genre.nombre}
                      className="w-16 h-16 object-cover rounded"
                    />
                  </TableCell>
                  <TableCell>
                    <div className="space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
                        onClick={() => handleEdit(genre)}
                      >
                        Editar
                      </Button>
                      <Button
                        variant="destructive"
                        size="sm"
                        className="bg-red-600 cursor-pointer hover:bg-red-700 text-white"
                        onClick={() => handleDelete(genre.id)}
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
              {Math.min(indexOfLastItem, genres.length)} de {genres.length}{" "}
              géneros
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
}

// Animación fadeIn para el Dialog
if (typeof window !== "undefined") {
  const style = document.createElement("style");
  style.innerHTML = `@keyframes fadeIn { from { opacity: 0; transform: scale(0.95);} to { opacity: 1; transform: scale(1);} } .animate-fadeIn { animation: fadeIn 0.2s ease; }`;
  document.head.appendChild(style);
}
