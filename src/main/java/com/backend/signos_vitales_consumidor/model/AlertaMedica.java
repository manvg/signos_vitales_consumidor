package com.backend.signos_vitales_consumidor.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertaMedica {
    private int idAlertaMedica;
    private String nombrePaciente;
    private int ritmoCardiaco;
    private double temperatura;
    private int presionSistolica;
    private int presionDiastolica;
    private String observacion;
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
