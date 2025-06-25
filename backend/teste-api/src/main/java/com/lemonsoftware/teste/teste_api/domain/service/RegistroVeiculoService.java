package com.lemonsoftware.teste.teste_api.domain.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lemonsoftware.teste.teste_api.domain.exception.EntityNotFoundException;
import com.lemonsoftware.teste.teste_api.domain.exception.NegocioException;
import com.lemonsoftware.teste.teste_api.domain.model.Proprietario;
import com.lemonsoftware.teste.teste_api.domain.model.StatusVeiculo;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;
import com.lemonsoftware.teste.teste_api.domain.repository.VeiculoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RegistroVeiculoService {

    private VeiculoRepository veiculoRepository;
    private RegistroProprietarioService registroProprietarioService;

    @Transactional
    public Veiculo salvar(Veiculo veiculo){
        veiculo.setStatus(StatusVeiculo.REGULAR);
        veiculo.setDataCadastro(OffsetDateTime.now());

        boolean placaEmUso = veiculoRepository.findByPlaca(veiculo.getPlaca())
        .filter(v -> !v.equals(veiculo))
        .isPresent();

        Proprietario proprietario = registroProprietarioService.buscar(veiculo.getProprietario().getId());

        veiculo.setProprietario(proprietario);

        if(placaEmUso){
            throw new NegocioException("Já existe veiculo cadastrado com a placa");
        }

        return veiculoRepository.save(veiculo);
    }

    public Veiculo buscar(Long veiculoId){
        return veiculoRepository.findById(veiculoId)
        .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    }
    
}
