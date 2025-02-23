package com.backend.signos_vitales_consumidor.service;

import com.backend.signos_vitales_consumidor.model.AlertaMedica;
import com.backend.signos_vitales_consumidor.model.SignosVitales;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KafkaConsumidorService {

    private static final String TOPIC_ALERTAS = "alertas";
    private static final AtomicInteger contadorAlertas = new AtomicInteger(1);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RegistroHistoricoService registroHistoricoService;

    private final ObjectMapper objectMapper;

    public KafkaConsumidorService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "signos_vitales", groupId = "grupo-procesamiento")
    public void consumirMensaje(String mensaje) {
        try {
            SignosVitales signos = objectMapper.readValue(mensaje, SignosVitales.class);

            String observacion = generarObservacion(signos);
            if (observacion != null) {
                AlertaMedica alerta = new AlertaMedica();
                alerta.setIdAlertaMedica(contadorAlertas.getAndIncrement());
                alerta.setNombrePaciente(signos.getNombrePaciente());
                alerta.setRitmoCardiaco(signos.getRitmoCardiaco());
                alerta.setTemperatura(signos.getTemperatura());
                alerta.setPresionSistolica(signos.getPresionSistolica());
                alerta.setPresionDiastolica(signos.getPresionDiastolica());
                alerta.setObservacion(observacion);

                //Publicar en el tópico de alertas
                kafkaTemplate.send(TOPIC_ALERTAS, objectMapper.writeValueAsString(alerta));
                System.out.println("Alerta médica generada para " + signos.getNombrePaciente());

            } else {
                registroHistoricoService.enviarARegistroHistorico(signos);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String generarObservacion(SignosVitales signos) {
        if (signos.getRitmoCardiaco() < 50) return "Bradicardia: Ritmo cardíaco demasiado bajo.";
        if (signos.getRitmoCardiaco() > 150) return "Taquicardia: Ritmo cardíaco demasiado alto.";
        if (signos.getTemperatura() < 35) return "Hipotermia: Temperatura corporal baja.";
        if (signos.getTemperatura() > 39) return "Fiebre alta: Posible infección.";
        if (signos.getPresionSistolica() > 180 || signos.getPresionDiastolica() > 120)
            return "Hipertensión grave: Riesgo de crisis hipertensiva.";
        return null;
    }
}
