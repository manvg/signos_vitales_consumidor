package com.backend.signos_vitales_consumidor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignosVitales {
    private String nombrePaciente;
    private int ritmoCardiaco;
    private double temperatura;
    private int presionSistolica;
    private int presionDiastolica;
}