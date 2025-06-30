package com.alkemy.disney_AlkemyChallenge.DTO.Usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDTO {
    @NotBlank(message = "La contraseña antigua no puede estar en blanco")
    private String oldPassword;
    @NotBlank(message = "La nueva contraseña no puede estar en blanco")
    private String newPassword;
    @NotBlank(message = "La confirmación de la nueva contraseña no puede estar en blanco")
    private String confirmNewPassword;
}
