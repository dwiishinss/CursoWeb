package com.lemonsoftware.teste.teste_api.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.lemonsoftware.teste.teste_api.api.model.VeiculoModel;
import com.lemonsoftware.teste.teste_api.api.model.input.VeiculoInputModel;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class VeiculoAssembler {
    
    private final ModelMapper modelMapper;

    public Veiculo toEntity(VeiculoInputModel veiculoInputModel){
        return modelMapper.map(veiculoInputModel, Veiculo.class);
    }

    public VeiculoModel toModel(Veiculo veiculo){
        return modelMapper.map(veiculo, VeiculoModel.class);
    }

    public List<VeiculoModel> toCollectionModel(List<Veiculo> veiculos){
        return veiculos.stream().map(this::toModel).toList();
    }

}
