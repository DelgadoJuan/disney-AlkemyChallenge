package com.alkemy.disney_AlkemyChallenge.DTO.Usuario;

import com.alkemy.disney_AlkemyChallenge.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private Role role;
}
