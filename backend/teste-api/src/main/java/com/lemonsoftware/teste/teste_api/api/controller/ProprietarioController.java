package com.lemonsoftware.teste.teste_api.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lemonsoftware.teste.teste_api.domain.model.Proprietario;
import com.lemonsoftware.teste.teste_api.domain.repository.ProprietarioRepository;
import com.lemonsoftware.teste.teste_api.domain.service.RegistroProprietarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;



@AllArgsConstructor
@RestController
@RequestMapping("/proprietarios")
public class ProprietarioController {
    
    private final ProprietarioRepository proprietarioRepository;
    private final RegistroProprietarioService registroProprietarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proprietario adicionarProprietario(@Valid @RequestBody Proprietario proprietario){
        return registroProprietarioService.salvar(proprietario);
    }

    @GetMapping
    public List<Proprietario> listaProprietarios(){
        return proprietarioRepository.findAll();
    } 

    @GetMapping("/{proprietarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Proprietario buscarProprietario(@PathVariable Long proprietarioId){
        return registroProprietarioService.buscar(proprietarioId);
    }

    @PutMapping("/{proprietarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Proprietario> atualizarProprietario(@PathVariable Long proprietarioId, @Valid @RequestBody Proprietario proprietario) {
        if(!proprietarioRepository.existsById(proprietarioId)){
            return ResponseEntity.notFound().build();
        }
        proprietario.setId(proprietarioId);
        return ResponseEntity.ok(registroProprietarioService.salvar(proprietario));
    }

    @DeleteMapping("/{proprietarioId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> removerProprietario(@PathVariable Long proprietarioId){
        if(proprietarioRepository.existsById(proprietarioId)){
            registroProprietarioService.excluir(proprietarioId);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
