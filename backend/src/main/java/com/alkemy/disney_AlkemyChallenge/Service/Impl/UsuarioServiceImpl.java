package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.CreateUserDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.UpdatePasswordDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Exception.InvalidPasswordException;
import com.alkemy.disney_AlkemyChallenge.Exception.ResourceNotFoundException;
import com.alkemy.disney_AlkemyChallenge.Mapper.UsuarioMapper;
import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     * @throws ServiceException si ocurre un error al obtener los usuarios
     * **/
    @Override
    public List<UsuarioEntity> findAllUsers() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener los usuarios: " + e.getMessage(), e);
        }
    }

    /** 
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return DTO de usuario
     * @throws IllegalArgumentException si el usuario no existe
     * **/
    @Override
    public UsuarioEntity findUserById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Usuario no encontrado con ID: " + id)
        );
    }

    /**
     * Actualiza la contraseña de un usuario
     * @param id ID del usuario
     * @param updatePasswordDTO DTO con la nueva contraseña
     * @return true si la contraseña se actualizó correctamente, false en caso contrario
     * @throws InvalidPasswordException si la contraseña no cumple con los requisitos de seguridad o las contraseñas no coinciden
     * **/
    @Override
    public void updatePassword(Long id, UpdatePasswordDTO updatePasswordDTO) {
        try {
            if (!isValidPassword(updatePasswordDTO.getNewPassword())) {
                throw new InvalidPasswordException("La contraseña no cumple con los requisitos de seguridad.");
            }
            if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmNewPassword())) {
                throw new InvalidPasswordException("Las contraseñas no coinciden.");
            }

            UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(() ->
                    new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

            if (!passwordEncoder.matches(updatePasswordDTO.getOldPassword(), usuario.getPassword())) {
                throw new InvalidPasswordException("La contraseña actual es incorrecta.");
            }

            usuario.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
            usuarioRepository.save(usuario);
        } catch (InvalidPasswordException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la contraseña del usuario: " + e.getMessage(), e);
        }
    }
    
    /** 
     * Valida la contraseña
     * @param password Contraseña a validar
     * @return true si la contraseña es válida, false en caso contrario
     * **/
    private boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        // Regex para validar la estructura básica
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    /**
     * Crea un nuevo usuario
     * @param usuario Usuario a crear
     * @return Usuario creado
     * @throws IllegalArgumentException si el usuario ya existe
     * **/
    @Override
    public UsuarioEntity createUser(CreateUserDTO usuario) {
        try {
            // Verificar si el usuario ya existe
            if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
                throw new IllegalArgumentException("El nombre de usuario ya existe");
            }
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya existe");
            }

            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }

            if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }

            if (!isValidPassword(usuario.getPassword())) {
                throw new InvalidPasswordException("La contraseña no cumple con los requisitos de seguridad.");
            }

            // Encriptar la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            
            return usuarioRepository.save(usuarioMapper.createUserDTOToUsuarioEntity(usuario));
        } catch (Exception e) {
            throw new ServiceException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario
     * @param usuario Usuario con los datos actualizados
     * @return Usuario actualizado
     * @throws ResourceNotFoundException si el usuario no existe
     * **/
    @Override
    public UsuarioEntity updateUser(Long id, UsuarioEntity usuario) {
        try {
            UsuarioEntity existingUser = usuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

            // Verificar si el username ya existe en otro usuario
            if (!existingUser.getUsername().equals(usuario.getUsername()) &&
                usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
                throw new IllegalArgumentException("El nombre de usuario ya existe");
            }

            // Verificar si el email ya existe en otro usuario
            if (!existingUser.getEmail().equals(usuario.getEmail()) &&
                usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                throw new IllegalArgumentException("El email ya existe");
            }

            // Actualizar campos
            existingUser.setUsername(usuario.getUsername());
            existingUser.setEmail(usuario.getEmail());
            existingUser.setRole(usuario.getRole());

            // Solo actualizar contraseña si se proporciona una nueva
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                if (!isValidPassword(usuario.getPassword())) {
                    throw new InvalidPasswordException("La contraseña no cumple con los requisitos de seguridad.");
                }
                existingUser.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }

            return usuarioRepository.save(existingUser);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un usuario
     * @param id ID del usuario a eliminar
     * @throws ResourceNotFoundException si el usuario no existe
     * **/
    @Override
    public void deleteUser(Long id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
            }
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }
}
