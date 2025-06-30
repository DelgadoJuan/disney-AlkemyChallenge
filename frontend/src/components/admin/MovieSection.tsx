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
  DialogTitle
} from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  getAudiovisualsForAdmin,
  createAudiovisual,
  updateAudiovisual,
  deleteAudiovisual,
} from "@/services/movieService";
import { getCategories } from "@/services/categoryService";
import { type Movie, type CreateAudiovisualData } from "@/types/MovieTypes";
import { type Category } from "@/types/CategoryTypes";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import { getImageUrl } from "@/lib/utils";

export const MovieSection = () => {
  const navigate = useNavigate();
  const [audiovisuals, setAudiovisuals] = useState<Movie[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingAudiovisual, setEditingAudiovisual] = useState<Movie | null>(null);
  
  // Estados del formulario
  const [titulo, setTitulo] = useState("");
  const [descripcion, setDescripcion] = useState("");
  const [director, setDirector] = useState("");
  const [duracion, setDuracion] = useState("");
  const [calificacion, setCalificacion] = useState("");
  const [generoId, setGeneroId] = useState("");
  const [fechaCreacion, setFechaCreacion] = useState("");
  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Estados para paginación
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<'id' | 'titulo' | 'duracion' | 'calificacion' | 'fechaCreacion'>("titulo");
  const [sortAsc, setSortAsc] = useState(true);

  const loadAudiovisuals = async () => {
    try {
      const data = await getAudiovisualsForAdmin();
      setAudiovisuals(data);
    } catch (error) {
      toast.error("Error al cargar los audiovisuales");
    } finally {
      setIsLoading(false);
    }
  };

  const loadCategories = async () => {
    try {
      const data = await getCategories();
      setCategories(data);
    } catch (error) {
      toast.error("Error al cargar los géneros");
    }
  };

  useEffect(() => {
    loadAudiovisuals();
    loadCategories();
  }, []);

  // Efecto para actualizar el género cuando se cargan las categorías y hay un audiovisual en edición
  useEffect(() => {
    if (editingAudiovisual && categories.length > 0) {
      const genero = categories.find(cat => cat.nombre === editingAudiovisual.nombreGenero);
      setGeneroId(genero ? genero.id.toString() : "");
    }
  }, [categories, editingAudiovisual]);

  const openDialog = (audiovisual?: Movie) => {
    setEditingAudiovisual(audiovisual || null);
    setTitulo(audiovisual ? audiovisual.titulo : "");
    setDescripcion(audiovisual ? audiovisual.descripcion : "");
    setDirector(audiovisual ? audiovisual.director : "");
    setDuracion(audiovisual ? audiovisual.duracion.toString() : "");
    setCalificacion(audiovisual ? audiovisual.calificacion.toString() : "");
    // El género se establecerá en el useEffect cuando las categorías se carguen
    setGeneroId("");
    setFechaCreacion(audiovisual ? audiovisual.fechaCreacion : "");
    setFile(null);
    setError("");
    setIsDialogOpen(true);
  };

  const closeDialog = () => {
    setIsDialogOpen(false);
    setEditingAudiovisual(null);
    setTitulo("");
    setDescripcion("");
    setDirector("");
    setDuracion("");
    setCalificacion("");
    setGeneroId("");
    setFechaCreacion("");
    setFile(null);
    setError("");
    setIsSubmitting(false);
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    
    if (!titulo.trim()) {
      setError("El título es obligatorio");
      return;
    }
    if (!descripcion.trim()) {
      setError("La descripción es obligatoria");
      return;
    }
    if (!director.trim()) {
      setError("El director es obligatorio");
      return;
    }
    if (!duracion || parseInt(duracion) <= 0) {
      setError("La duración debe ser mayor a 0");
      return;
    }
    if (!calificacion || parseFloat(calificacion) < 0 || parseFloat(calificacion) > 5) {
      setError("La calificación debe estar entre 0 y 5");
      return;
    }
    if (!generoId) {
      setError("Debe seleccionar un género");
      return;
    }
    if (!editingAudiovisual && !file) {
      setError("La imagen es obligatoria");
      return;
    }

    setIsSubmitting(true);
    
    const data: CreateAudiovisualData = {
      titulo: titulo.trim(),
      descripcion: descripcion.trim(),
      director: director.trim(),
      duracion: parseInt(duracion),
      calificacion: parseFloat(calificacion),
      generoId: parseInt(generoId),
      fechaCreacion: fechaCreacion || undefined,
      imagen: file!
    };

    try {
      if (editingAudiovisual) {
        await updateAudiovisual(editingAudiovisual.id, data);
        toast.success("Audiovisual actualizado correctamente");
      } else {
        await createAudiovisual(data);
        toast.success("Audiovisual creado correctamente");
      }
      closeDialog();
      loadAudiovisuals();
    } catch (error) {
      setError("Error al guardar el audiovisual");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (audiovisual: Movie) => {
    openDialog(audiovisual);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este audiovisual?")) {
      try {
        await deleteAudiovisual(id);
        toast.success("Audiovisual eliminado correctamente");
        loadAudiovisuals();
      } catch (error) {
        toast.error("Error al eliminar el audiovisual");
      }
    }
  };

  const handleManageCharacters = (audiovisual: Movie) => {
    navigate(`/admin/movies/${audiovisual.id}/characters`);
  };

  // Filtrado y ordenamiento
  const filteredAudiovisuals = audiovisuals.filter((a) =>
    a.titulo.toLowerCase().includes(search.toLowerCase()) ||
    a.nombreGenero.toLowerCase().includes(search.toLowerCase())
  );

  const sortedAudiovisuals = [...filteredAudiovisuals].sort((a, b) => {
    let comparison = 0;
    
    switch (sortBy) {
      case "titulo":
        comparison = a.titulo.toLowerCase().localeCompare(b.titulo.toLowerCase());
        break;
      case "duracion":
        comparison = a.duracion - b.duracion;
        break;
      case "calificacion":
        comparison = a.calificacion - b.calificacion;
        break;
      case "fechaCreacion":
        comparison = new Date(a.fechaCreacion).getTime() - new Date(b.fechaCreacion).getTime();
        break;
      default:
        comparison = a.id - b.id;
    }
    
    return sortAsc ? comparison : -comparison;
  });

  // Paginación
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentAudiovisuals = sortedAudiovisuals.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(sortedAudiovisuals.length / itemsPerPage);

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
        <h2 className="text-2xl font-bold">Audiovisuales</h2>
        <Button
          className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
          onClick={() => openDialog()}
        >
          Nuevo Audiovisual
        </Button>
      </div>

      {/* Buscador */}
      <div className="flex items-center gap-2 mb-2">
        <Input
          type="text"
          placeholder="Buscar por título o género..."
          value={search}
          onChange={e => {
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
          className="max-w-2xl w-full bg-white rounded-2xl shadow-2xl border border-gray-200 p-8 animate-fadeIn"
          style={{ zIndex: 50 }}
        >
          <DialogHeader>
            <DialogTitle className="text-center text-2xl font-semibold mb-2">
              {editingAudiovisual ? "Editar Audiovisual" : "Nuevo Audiovisual"}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="titulo">Título *</Label>
                <Input
                  id="titulo"
                  value={titulo}
                  onChange={(e) => setTitulo(e.target.value)}
                  required
                  autoFocus
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="director">Director *</Label>
                <Input
                  id="director"
                  value={director}
                  onChange={(e) => setDirector(e.target.value)}
                  required
                />
              </div>
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="descripcion">Descripción *</Label>
              <Textarea
                id="descripcion"
                value={descripcion}
                onChange={(e) => setDescripcion(e.target.value)}
                required
                rows={3}
              />
            </div>

            <div className="grid grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="duracion">Duración (min) *</Label>
                <Input
                  id="duracion"
                  type="number"
                  min="1"
                  value={duracion}
                  onChange={(e) => setDuracion(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="calificacion">Calificación (0-5) *</Label>
                <Input
                  id="calificacion"
                  type="number"
                  min="0"
                  max="5"
                  step="0.1"
                  value={calificacion}
                  onChange={(e) => setCalificacion(e.target.value)}
                  required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="fechaCreacion">Fecha de Creación</Label>
                <Input
                  id="fechaCreacion"
                  type="date"
                  value={fechaCreacion}
                  onChange={(e) => setFechaCreacion(e.target.value)}
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="genero">Género *</Label>
              <Select value={generoId} onValueChange={setGeneroId} required>
                <SelectTrigger>
                  <SelectValue placeholder="Seleccionar género" />
                </SelectTrigger>
                <SelectContent>
                  {categories.map((category) => (
                    <SelectItem key={category.id} value={category.id.toString()}>
                      {category.nombre}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label htmlFor="imagen">Imagen {!editingAudiovisual && "*"}</Label>
              <Input
                id="imagen"
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                required={!editingAudiovisual}
              />
            </div>

            {error && (
              <div className="text-red-600 text-sm text-center">{error}</div>
            )}

            <div className="flex gap-2">
              <Button type="submit" className="w-full" disabled={isSubmitting}>
                {isSubmitting
                  ? "Guardando..."
                  : editingAudiovisual
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
                <TableHead>Imagen</TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "titulo") setSortAsc((asc) => !asc);
                    else { setSortBy("titulo"); setSortAsc(true); }
                  }}
                >
                  Título {sortBy === "titulo" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead>Director</TableHead>
                <TableHead>Descripción</TableHead>
                <TableHead>Género</TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "duracion") setSortAsc((asc) => !asc);
                    else { setSortBy("duracion"); setSortAsc(true); }
                  }}
                >
                  Duración {sortBy === "duracion" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "calificacion") setSortAsc((asc) => !asc);
                    else { setSortBy("calificacion"); setSortAsc(true); }
                  }}
                >
                  Calificación {sortBy === "calificacion" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "fechaCreacion") setSortAsc((asc) => !asc);
                    else { setSortBy("fechaCreacion"); setSortAsc(true); }
                  }}
                >
                  Fecha {sortBy === "fechaCreacion" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {currentAudiovisuals.map((audiovisual) => (
                <TableRow key={audiovisual.id}>
                  <TableCell>{audiovisual.id}</TableCell>
                  <TableCell>
                    <img
                      src={getImageUrl(audiovisual.imagen) + `?t=${new Date().getTime()}`}
                      alt={audiovisual.titulo}
                      className="w-16 h-16 object-cover rounded"
                    />
                  </TableCell>
                  <TableCell>{audiovisual.titulo}</TableCell>
                  <TableCell>{audiovisual.director}</TableCell>
                  <TableCell className="max-w-xs truncate" title={audiovisual.descripcion}>
                    {audiovisual.descripcion}
                  </TableCell>
                  <TableCell>{audiovisual.nombreGenero}</TableCell>
                  <TableCell>{audiovisual.duracion} min</TableCell>
                  <TableCell>{audiovisual.calificacion}/5</TableCell>
                  <TableCell>
                    {new Date(audiovisual.fechaCreacion).toLocaleDateString()}
                  </TableCell>
                  <TableCell>
                    <div className="space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        className="bg-green-600 cursor-pointer hover:bg-green-700 text-white"
                        onClick={() => handleManageCharacters(audiovisual)}
                      >
                        Personajes
                      </Button>
                      <Button
                        variant="outline"
                        size="sm"
                        className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
                        onClick={() => handleEdit(audiovisual)}
                      >
                        Editar
                      </Button>
                      <Button
                        variant="destructive"
                        size="sm"
                        className="bg-red-600 cursor-pointer hover:bg-red-700 text-white"
                        onClick={() => handleDelete(audiovisual.id)}
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
              {Math.min(indexOfLastItem, audiovisuals.length)} de {audiovisuals.length}{" "}
              audiovisuales
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
