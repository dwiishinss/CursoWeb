package com.lemonsoftware.lemonmoney.lemonmoney_api.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.PessoaRepository;

@Service
public class PessoaService {
    
    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa atualizarPessoa(Long id, Pessoa pessoa){
        Pessoa pessoaSalva = buscarPessoaById(id);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "id");

        return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPessoaAtivo(Long id, Boolean ativo){
        Pessoa pessoaSalva = buscarPessoaById(id);
        pessoaSalva.setAtivo(ativo);
        pessoaRepository.save(pessoaSalva);
    }

    public Pessoa buscarPessoaById(Long id){
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if(!pessoaRepository.existsById(id)){
            throw new EmptyResultDataAccessException(1);
        }
        return pessoaOptional.get();
    }

}
