package com.lemonsoftware.lemonmoney.lemonmoney_api.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Lancamento;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.LancamentoRepository;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.PessoaRepository;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.service.exceptions.PessoaInativaOuInexistenteException;

@Service
public class LancamentoService {
    
    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvarLancamento(Lancamento lancamento){
        Optional<Pessoa> pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());
        if(!pessoa.isPresent() || !pessoa.get().isAtivo()){
            throw new PessoaInativaOuInexistenteException();
        }

        return lancamentoRepository.save(lancamento);

    }

    public void deletarLancamento(Long id){
        Optional<Lancamento> lancamento = lancamentoRepository.findById(id);
        if(!lancamento.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }

        lancamentoRepository.deleteById(id);
    }

}
