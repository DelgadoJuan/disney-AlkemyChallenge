package com.alkemy.disney_AlkemyChallenge.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

public interface IJWTUtilityService {
    String generateJWT(Long userId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;
    JWTClaimsSet parseJWT(String jwt) throws ParseException, JOSEException, IOException, NoSuchAlgorithmException, InvalidKeySpecException;
}
