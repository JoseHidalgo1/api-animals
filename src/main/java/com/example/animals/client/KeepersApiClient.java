package com.example.animals.client;

import com.example.animals.dto.KeeperDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Cliente para comunicarse con el microservicio de Keepers
 */
@Component
public class KeepersApiClient {

    private final WebClient webClient;

    public KeepersApiClient(@Value("${keepers.api.url}") String keepersApiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(keepersApiUrl)
                .build();
    }

    /**
     * Obtener información de un Keeper por su ID
     * @param keeperId ID del keeper
     * @return KeeperDTO con la información del keeper, o null si no existe
     */
    public KeeperDTO getKeeperById(Long keeperId) {
        try {
            return webClient.get()
                    .uri("/keepers/{id}", keeperId)
                    .retrieve()
                    .bodyToMono(KeeperDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            return null;
        } catch (Exception e) {
            System.err.println("Error al comunicarse con Keepers API: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verificar si un Keeper existe
     * @param keeperId ID del keeper
     * @return true si existe, false si no
     */
    public boolean keeperExists(Long keeperId) {
        try {
            webClient.head()
                    .uri("/keepers/{id}", keeperId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println("Error al verificar existencia de Keeper: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verificar si el microservicio de Keepers está disponible
     * @return true si está disponible, false si no
     */
    public boolean isKeepersApiAvailable() {
        try {
            webClient.get()
                    .uri("/health")
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
