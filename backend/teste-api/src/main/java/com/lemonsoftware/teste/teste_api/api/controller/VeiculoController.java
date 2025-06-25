package com.lemonsoftware.teste.teste_api.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lemonsoftware.teste.teste_api.api.assembler.VeiculoAssembler;
import com.lemonsoftware.teste.teste_api.api.model.VeiculoModel;
import com.lemonsoftware.teste.teste_api.api.model.input.VeiculoInputModel;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;
import com.lemonsoftware.teste.teste_api.domain.repository.VeiculoRepository;
import com.lemonsoftware.teste.teste_api.domain.service.ApreencaoVeiculoService;
import com.lemonsoftware.teste.teste_api.domain.service.RegistroVeiculoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@AllArgsConstructor
@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    
    private final VeiculoRepository veiculoRepository;
    private final RegistroVeiculoService registroVeiculoService;
    private final VeiculoAssembler veiculoAssembler;
    private final ApreencaoVeiculoService apreencaoVeiculoService;

    @GetMapping
    public List<VeiculoModel> listaVeiculos(){
        return veiculoAssembler.toCollectionModel(veiculoRepository.findAll());
    }

    @GetMapping("/{veiculoId}")
    public ResponseEntity<VeiculoModel> buscarVeiculo(@PathVariable Long veiculoId) {
        return veiculoRepository.findById(veiculoId)
        .map(veiculo -> veiculoAssembler.toModel(veiculo))
        .map(veiculo -> ResponseEntity.ok(veiculo))
        .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoModel adicionarVeiculo(@Valid @RequestBody VeiculoInputModel veiculoInput) {
        Veiculo novoVeiculo = veiculoAssembler.toEntity(veiculoInput);
        Veiculo veiculoCadastrado = registroVeiculoService.salvar(novoVeiculo);
        return veiculoAssembler.toModel(veiculoCadastrado);
    }

    @PutMapping("{veiculoId}/apreensao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void apreender(@PathVariable Long veiculoId) {
        apreencaoVeiculoService.apreender(veiculoId);
    }

    @DeleteMapping("{veiculoId}/apreensao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void liberar(@PathVariable Long veiculoId) {
        apreencaoVeiculoService.liberar(veiculoId);
    }

}
