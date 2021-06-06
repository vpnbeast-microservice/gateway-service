package com.vpnbeast.gatewayservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenResponse {

    private Boolean status;
    private String username;
    private String[] roles;
    private String errorMessage;
    private Integer httpCode;
    private String timestamp;

}
