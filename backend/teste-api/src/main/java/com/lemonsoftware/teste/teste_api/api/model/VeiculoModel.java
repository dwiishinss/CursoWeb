package com.lemonsoftware.teste.teste_api.api.model;

import java.time.OffsetDateTime;

import com.lemonsoftware.teste.teste_api.domain.model.StatusVeiculo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VeiculoModel {
    
    private Long id;
    private ProprietarioResumoModel proprietario;
    private String marca;
    private String modelo;
    private String numeroPlaca;
    private StatusVeiculo status;
    private OffsetDateTime dataCadastro;
    private OffsetDateTime dataApreensao;

}
