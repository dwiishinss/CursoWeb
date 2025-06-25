package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
}
