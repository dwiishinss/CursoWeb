package com.lemonsoftware.teste.teste_api.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lemonsoftware.teste.teste_api.domain.model.Autuacao;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RegistroAutuacaoService {

    private RegistroVeiculoService registroVeiculoService;

    @Transactional
    public Autuacao registrar(Long veiculoId, Autuacao novaAutuacao){
        Veiculo veiculo = registroVeiculoService.buscar(veiculoId);
        return veiculo.adicionarAutuacao(novaAutuacao);
    }

}
