package com.vpnbeast.gatewayservice.exception;

import lombok.*;

@Getter
@RequiredArgsConstructor
public class ClientException extends RuntimeException {

    private final transient ExceptionInfo exceptionInfo;

}