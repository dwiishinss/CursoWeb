package com.lemonsoftware.lemonmoney.lemonmoney_api.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
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

    public Lancamento atualizarLancamento(Long id, Lancamento lancamento){
        Lancamento lancamentoSalvo = buscarLancamentoById(id);
        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "id");

        return lancamentoRepository.save(lancamentoSalvo);
    }

    public Lancamento buscarLancamentoById(Long id){
        Optional<Lancamento> lancamentoOptional = lancamentoRepository.findById(id);
        if(!lancamentoRepository.existsById(id)){
            throw new EmptyResultDataAccessException(1);
        }
        return lancamentoOptional.get();
    }

    public void deletarLancamento(Long id){
        Optional<Lancamento> lancamento = lancamentoRepository.findById(id);
        if(!lancamento.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }

        lancamentoRepository.deleteById(id);
    }

}
