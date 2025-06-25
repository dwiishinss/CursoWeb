package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Lancamento;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.LancamentoFilter;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
    
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable page);
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable page);

}
