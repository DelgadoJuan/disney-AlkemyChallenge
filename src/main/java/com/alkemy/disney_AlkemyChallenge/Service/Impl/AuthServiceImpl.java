package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.DTO.EmailDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.Entity.UsuarioEntity;
import com.alkemy.disney_AlkemyChallenge.Exception.EmailAlreadyRegisteredException;
import com.alkemy.disney_AlkemyChallenge.Exception.InvalidPasswordException;
import com.alkemy.disney_AlkemyChallenge.Repository.UsuarioRepository;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import com.alkemy.disney_AlkemyChallenge.Service.IEmailService;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {
    private final UsuarioRepository UsuarioRepository;
    private final IJWTUtilityService jwtUtilityService;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;
    private final IEmailService emailService;

    @Autowired
    public AuthServiceImpl(UsuarioRepository UsuarioRepository, IJWTUtilityService jwtUtilityService, UsuarioRepository usuarioRepository, IEmailService emailService) {
        this.UsuarioRepository = UsuarioRepository;
        this.jwtUtilityService = jwtUtilityService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<UsuarioEntity> usuario = UsuarioRepository.findByEmail(loginDTO.getEmail());

            if (usuario.isEmpty()) {
                jwt.put("Error", "Usuario no registrado");
                return jwt;
            }

            // Verificar la contraseña
            if (verifyPassword(loginDTO.getPassword(), usuario.get().getPassword())) {
                jwt.put("JWT", jwtUtilityService.generateJWT(usuario.get().getId()));
            } else {
                jwt.put("Error", "Autenticación fallida");
            }

            return jwt;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    public ResponseDTO register(UsuarioEntity usuario) throws MessagingException {
        List<UsuarioEntity> getAllUsers = usuarioRepository.findAll();
        for (UsuarioEntity usuarioEntity : getAllUsers) {
            if (usuarioEntity.getEmail().equals(usuario.getEmail())) {
                throw new EmailAlreadyRegisteredException(usuario.getEmail() + " ya está registrado");
            }
        }

        if (!isValidPassword(usuario.getPassword())) {
            throw new InvalidPasswordException("Contraseña no válida. El formato de la contraseña debe ser minimo una mayuscula, " +
                    "una minuscula, un numero, un caracter especial, minimo 8 y maximo 16 caracteres, no puede haber " +
                    "más de dos números iguales consecutivos");
        }

        usuario.setPassword(encoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Usuario registrado");

        String subject = "¡Bienvenido a Disney!";
        String body = "<h1>¡Hola, " + usuario.getUsername() + "!</h1>" +
                "<p>Te damos la bienvenida a nuestra comunidad de Disney. Estamos emocionados de tenerte con nosotros.</p>" +
                "<p>Explora un mundo mágico lleno de aventuras y diversión. No dudes en ponerte en contacto con nosotros si tienes alguna pregunta.</p>" +
                "<p>¡Esperamos que disfrutes tu experiencia!</p>" +
                "<br><img src='cid:disneyImage' alt='Disney Logo'>" +
                "<p>Saludos cordiales,<br>El equipo de Disney</p>";

        emailService.sendEmail(new EmailDTO(usuario.getEmail(), subject, body),
                "src/main/resources/static/mail/disney.jpg");

        return responseDTO;
    }

    private boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        // Regex para validar la estructura básica
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    private boolean verifyPassword(String enteredPassword, String StoredPassword) {
        return encoder.matches(enteredPassword, StoredPassword);
    }
}
