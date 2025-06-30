package com.alkemy.disney_AlkemyChallenge.Controller;

import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.LoginDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.ResponseDTO;
import com.alkemy.disney_AlkemyChallenge.DTO.Usuario.RegisterDTO;
import com.alkemy.disney_AlkemyChallenge.Service.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alkemy.disney_AlkemyChallenge.Exception.InvalidPasswordException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    final IAuthService authService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en la aplicación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "409", description = "Usuario ya registrado"),
        @ApiResponse(responseCode = "400", description = "Contraseña inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterDTO usuario) throws Exception {
        try {
            return new ResponseEntity<>(authService.register(usuario), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO(1, e.getMessage()));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(1, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(1, e.getMessage()));
        }
    }

    @Operation(summary = "Iniciar sesión", description = "Inicia sesión en la aplicación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    private ResponseEntity<HashMap<String, String>> login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) throws Exception {
        HashMap<String, String> loginResponse = authService.login(loginDTO);
        if (loginResponse.containsKey("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        }
        // Setear refreshToken en cookie httpOnly
        String refreshToken = loginResponse.remove("refreshToken");
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false); // Cambia a true si usas HTTPS
        refreshCookie.setPath("/");
        refreshCookie.setDomain("localhost");
        refreshCookie.setMaxAge(30 * 24 * 60 * 60); // 30 días
        response.addCookie(refreshCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<HashMap<String, String>> refreshToken(HttpServletRequest request) {
        try {
            String refreshToken = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("refreshToken".equals(cookie.getName())) {
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }
            if (refreshToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String newJwt = ((com.alkemy.disney_AlkemyChallenge.Service.Impl.AuthServiceImpl)authService).refreshAccessToken(refreshToken);
            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put("JWT", newJwt);
            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken != null) {
            ((com.alkemy.disney_AlkemyChallenge.Service.Impl.AuthServiceImpl)authService).clearRefreshToken(refreshToken);
        }
        // Borrar cookie
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
