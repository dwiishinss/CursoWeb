package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lemonsoftware.lemonmoney.lemonmoney_api.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    public Optional<Usuario> findByEmail(String email);

}
