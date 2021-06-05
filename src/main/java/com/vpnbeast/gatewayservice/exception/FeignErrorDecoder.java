package com.vpnbeast.gatewayservice.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpnbeast.gatewayservice.util.DateUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public ClientException decode(String methodKey, Response response) {
        ClientException clientException = null;

        try {
            Map<String, Object> content = objectMapper.readValue(response.body().asInputStream(),
                    new TypeReference<>() {
                    });
            clientException = new ClientException(ExceptionInfo.builder()
                    .tag(String.valueOf(content.getOrDefault("tag", "")))
                    .errorMessage(String.valueOf(content.getOrDefault("errorMessage", "unknown error occured at the backend")))
                    .status((Boolean) content.getOrDefault("status", false))
                    .httpCode(response.status())
                    .timestamp(DateUtil.getCurrentLocalDateTime())
                    .build());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return clientException;
    }

}
