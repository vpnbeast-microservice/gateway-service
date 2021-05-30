package com.vpnbeast.gatewayservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionInfo {

    private final String tag;
    private final String errorMessage;
    private final Boolean status;
    private final Integer httpCode;
    private final LocalDateTime timestamp;

}
