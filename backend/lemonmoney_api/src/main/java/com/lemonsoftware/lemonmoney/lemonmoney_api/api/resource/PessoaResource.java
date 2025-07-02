package com.lemonsoftware.lemonmoney.lemonmoney_api.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.event.RecursoCriadoEvent;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.PessoaRepository;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.PessoaFilter;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.service.PessoaService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
    public Page<Pessoa> listaPessoas(PessoaFilter pessoaFilter, Pageable page) {
        return pessoaRepository.filtrar(pessoaFilter, page);
    }

    @GetMapping("/ativo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
    public List<Pessoa> listaPessoasAtivas() {
        return pessoaRepository.buscaAtivos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
    public ResponseEntity<Pessoa> buscarPessoaById(@PathVariable Long id){
        Optional<Pessoa> pessoa = pessoaRepository.findById(id);

        if (pessoa.isPresent()) {
            return ResponseEntity.ok(pessoa.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
    public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA')")
    public ResponseEntity<Void> removerPessoa(@PathVariable Long id){
        if(!pessoaRepository.existsById(id)){
            throw new EmptyResultDataAccessException(1);
        }
        pessoaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaService.atualizarPessoa(id, pessoa);
        return ResponseEntity.ok(pessoaSalva);
    }

    @PutMapping("/{id}/ativo")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putMethodName(@PathVariable Long id, @RequestBody Boolean ativo) {
        pessoaService.atualizarPessoaAtivo(id, ativo);
    }

}
