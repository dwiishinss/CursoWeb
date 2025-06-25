package com.lemonsoftware.teste.teste_api.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApreencaoVeiculoService {
    
    private RegistroVeiculoService registroVeiculoService;

    @Transactional
    public void apreender(Long veiculoId){
        Veiculo veiculo = registroVeiculoService.buscar(veiculoId);
        veiculo.apreender();
    }


    @Transactional
    public void liberar(Long veiculoId){
        Veiculo veiculo = registroVeiculoService.buscar(veiculoId);
        veiculo.liberar();
    }

}
