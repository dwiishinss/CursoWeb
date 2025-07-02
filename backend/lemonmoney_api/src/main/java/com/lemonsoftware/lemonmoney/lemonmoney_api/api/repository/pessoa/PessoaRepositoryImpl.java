package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.PessoaFilter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;

        @Override
    public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable page) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        List<Predicate> predicates = criarRestricoes(builder, root, pessoaFilter); 

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Pessoa> query = manager.createQuery(criteria);
        adicionarRestricaoDePaginacao(query, page);
        
        return new PageImpl<>(query.getResultList(), page, total(pessoaFilter));
    }

    @Override
    public List<Pessoa> buscaAtivos() {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        PessoaFilter filtro = new PessoaFilter();
        filtro.setAtivo(true);

        List<Predicate> predicates = criarRestricoes(builder, root, filtro); 

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Pessoa> query = manager.createQuery(criteria);
        
        return query.getResultList();
    }

    private List<Predicate> criarRestricoes(CriteriaBuilder builder, Root<Pessoa> root, PessoaFilter filtroPessoa){
        List<Predicate> predicates = new ArrayList<>();

        if(filtroPessoa.isAtivo()){
            predicates.add(builder.equal(((Path<Pessoa>) root).get("ativo"), true));
        }

        if (filtroPessoa.getNome() != null && !filtroPessoa.getNome().isBlank()) {
            predicates.add(
                builder.like(
                    builder.lower(((Path<Pessoa>) root).get("nome")),
                    "%" + filtroPessoa.getNome().toLowerCase() + "%"
                )
            );
        }

        return predicates;
    }

    private Long total(PessoaFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pessoa> root = criteria.from(Pessoa.class);

        List<Predicate> predicates = criarRestricoes(builder, root, filtro);
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
