package com.vpnbeast.gatewayservice.service;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Function;

public interface JwtService {

    ArrayList getRolesFromToken(String token);
    Claims getAllClaimsFromToken(String token);
    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
    Boolean validateToken(String token, String username);
    Boolean isTokenValid(String token);
    LocalDateTime getExpiresAt(String token);
    String getUsernameFromToken(String token);

}
