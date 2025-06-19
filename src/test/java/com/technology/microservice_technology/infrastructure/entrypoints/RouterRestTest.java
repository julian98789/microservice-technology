package com.technology.microservice_technology.infrastructure.entrypoints;

import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyCapabilityHandlerImpl;
import com.technology.microservice_technology.infrastructure.entrypoints.handler.TechnologyHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    @Mock
    private TechnologyHandlerImpl technologyHandler;

    @Mock
    private TechnologyCapabilityHandlerImpl technologyCapabilityHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(
                routerRest.routerFunction(technologyHandler, technologyCapabilityHandler)
        ).build();

        lenient().when(technologyHandler.createTechnology(any())).thenReturn(ServerResponse.ok().build());
        lenient().when(technologyCapabilityHandler.associateTechnologiesToCapability(any())).thenReturn(ServerResponse.ok().build());
        lenient().when(technologyCapabilityHandler.findTechnologiesByCapabilityId(any())).thenReturn(ServerResponse.ok().build());
        lenient().when(technologyCapabilityHandler.getAllCapabilityRelationCounts(any())).thenReturn(ServerResponse.ok().build());
        lenient().when(technologyCapabilityHandler.deleteTechnologiesByCapabilityId(any())).thenReturn(ServerResponse.noContent().build());
    }

    @Test
    void testCreateTechnologyRoute() {
        webTestClient.post().uri("/technology")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testAssociateTechnologiesToCapabilityRoute() {
        webTestClient.post().uri("/technology/capability/associate")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testFindTechnologiesByCapabilityIdRoute() {
        webTestClient.get().uri("/technology/capability/1/technologies")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testGetAllCapabilityRelationCountsRoute() {
        webTestClient.get().uri("/technology/capability/relation-counts")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteTechnologiesByCapabilityIdRoute() {
        webTestClient.delete().uri("/technology/capability/1/exclusive-delete")
                .exchange()
                .expectStatus().isNoContent();
    }
}