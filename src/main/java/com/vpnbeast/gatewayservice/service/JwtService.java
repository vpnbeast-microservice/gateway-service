package com.vpnbeast.gatewayservice.service;

import com.vpnbeast.gatewayservice.model.ValidateTokenResponse;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.function.Function;

public interface JwtService {

    ArrayList getRolesFromToken(String token);
    Claims getAllClaimsFromToken(String token);
    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
    LocalDateTime getExpiresAt(String token);
    String getUsernameFromToken(String token);

}
