package com.alkemy.disney_AlkemyChallenge.Entity;

import com.alkemy.disney_AlkemyChallenge.Enum.Role;
import com.alkemy.disney_AlkemyChallenge.Validation.StrongEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
@Schema(description = "Entidad que representa un usuario en el sistema")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de usuario", example = "john_doe", required = true)
    private String username;

    @NotBlank(message = "El email no puede estar en blanco")
    @StrongEmail
    @Column(nullable = false, unique = true)
    @Schema(description = "Email del usuario", example = "john.doe@example.com", required = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(description = "Rol del usuario", example = "USER", required = true)
    private Role role = Role.USER;

    @NotBlank(message = "La contraseña no puede estar en blanco")
    @JsonIgnore
    @Size(min = 8, max = 16, message = "La contraseña debe tener entre 8 y 16 caracteres")
    @Column(nullable = false)
    @Schema(description = "Contraseña del usuario", example = "Password123$", required = true)
    private String password;

    @Column(name = "refresh_token")
    @Schema(description = "Token de refresco del usuario", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String refreshToken;

    public UsuarioEntity(String username, String email, Role role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }
}
