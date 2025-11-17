package com.example.animals.controller;

import com.example.animals.dto.KeeperDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Controlador Proxy para redirigir peticiones del frontend hacia Keepers API
 * El frontend solo necesita llamar a http://localhost:8080/keepers
 */
@RestController
@RequestMapping("/keepers")
@CrossOrigin(origins = "*")
public class KeeperProxyController {

    private final WebClient webClient;

    public KeeperProxyController(@Value("${keepers.api.url}") String keepersApiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(keepersApiUrl)
                .build();
    }

    // GET /keepers - Obtener todos los cuidadores (con filtro opcional)
    @GetMapping
    public ResponseEntity<?> getAllKeepers(@RequestParam(required = false) Boolean is_active) {
        try {
            String uri = is_active != null ? "/keepers?is_active=" + is_active : "/keepers";
            
            List<KeeperDTO> keepers = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(KeeperDTO.class)
                    .collectList()
                    .block();
            
            return ResponseEntity.ok(keepers);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // GET /keepers/{id} - Obtener cuidador por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getKeeperById(@PathVariable Long id) {
        try {
            KeeperDTO keeper = webClient.get()
                    .uri("/keepers/{id}", id)
                    .retrieve()
                    .bodyToMono(KeeperDTO.class)
                    .block();
            
            return ResponseEntity.ok(keeper);
        } catch (WebClientResponseException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", "Keeper with id " + id + " not found"));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // POST /keepers - Crear nuevo cuidador
    @PostMapping
    public ResponseEntity<?> createKeeper(@RequestBody KeeperDTO keeper) {
        try {
            KeeperDTO createdKeeper = webClient.post()
                    .uri("/keepers")
                    .bodyValue(keeper)
                    .retrieve()
                    .bodyToMono(KeeperDTO.class)
                    .block();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdKeeper);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // PUT /keepers/{id} - Actualizar cuidador
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKeeper(@PathVariable Long id, @RequestBody KeeperDTO keeper) {
        try {
            KeeperDTO updatedKeeper = webClient.put()
                    .uri("/keepers/{id}", id)
                    .bodyValue(keeper)
                    .retrieve()
                    .bodyToMono(KeeperDTO.class)
                    .block();
            
            return ResponseEntity.ok(updatedKeeper);
        } catch (WebClientResponseException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", "Keeper with id " + id + " not found"));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // DELETE /keepers/{id} - Eliminar cuidador
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKeeper(@PathVariable Long id) {
        try {
            webClient.delete()
                    .uri("/keepers/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            
            return ResponseEntity.noContent().build();
        } catch (WebClientResponseException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Not Found", "message", "Keeper with id " + id + " not found"));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // GET /keepers/search/by-specialization - Buscar por especialización
    @GetMapping("/search/by-specialization")
    public ResponseEntity<?> searchBySpecialization(@RequestParam String specialization) {
        try {
            List<KeeperDTO> keepers = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/keepers/search/by-specialization")
                            .queryParam("specialization", specialization)
                            .build())
                    .retrieve()
                    .bodyToFlux(KeeperDTO.class)
                    .collectList()
                    .block();
            
            return ResponseEntity.ok(keepers);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // GET /keepers/search/by-experience - Buscar por experiencia mínima
    @GetMapping("/search/by-experience")
    public ResponseEntity<?> searchByExperience(@RequestParam Integer min_years) {
        try {
            List<KeeperDTO> keepers = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/keepers/search/by-experience")
                            .queryParam("min_years", min_years)
                            .build())
                    .retrieve()
                    .bodyToFlux(KeeperDTO.class)
                    .collectList()
                    .block();
            
            return ResponseEntity.ok(keepers);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // GET /keepers/search/by-name - Buscar por nombre
    @GetMapping("/search/by-name")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        try {
            List<KeeperDTO> keepers = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/keepers/search/by-name")
                            .queryParam("name", name)
                            .build())
                    .retrieve()
                    .bodyToFlux(KeeperDTO.class)
                    .collectList()
                    .block();
            
            return ResponseEntity.ok(keepers);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }

    // GET /keepers/search/active-by-specialization - Buscar activos por especialización
    @GetMapping("/search/active-by-specialization")
    public ResponseEntity<?> searchActiveBySpecialization(@RequestParam String specialization) {
        try {
            List<KeeperDTO> keepers = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/keepers/search/active-by-specialization")
                            .queryParam("specialization", specialization)
                            .build())
                    .retrieve()
                    .bodyToFlux(KeeperDTO.class)
                    .collectList()
                    .block();
            
            return ResponseEntity.ok(keepers);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Keepers service unavailable", "message", e.getMessage()));
        }
    }
}
