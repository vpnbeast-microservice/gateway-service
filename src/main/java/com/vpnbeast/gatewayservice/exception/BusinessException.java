package com.vpnbeast.gatewayservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private String tag;
    private String errorMessage;
    private Boolean status;
    private Integer httpCode;
    private String timestamp;

}