package com.helix.dove.controller;

import com.helix.dove.dto.ResponseDTO;
import com.helix.dove.dto.RouteDTO;
import com.helix.dove.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public Flux<ResponseDTO<RouteDTO>> getRoutes() {
        return routeService.getRoutes()
                .map(ResponseDTO::success);
    }

    @GetMapping("/{id}")
    public Mono<ResponseDTO<RouteDTO>> getRoute(@PathVariable String id) {
        return routeService.getRoute(id)
                .map(ResponseDTO::success);
    }

    @PostMapping
    public Mono<ResponseDTO<RouteDTO>> createRoute(@RequestBody RouteDTO route) {
        return routeService.createRoute(route)
                .map(ResponseDTO::success);
    }

    @PutMapping("/{id}")
    public Mono<ResponseDTO<RouteDTO>> updateRoute(@PathVariable String id, @RequestBody RouteDTO route) {
        return routeService.updateRoute(id, route)
                .map(ResponseDTO::success);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseDTO<Void>> deleteRoute(@PathVariable String id) {
        return routeService.deleteRoute(id)
                .then(Mono.just(ResponseDTO.success()));
    }
}