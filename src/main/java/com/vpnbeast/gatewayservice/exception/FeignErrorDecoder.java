package com.vpnbeast.gatewayservice.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public BusinessException decode(String methodKey, Response response) {
        BusinessException businessException = null;

        try {
            Map<String, Object> content = objectMapper.readValue(response.body().asInputStream(),
                    new TypeReference<>() {
                    });
            businessException = new BusinessException(String.valueOf(content.getOrDefault("tag", "")),
                    String.valueOf(content.getOrDefault("errorMessage", "unknown error occured at the backend")),
                    (Boolean) content.getOrDefault("status", false), response.status(), "");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return businessException;
    }

}
