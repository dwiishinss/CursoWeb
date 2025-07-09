package com.lemonsoftware.lemonmoney.lemonmoney_api.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.exceptionHandler.lemonmoneyExceptionHandler.Erro;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Lancamento;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Pessoa;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.LancamentoRepository;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter.LancamentoFilter;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.projection.ResumoLancamento;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.service.LancamentoService;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.service.exceptions.PessoaInativaOuInexistenteException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
    
    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
    public Page<Lancamento> pesquisarLancamentos(LancamentoFilter lancamentoFilter, Pageable page) {
        return lancamentoRepository.filtrar(lancamentoFilter, page);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
    public Page<ResumoLancamento> resumirLancamentos(LancamentoFilter lancamentoFilter, Pageable page) {
        return lancamentoRepository.resumir(lancamentoFilter, page);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> buscarLancamentoById(@PathVariable Long id){
        Optional<Lancamento> lancamento = lancamentoRepository.findById(id);

        if (!lancamento.isPresent()) {
            throw new EmptyResultDataAccessException(1);    
        }

        return ResponseEntity.ok(lancamento.get());
    }
    
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> criarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalva = lancamentoService.salvarLancamento(lancamento);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalva.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalva);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletaLancamento(@PathVariable Long id){
        lancamentoService.deletarLancamento(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> atualizarLancamento(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento) {
        Lancamento lancamentoSalvo = lancamentoService.atualizarLancamento(id, lancamento);
        return ResponseEntity.ok(lancamentoSalvo);
    }

    @ExceptionHandler({PessoaInativaOuInexistenteException.class})
    public ResponseEntity<Object> handlePessoaInativaOuInexistenteException(PessoaInativaOuInexistenteException ex){
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }

}
