package com.lemonsoftware.teste.teste_api.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;


@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long>{
    
    public Optional<Veiculo> findByPlaca(String placa);

}
