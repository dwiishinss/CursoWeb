package com.lemonsoftware.teste.teste_api.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lemonsoftware.teste.teste_api.api.assembler.AutuacaoAssembler;
import com.lemonsoftware.teste.teste_api.api.model.AutucaoModel;
import com.lemonsoftware.teste.teste_api.api.model.input.AutuacaoInputModel;
import com.lemonsoftware.teste.teste_api.domain.model.Autuacao;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;
import com.lemonsoftware.teste.teste_api.domain.service.RegistroAutuacaoService;
import com.lemonsoftware.teste.teste_api.domain.service.RegistroVeiculoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@AllArgsConstructor
@RequestMapping("/veiculos/{veiculoId}/autuacoes")
public class AutuacaoController {
    
    private final RegistroAutuacaoService registroAutuacaoService;
    private final AutuacaoAssembler autuacaoAssembler;
    private final RegistroVeiculoService registroVeiculoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AutucaoModel registrarAutuacao(@PathVariable Long veiculoId, @RequestBody @Valid AutuacaoInputModel autuacaoInputModel){

        Autuacao novaAutuacao = autuacaoAssembler.toEntity(autuacaoInputModel);
        Autuacao autuacaoRegistrada = registroAutuacaoService.registrar(veiculoId, novaAutuacao);

        return autuacaoAssembler.toModel(autuacaoRegistrada);

    }

    @GetMapping
    public List<AutucaoModel> listaAutuacao(@PathVariable Long veiculoId) {
        Veiculo veiculo = registroVeiculoService.buscar(veiculoId);
        return autuacaoAssembler.toCollectionModel(veiculo.getAutuacoes());
    }
    

}
