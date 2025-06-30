import { Routes, Route } from "react-router-dom";
import "./index.css";
import Home from "./pages/Home";
import Navbar from "./components/Navbar";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { UsuarioProvider } from "./context/UsuarioContext";
import Categories from "./pages/Categories";
import Movies from "./pages/Movies";
import MovieDetail from "./pages/MovieDetail";
import CharacterDetail from "./pages/CharacterDetail";
import { Profile } from "./pages/Profile";
import Admin from "./pages/Admin";
import AdminUsers from "./pages/admin/AdminUsers";
import AdminMovies from "./pages/admin/AdminMovies";
import AdminCharacters from "./pages/admin/AdminCharacters";
import AdminGenres from "./pages/admin/AdminGenres";
import { AdminMovieCharacters } from "./pages/admin/AdminMovieCharacters";
import { Toaster } from "sonner";
import Footer from "./components/Footer";

function App() {
  return (
    <UsuarioProvider>
      <div className="min-h-screen bg-gradient-to-b from-blue-50 to-purple-50">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/auth/login" element={<Login />} />
          <Route path="/auth/register" element={<Register />} />
          <Route path="/movies" element={<Movies />} />
          <Route path="/movie/:id" element={<MovieDetail />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/characters/:id" element={<CharacterDetail />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<Admin />} />
          <Route path="/admin/users" element={<AdminUsers />} />
          <Route path="/admin/movies" element={<AdminMovies />} />
          <Route path="/admin/movies/:id/characters" element={<AdminMovieCharacters />} />
          <Route path="/admin/characters" element={<AdminCharacters />} />
          <Route path="/admin/genres" element={<AdminGenres />} />
        </Routes>
        <Toaster />
        <Footer />
      </div>
    </UsuarioProvider>
  );
}

export default App;
