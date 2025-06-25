package com.lemonsoftware.lemonmoney.lemonmoney_api.api.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.event.RecursoCriadoEvent;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Categoria;
import com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.CategoriaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")
    public List<Categoria> listaCategorias() {
        return categoriaRepository.findAll();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA')")
    public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA')")   
    public ResponseEntity<Categoria> buscarCategoriaById(@PathVariable Long id){
        Optional<Categoria> categoria = categoriaRepository.findById(id);

        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        }

        return ResponseEntity.notFound().build();
    }
    

}
