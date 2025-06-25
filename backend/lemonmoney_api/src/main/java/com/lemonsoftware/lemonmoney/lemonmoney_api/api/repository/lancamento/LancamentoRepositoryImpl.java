package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Lancamento;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.LancamentoFilter;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.projection.ResumoLancamento;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable page) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        root.fetch("categoria", JoinType.LEFT);
        root.fetch("pessoa", JoinType.LEFT);

        List<Predicate> predicates = criarRestricoes(lancamentoFilter, builder, root); 

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        adicionarRestricaoDePaginacao(query, page);
        
        return new PageImpl<>(query.getResultList(), page, total(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable page) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        criteria.select(builder.construct(ResumoLancamento.class 
        , root.get("id"), root.get("descricao"), root.get("dataVencimento")
        , root.get("dataPagamento"), root.get("valor"), root.get("tipo")
        , root.get("categoria").get("nome"), root.get("pessoa").get("nome")));
       
        List<Predicate> predicates = criarRestricoes(lancamentoFilter, builder, root); 

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
        adicionarRestricaoDePaginacao(query, page);
        
        return new PageImpl<>(query.getResultList(), page, total(lancamentoFilter));
    }

    private List<Predicate> criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root){
        List<Predicate> predicates = new ArrayList<>();

        if (lancamentoFilter.getDescricao() != null && !lancamentoFilter.getDescricao().isBlank()) {
            predicates.add(
                builder.like(
                    builder.lower(((Path<Lancamento>) root).get("descricao")),
                    "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"
                )
            );
        }

        if (lancamentoFilter.getDataVencimentoDe() != null) {
            predicates.add(
                builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe())
            );
        }

        if (lancamentoFilter.getDataVencimentoAte() != null) {
            predicates.add(
                builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte())
            );
        }

        return predicates;
    }

    private Long total(LancamentoFilter lancamentoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        List<Predicate> predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates.toArray(new Predicate[0]));

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private void adicionarRestricaoDePaginacao(TypedQuery<?> query, Pageable page) {
        int paginaAtual = page.getPageNumber();
        int totalRegistrosPorPagina = page.getPageSize();
        int primeiroRegistro = paginaAtual*totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistro);
        query.setMaxResults(totalRegistrosPorPagina);
    }
    
}
