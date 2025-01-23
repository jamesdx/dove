package com.helix.dove.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RouteLocator routeLocator;

    @Test
    public void testRouteConfiguration() {
        routeLocator.getRoutes()
                .subscribe(route -> {
                    System.out.println("Route ID: " + route.getId());
                    System.out.println("Route URI: " + route.getUri());
                    System.out.println("Route Order: " + route.getOrder());
                    System.out.println("Route Predicates: " + route.getPredicate());
                    System.out.println("Route Filters: " + route.getFilters());
                });
    }

    @Test
    public void testExampleRoute() {
        webTestClient.get().uri("/example/test")
                .exchange()
                .expectStatus().isOk();
    }
} 