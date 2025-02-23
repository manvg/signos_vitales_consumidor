package com.backend.signos_vitales_consumidor.service;

import com.backend.signos_vitales_consumidor.model.SignosVitales;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegistroHistoricoService {

    private static final String HISTORIAL_API_URL = "http://50.17.240.116:8083/api/registro-historico/enviar";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RegistroHistoricoService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public void enviarARegistroHistorico(SignosVitales signos) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(signos), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(HISTORIAL_API_URL, request, String.class);

            System.out.println("Signos vitales normales enviados a RabbitMQ: " + signos.getNombrePaciente());
            System.out.println("Respuesta del servidor: " + response.getBody());

        } catch (JsonProcessingException e) {
            System.err.println("Error al serializar los signos vitales: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al enviar datos a RabbitMQ: " + e.getMessage());
        }
    }
}
