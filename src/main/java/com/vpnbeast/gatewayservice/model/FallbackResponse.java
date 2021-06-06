package com.vpnbeast.gatewayservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FallbackResponse {

    private String errorMessage;
    private Boolean status;
    private Integer httpCode;
    private LocalDateTime timestamp;

}