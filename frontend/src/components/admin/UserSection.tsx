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
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
} from "@/services/userService";
import { type Usuario, type RegisterData } from "@/types/UsuarioTypes";
import { toast } from "sonner";

export function UserSection() {
  const [users, setUsers] = useState<Usuario[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingUser, setEditingUser] = useState<Usuario | null>(null);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState<string>("USER");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");

  // Estados para paginación
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  const [search, setSearch] = useState("");
  const [sortBy, setSortBy] = useState<'id' | 'username' | 'email' | 'role'>("username");
  const [sortAsc, setSortAsc] = useState(true); // true = ascendente, false = descendente

  const loadUsers = async () => {
    try {
      const data = await getUsers();
      setUsers(data);
    } catch (error) {
      toast.error("Error al cargar los usuarios");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  // Cuando abro el modal para editar o crear, seteo los datos
  const openDialog = (user?: Usuario) => {
    setEditingUser(user || null);
    setUsername(user ? user.username : "");
    setEmail(user ? user.email : "");
    setPassword("");
    setRole(user ? user.role : "USER");
    setError("");
    setIsDialogOpen(true);
  };

  // Cuando cierro el modal, limpio todo
  const closeDialog = () => {
    setIsDialogOpen(false);
    setEditingUser(null);
    setUsername("");
    setEmail("");
    setPassword("");
    setRole("USER");
    setError("");
    setIsSubmitting(false);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    
    if (!username.trim()) {
      setError("El nombre de usuario es obligatorio");
      return;
    }
    if (!email.trim()) {
      setError("El email es obligatorio");
      return;
    }
    if (!editingUser && !password.trim()) {
      setError("La contraseña es obligatoria");
      return;
    }

    setIsSubmitting(true);
    
    try {
      const userData: RegisterData = {
        username: username.trim(),
        email: email.trim(),
        password: password || "tempPassword123!", // Contraseña temporal para edición
        confirmPassword: confirmPassword,
        role: role,
      };

      if (editingUser) {
        await updateUser(editingUser.id, userData);
        toast.success("Usuario actualizado correctamente");
      } else {
        await createUser(userData);
        toast.success("Usuario creado correctamente");
      }
      closeDialog();
      loadUsers();
    } catch (error: any) {
      if (error.response?.data?.Error) {
        setError(error.response.data.Error);
      } else if (error.response?.data?.message) {
        setError(error.response.data.message);
      } else {
        setError("Error al guardar el usuario");
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (user: Usuario) => {
    openDialog(user);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm("¿Estás seguro de que deseas eliminar este usuario?")) {
      try {
        await deleteUser(id);
        toast.success("Usuario eliminado correctamente");
        loadUsers();
      } catch (error) {
        toast.error("Error al eliminar el usuario");
      }
    }
  };

  // Filtrado y ordenamiento
  const filteredUsers = users.filter((u) =>
    u.username.toLowerCase().includes(search.toLowerCase()) ||
    u.email.toLowerCase().includes(search.toLowerCase())
  );
  
  const sortedUsers = [...filteredUsers].sort((a, b) => {
    let aValue: string | number;
    let bValue: string | number;
    
    switch (sortBy) {
      case "id":
        aValue = a.id;
        bValue = b.id;
        break;
      case "username":
        aValue = a.username.toLowerCase();
        bValue = b.username.toLowerCase();
        break;
      case "email":
        aValue = a.email.toLowerCase();
        bValue = b.email.toLowerCase();
        break;
      case "role":
        aValue = a.role.toLowerCase();
        bValue = b.role.toLowerCase();
        break;
      default:
        aValue = a.username.toLowerCase();
        bValue = b.username.toLowerCase();
    }

    if (aValue < bValue) return sortAsc ? -1 : 1;
    if (aValue > bValue) return sortAsc ? 1 : -1;
    return 0;
  });

  // Paginación sobre la lista ordenada y filtrada
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentUsers = sortedUsers.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(sortedUsers.length / itemsPerPage);

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
        <h2 className="text-2xl font-bold">Usuarios</h2>
        <Button
          className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
          onClick={() => openDialog()}
        >
          Nuevo Usuario
        </Button>
      </div>
      
      {/* Buscador */}
      <div className="flex items-center gap-2 mb-2">
        <Input
          type="text"
          placeholder="Buscar por username o email..."
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
          className="max-w-md w-full bg-white rounded-2xl shadow-2xl border border-gray-200 p-8 animate-fadeIn"
          style={{ zIndex: 50 }}
        >
          <DialogHeader>
            <DialogTitle className="text-center text-2xl font-semibold mb-2">
              {editingUser ? "Editar Usuario" : "Nuevo Usuario"}
            </DialogTitle>
          </DialogHeader>
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-2">
              <Label htmlFor="username">Nombre de Usuario</Label>
              <Input
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                autoFocus
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">
                Contraseña {editingUser && "(dejar vacío para mantener la actual)"}
              </Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required={!editingUser}
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="role">Requisitos de contraseña</Label>
              <p className="text-sm text-gray-600 dark:text-gray-400">
                La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial (@#$%^&+=).
              </p>
            </div>
            <div className="space-y-2">
              <Label htmlFor="role">Confirmar contraseña</Label>
              <Input
                id="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="role">Rol</Label>
              <Select value={role} onValueChange={setRole}>
                <SelectTrigger>
                  <SelectValue placeholder="Seleccionar rol" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="USER">Usuario</SelectItem>
                  <SelectItem value="ADMIN">Administrador</SelectItem>
                </SelectContent>
              </Select>
            </div>
            {error && (
              <div className="text-red-600 text-sm text-center">{error}</div>
            )}
            <div className="flex gap-2">
              <Button type="submit" className="w-full bg-blue-600 cursor-pointer hover:bg-blue-700 text-white" disabled={isSubmitting}>
                {isSubmitting
                  ? "Guardando..."
                  : editingUser
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
                    if (sortBy === "username") setSortAsc((asc) => !asc);
                    else { setSortBy("username"); setSortAsc(true); }
                  }}
                >
                  Username {sortBy === "username" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "email") setSortAsc((asc) => !asc);
                    else { setSortBy("email"); setSortAsc(true); }
                  }}
                >
                  Email {sortBy === "email" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead
                  className="cursor-pointer select-none"
                  onClick={() => {
                    if (sortBy === "role") setSortAsc((asc) => !asc);
                    else { setSortBy("role"); setSortAsc(true); }
                  }}
                >
                  Rol {sortBy === "role" && (sortAsc ? "▲" : "▼")}
                </TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {currentUsers.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell>
                    <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                      user.role === 'ADMIN' 
                        ? 'bg-red-100 text-red-800' 
                        : 'bg-blue-100 text-blue-800'
                    }`}>
                      {user.role}
                    </span>
                  </TableCell>
                  <TableCell>
                    <div className="space-x-2">
                      <Button
                        variant="outline"
                        size="sm"
                        className="bg-blue-600 cursor-pointer hover:bg-blue-700 text-white"
                        onClick={() => handleEdit(user)}
                      >
                        Editar
                      </Button>
                      <Button
                        variant="destructive"
                        size="sm"
                        className="bg-red-600 cursor-pointer hover:bg-red-700 text-white"
                        onClick={() => handleDelete(user.id)}
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
              {Math.min(indexOfLastItem, users.length)} de {users.length}{" "}
              usuarios
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
