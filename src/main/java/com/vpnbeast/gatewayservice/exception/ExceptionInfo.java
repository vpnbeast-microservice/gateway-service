package com.vpnbeast.gatewayservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionInfo {

    private String tag;
    private String errorMessage;
    private Boolean status;
    private Integer httpCode;
    private LocalDateTime timestamp;

}
