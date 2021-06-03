package com.vpnbeast.gatewayservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidateTokenRequest {

    private String token;

}