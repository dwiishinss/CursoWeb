package com.lemonsoftware.teste.teste_api.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.lemonsoftware.teste.teste_api.api.model.AutucaoModel;
import com.lemonsoftware.teste.teste_api.api.model.input.AutuacaoInputModel;
import com.lemonsoftware.teste.teste_api.domain.model.Autuacao;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AutuacaoAssembler {
    
    private final ModelMapper modelMapper;

    public AutucaoModel toModel(Autuacao autuacao){
        return modelMapper.map(autuacao, AutucaoModel.class);
    }

    public List<AutucaoModel> toCollectionModel(List<Autuacao> autuacoes){
        return autuacoes.stream().map(this::toModel).toList();
    }

    public Autuacao toEntity(AutuacaoInputModel autuacaoInputModel){
        return modelMapper.map(autuacaoInputModel, Autuacao.class);
    }

}
