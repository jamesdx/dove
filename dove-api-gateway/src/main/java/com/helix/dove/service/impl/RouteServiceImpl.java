package com.helix.dove.service.impl;

import com.helix.dove.dto.RouteDTO;
import com.helix.dove.entity.RouteEntity;
import com.helix.dove.repository.RouteRepository;
import com.helix.dove.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    public Mono<RouteDTO> createRoute(RouteDTO route) {
        RouteEntity entity = convertToEntity(route);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        return routeRepository.save(entity)
                .map(this::convertToDTO);
    }

    @Override
    public Mono<RouteDTO> updateRoute(String id, RouteDTO route) {
        return routeRepository.findById(Long.valueOf(id))
                .flatMap(existingRoute -> {
                    updateEntityFromDTO(existingRoute, route);
                    existingRoute.setUpdatedTime(LocalDateTime.now());
                    return routeRepository.save(existingRoute);
                })
                .map(this::convertToDTO);
    }

    @Override
    public Mono<Void> deleteRoute(String id) {
        return routeRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public Mono<RouteDTO> getRoute(String id) {
        return routeRepository.findById(Long.valueOf(id))
                .map(this::convertToDTO);
    }

    @Override
    public Flux<RouteDTO> getRoutes() {
        return routeRepository.findAll()
                .map(this::convertToDTO);
    }

    @Override
    public Flux<RouteDTO> getRoutesByServiceName(String serviceName) {
        return routeRepository.findByServiceName(serviceName)
                .map(this::convertToDTO);
    }

    @Override
    public Flux<RouteDTO> getRoutesByEnabled(Boolean enabled) {
        return routeRepository.findByEnabled(enabled)
                .map(this::convertToDTO);
    }

    @Override
    public Mono<RouteDTO> updateRouteStatus(String id, Boolean enabled) {
        return routeRepository.findById(Long.valueOf(id))
                .flatMap(route -> {
                    route.setEnabled(enabled);
                    route.setUpdatedTime(LocalDateTime.now());
                    return routeRepository.save(route);
                })
                .map(this::convertToDTO);
    }

    @Override
    public Mono<RouteDTO> updateRouteWeight(String id, Integer weight) {
        return routeRepository.findById(Long.valueOf(id))
                .flatMap(route -> {
                    route.setWeight(weight);
                    route.setUpdatedTime(LocalDateTime.now());
                    return routeRepository.save(route);
                })
                .map(this::convertToDTO);
    }

    @Override
    public Mono<RouteDTO> addPredicate(String id, Object predicate) {
        // TODO: Implement predicate addition logic
        return Mono.empty();
    }

    @Override
    public Mono<RouteDTO> addFilter(String id, Object filter) {
        // TODO: Implement filter addition logic
        return Mono.empty();
    }

    @Override
    public Mono<Void> deletePredicate(String id, String predicateId) {
        // TODO: Implement predicate deletion logic
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteFilter(String id, String filterId) {
        // TODO: Implement filter deletion logic
        return Mono.empty();
    }

    private RouteEntity convertToEntity(RouteDTO dto) {
        RouteEntity entity = new RouteEntity();
        entity.setRouteId(dto.getId());
        entity.setUri(dto.getUri());
        entity.setPredicates(dto.getPredicates().toString());
        entity.setFilters(dto.getFilters().toString());
        entity.setMetadata(dto.getMetadata());
        entity.setOrder(dto.getOrder());
        entity.setEnabled(dto.isEnabled());
        return entity;
    }

    private RouteDTO convertToDTO(RouteEntity entity) {
        RouteDTO dto = new RouteDTO();
        dto.setId(entity.getRouteId());
        dto.setUri(entity.getUri());
        // TODO: Convert predicates and filters from string to object
        dto.setMetadata(entity.getMetadata());
        dto.setOrder(entity.getOrder());
        dto.setEnabled(entity.getEnabled());
        return dto;
    }

    private void updateEntityFromDTO(RouteEntity entity, RouteDTO dto) {
        entity.setUri(dto.getUri());
        entity.setPredicates(dto.getPredicates().toString());
        entity.setFilters(dto.getFilters().toString());
        entity.setMetadata(dto.getMetadata());
        entity.setOrder(dto.getOrder());
        entity.setEnabled(dto.isEnabled());
    }
}