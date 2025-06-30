package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.EmailDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.RegisterDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Exception.EmailAlreadyRegisteredException;
import com.alkemy.disney_AlkemyChallenge.Exception.InvalidPasswordException;
import com.alkemy.disney_AlkemyChallenge.Mapper.UsuarioMapper;
import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import com.alkemy.disney_AlkemyChallenge.Service.IEmailService;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.security.SecureRandom;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final IJWTUtilityService jwtUtilityService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;
    private final UsuarioMapper usuarioMapper;

    /** 
     * Inicia sesión
     * @param loginDTO DTO de login
     * @return HashMap con JWT o error
     * @throws Exception si ocurre un error al iniciar sesión
     * **/
    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String, String> loginResponse = new HashMap<>();
            Optional<UsuarioEntity> usuario = usuarioRepository.findByEmail(loginDTO.getEmail());

            if (usuario.isEmpty()) {
                loginResponse.put("Error", "Email no registrado");
                return loginResponse;
            }

            // Verificar la contraseña
            if (verifyPassword(loginDTO.getPassword(), usuario.get().getPassword())) {
                String jwt = jwtUtilityService.generateJWT(usuario.get().getId(), usuario.get().getRole().name(), usuario.get().getUsername(), usuario.get().getEmail());
                String refreshToken = generateRefreshToken();
                usuario.get().setRefreshToken(refreshToken);
                usuarioRepository.save(usuario.get());
                loginResponse.put("JWT", jwt);
                loginResponse.put("refreshToken", refreshToken); // Solo para el controlador, no enviar al frontend
            } else {
                loginResponse.put("Error", "Contraseña incorrecta");
            }

            loginResponse.put("id", String.valueOf(usuario.get().getId()));
            loginResponse.put("username", usuario.get().getUsername());
            loginResponse.put("email", usuario.get().getEmail());
            loginResponse.put("role", usuario.get().getRole().toString());

            return loginResponse;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    /** 
     * Registra un nuevo usuario
     * @param usuario DTO de registro
     * @return ResponseDTO con mensaje de éxito
     * @throws MessagingException si ocurre un error al enviar el email de bienvenida
     * @throws IllegalArgumentException si el nombre de usuario o email están vacíos
     * @throws EmailAlreadyRegisteredException si el email ya está registrado
     * @throws InvalidPasswordException si la contraseña no es válida
     * **/
    @Transactional
    public ResponseDTO register(RegisterDTO usuario) throws MessagingException {
        if (!isValidPassword(usuario.getPassword())) {
            throw new InvalidPasswordException("Contraseña no válida. El formato de la contraseña debe ser minimo una mayuscula, " +
                    "una minuscula, un numero, un caracter especial, minimo 8 y maximo 16 caracteres");
        }

        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        } else if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está registrado");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        } else if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailAlreadyRegisteredException("El email ya está registrado");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuarioMapper.registerDTOToUsuarioEntity(usuario));

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Usuario registrado");

        String subject = "¡Bienvenido a Disney!";
        String body = "<h1>¡Hola, " + usuario.getUsername() + "!</h1>" +
                "<p>Te damos la bienvenida a nuestra comunidad de Disney. Estamos emocionados de tenerte con nosotros.</p>" +
                "<p>Explora un mundo mágico lleno de aventuras y diversión. No dudes en ponerte en contacto con nosotros si tienes alguna pregunta.</p>" +
                "<p>¡Esperamos que disfrutes tu experiencia!</p>" +
                "<br><img src='cid:disneyImage' alt='Disney Logo'>" +
                "<p>Saludos cordiales,<br>El equipo de Disney</p>";

        sendWelcomeEmail(new EmailDTO(usuario.getEmail(), subject, body),
                "src/main/resources/static/images/mail/disney.jpg");

        return responseDTO;
    }

    /** 
     * Envia un email de bienvenida
     * @param emailDTO DTO de email
     * @param path Ruta de la imagen del email
     * @throws MessagingException si ocurre un error al enviar el email
     * **/
    @Async
    public void sendWelcomeEmail(EmailDTO emailDTO, String path) throws MessagingException {
        emailService.sendEmail(emailDTO, path);
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
     * Verifica la contraseña
     * @param enteredPassword Contraseña ingresada
     * @param StoredPassword Contraseña almacenada
     * @return true si la contraseña es válida, false en caso contrario
     * **/
    private boolean verifyPassword(String enteredPassword, String StoredPassword) {
        return passwordEncoder.matches(enteredPassword, StoredPassword);
    }

    // Generar un refreshToken aleatorio seguro
    private String generateRefreshToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    // Validar refreshToken y generar nuevo JWT
    public String refreshAccessToken(String refreshToken) throws Exception {
        Optional<UsuarioEntity> usuario = usuarioRepository.findAll().stream()
            .filter(u -> refreshToken.equals(u.getRefreshToken()))
            .findFirst();
        if (usuario.isEmpty()) {
            throw new Exception("Refresh token inválido");
        }
        return jwtUtilityService.generateJWT(usuario.get().getId(), usuario.get().getRole().name(), usuario.get().getUsername(), usuario.get().getEmail());
    }

    // Borrar refreshToken (logout)
    public void clearRefreshToken(String refreshToken) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findAll().stream()
            .filter(u -> refreshToken.equals(u.getRefreshToken()))
            .findFirst();
        usuario.ifPresent(u -> {
            u.setRefreshToken(null);
            usuarioRepository.save(u);
        });
    }
}
