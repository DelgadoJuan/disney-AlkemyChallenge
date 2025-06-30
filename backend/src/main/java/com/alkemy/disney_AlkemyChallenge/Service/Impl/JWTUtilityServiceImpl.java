package com.alkemy.disney_AlkemyChallenge.Service.Impl;

import com.alkemy.disney_AlkemyChallenge.Exception.TokenExpiredException;
import com.alkemy.disney_AlkemyChallenge.Service.IJWTUtilityService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTUtilityServiceImpl implements IJWTUtilityService {

    @Value("classpath:jwtKeys/private_key.pem")
    private Resource privateKeyResource;
    @Value("classpath:jwtKeys/public_key.pem")
    private Resource publicKeyResource;

    /** 
     * Genera un token JWT
     * @param userId ID del usuario
     * @param role Rol del usuario
     * @param username Nombre de usuario
     * @param email Email del usuario
     * @return JWT generado
     * @throws IOException si ocurre un error al leer el archivo de la clave privada
     * @throws NoSuchAlgorithmException si ocurre un error al generar la clave
     * @throws InvalidKeySpecException si ocurre un error al generar la clave
     * @throws JOSEException si ocurre un error al generar el JWT
     * **/
    @Override
    public String generateJWT(Long userId, String role, String username, String email) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, JOSEException {
        PrivateKey privateKey = loadPrivateKey(privateKeyResource);
        JWSSigner signer = new RSASSASigner(privateKey);
        Date now = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + 900000)) // 15 minutos
                .claim("type", "access")
                .claim("role", role)
                .claim("username", username)
                .claim("email", email)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    /** 
     * Parsea un JWT
     * @param jwt JWT a parsear
     * @return ClaimsSet del JWT
     * @throws ParseException si ocurre un error al parsear el JWT
     * @throws JOSEException si ocurre un error al verificar el JWT
     * @throws IOException si ocurre un error al leer el archivo de la clave pública
     * @throws NoSuchAlgorithmException si ocurre un error al generar la clave
     * @throws InvalidKeySpecException si ocurre un error al generar la clave
     * **/
    @Override
    public JWTClaimsSet parseJWT(String jwt) throws ParseException, TokenExpiredException, JOSEException, IOException,
            NoSuchAlgorithmException, InvalidKeySpecException {
        PublicKey publicKey = loadPublicKey(publicKeyResource);
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid signature");
        }

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

        if (claimsSet.getExpirationTime().before(new Date())) {
            throw new TokenExpiredException("Token has expired");
        }

        // Verificar que sea un access token
        String tokenType = (String) claimsSet.getClaim("type");
        if (!"access".equals(tokenType)) {
            throw new JOSEException("Invalid token type");
        }

        return claimsSet;
    }

    /** 
     * Carga una clave privada
     * @param resource Recurso de la clave privada
     * @return Clave privada
     * @throws IOException si ocurre un error al leer el archivo de la clave privada
     * @throws NoSuchAlgorithmException si ocurre un error al generar la clave
     * @throws InvalidKeySpecException si ocurre un error al generar la clave
     * **/
    private PrivateKey loadPrivateKey(Resource resource) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] keyBites = Files.readAllBytes(Paths.get(resource.getURI()));
        String privateKeyPEM = new String(keyBites, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    };

    /** 
     * Carga una clave pública
     * @param resource Recurso de la clave pública
     * @return Clave pública
     * @throws IOException si ocurre un error al leer el archivo de la clave pública
     * @throws NoSuchAlgorithmException si ocurre un error al generar la clave
     * @throws InvalidKeySpecException si ocurre un error al generar la clave
     * **/
    private PublicKey loadPublicKey(Resource resource) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        byte[] keyBites = Files.readAllBytes(Paths.get(resource.getURI()));
        String publicKeyPEM = new String(keyBites, StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
    }
}
