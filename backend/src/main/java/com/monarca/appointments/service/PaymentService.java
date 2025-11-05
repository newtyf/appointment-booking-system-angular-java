package com.monarca.appointments.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final WebClient webClient;

    @Value("${culqi.secret-key}")
    private String secretKey;

    public PaymentService(@Value("${culqi.api-url}") String apiUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Map<String, Object> createCharge(
            String tokenId,
            Integer amount,
            String currency,
            String description,
            String email,
            Map<String, Object> metadata
    ) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amount);
        payload.put("currency_code", currency);
        payload.put("email", email);
        payload.put("source_id", tokenId);
        payload.put("description", description);
        
        if (metadata != null && !metadata.isEmpty()) {
            payload.put("metadata", metadata);
        }

        logger.info("Creating charge: {} - Amount: {} {}", description, amount / 100.0, currency);

        try {
            Map<String, Object> response = webClient.post()
                    .uri("/charges")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            logger.info("Charge created successfully: {}", response.get("id"));
            return response;

        } catch (WebClientResponseException e) {
            logger.error("Error creating charge: {}", e.getResponseBodyAsString());
            throw new RuntimeException(extractErrorMessage(e), e);
        } catch (Exception e) {
            logger.error("Unexpected error creating charge: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el procesador de pagos", e);
        }
    }

    public Map<String, Object> getCharge(String chargeId) {
        logger.info("Getting charge: {}", chargeId);

        try {
            Map<String, Object> response = webClient.get()
                    .uri("/charges/" + chargeId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response;

        } catch (WebClientResponseException e) {
            logger.error("Error getting charge: {}", e.getResponseBodyAsString());
            throw new RuntimeException("No se pudo obtener la información del cargo", e);
        } catch (Exception e) {
            logger.error("Unexpected error getting charge: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el procesador de pagos", e);
        }
    }

    public Map<String, Object> listCharges(String email, Integer limit) {
        logger.info("Listing charges (email: {}, limit: {})", email, limit);

        try {
            WebClient.RequestHeadersSpec<?> request = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/charges");
                        if (limit != null) {
                            uriBuilder.queryParam("limit", limit);
                        }
                        if (email != null && !email.isEmpty()) {
                            uriBuilder.queryParam("email", email);
                        }
                        return uriBuilder.build();
                    })
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);

            Map<String, Object> response = request
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return response;

        } catch (WebClientResponseException e) {
            logger.error("Error listing charges: {}", e.getResponseBodyAsString());
            throw new RuntimeException("No se pudo obtener el historial de pagos", e);
        } catch (Exception e) {
            logger.error("Unexpected error listing charges: {}", e.getMessage());
            throw new RuntimeException("Error de conexión con el procesador de pagos", e);
        }
    }

    private String extractErrorMessage(WebClientResponseException e) {
        try {
            Map<String, Object> errorBody = e.getResponseBodyAs(Map.class);
            if (errorBody != null && errorBody.containsKey("user_message")) {
                return (String) errorBody.get("user_message");
            }
        } catch (Exception ignored) {
        }
        return "Error al procesar el pago";
    }
}
