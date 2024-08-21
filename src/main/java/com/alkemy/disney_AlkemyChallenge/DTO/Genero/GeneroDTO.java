package com.alkemy.disney_AlkemyChallenge.DTO.Genero;

import org.springframework.web.multipart.MultipartFile;

public class GeneroDTO {
    private String nombre;
    private MultipartFile imagen;

    public GeneroDTO() {
    }

    public GeneroDTO(String nombre, MultipartFile imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public MultipartFile getImagen() {
        return imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setImagen(MultipartFile imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "GeneroDTO{" +
                "nombre='" + nombre + '\'' +
                ", imagen=" + imagen +
                '}';
    }
}
