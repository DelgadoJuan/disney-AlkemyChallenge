package com.alkemy.disney_AlkemyChallenge.DTO.Usuario;

import com.alkemy.disney_AlkemyChallenge.Validation.StrongEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El email no puede estar en blanco")
    @StrongEmail
    private String email;

    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 8, max = 16, message = "La contraseña debe tener entre 8 y 16 caracteres")
    private String password;
}
