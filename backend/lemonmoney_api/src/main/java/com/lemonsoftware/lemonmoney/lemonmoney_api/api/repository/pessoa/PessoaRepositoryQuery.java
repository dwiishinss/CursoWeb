package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.pessoa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
    
    public List<Pessoa> buscaAtivos();
    public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable page);

}
