package com.helix.dove.exception;

import com.helix.dove.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Order(-1)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return writeResponse(response, new ResponseDTO<>(404, "Service Not Found", null));
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            response.setStatusCode(responseStatusException.getStatusCode());
            return writeResponse(response, new ResponseDTO<>(responseStatusException.getStatusCode().value(), 
                responseStatusException.getMessage(), null));
        }

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("Global error occurred: ", ex);
        return writeResponse(response, new ResponseDTO<>(500, "Internal Server Error", null));
    }

    private Mono<Void> writeResponse(ServerHttpResponse response, ResponseDTO<?> responseDTO) {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(responseDTO);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error writing response: ", e);
            return Mono.error(e);
        }
    }
} 