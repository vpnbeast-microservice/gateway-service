package com.vpnbeast.gatewayservice.service.impl;

import com.vpnbeast.gatewayservice.client.AuthServiceClient;
import com.vpnbeast.gatewayservice.configuration.AuthenticationProperties;
import com.vpnbeast.gatewayservice.model.UserRequest;
import com.vpnbeast.gatewayservice.model.UserResponse;
import com.vpnbeast.gatewayservice.service.JwtService;
import com.vpnbeast.gatewayservice.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final AuthenticationProperties authenticationProperties;
    private final AuthServiceClient authServiceClient;

    @Override
    public ArrayList getRolesFromToken(String token) {
        Claims jwsMap = Jwts.parser().setSigningKey(getPublicKey(authenticationProperties.getPublicKeyString()))
                .parseClaimsJws(token)
                .getBody();
        return jwsMap.get("roles", ArrayList.class);
    }

    @Override
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getPrivateKey(authenticationProperties.getPrivateKeyString()))
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(getPublicKey(authenticationProperties.getPublicKeyString()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public LocalDateTime getExpiresAt(String token) {
        return DateUtil.convertDateToLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
    }

    private static RSAPrivateKey getPrivateKey(String key) {
        try {
            byte[] encoded = Base64.getMimeDecoder().decode(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("", exception);
        }
        return null;
    }

    private static RSAPublicKey getPublicKey(String key) {
        try {
            byte[] encoded = Base64.getMimeDecoder().decode(key);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("", exception);
        }
        return null;
    }

}
