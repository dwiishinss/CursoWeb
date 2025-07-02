package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery{
    
}
