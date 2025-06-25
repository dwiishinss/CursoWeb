package com.lemonsoftware.teste.teste_api.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lemonsoftware.teste.teste_api.domain.exception.NegocioException;
import com.lemonsoftware.teste.teste_api.domain.model.Proprietario;
import com.lemonsoftware.teste.teste_api.domain.repository.ProprietarioRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RegistroProprietarioService {
    
    private ProprietarioRepository proprietarioRepository;

    @Transactional
    public Proprietario buscar(Long proprietarioId){
        return proprietarioRepository.findById(proprietarioId)
        .orElseThrow(() -> new NegocioException("Proprietario não encontrado"));
    }

    @Transactional
    public Proprietario salvar(Proprietario proprietario) {
        boolean emailEmUso = proprietarioRepository.findByEmail(proprietario.getEmail())
        .filter(p -> !p.equals(proprietario))
        .isPresent();

        if(emailEmUso){
            throw new NegocioException("Já existe usuário cadastrado com o email");
        }

        return proprietarioRepository.save(proprietario);
    }

    @Transactional
    public void excluir(Long proprietarioID){
        proprietarioRepository.deleteById(proprietarioID);
    }

}
