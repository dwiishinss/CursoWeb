package com.lemonsoftware.teste.teste_api.common;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lemonsoftware.teste.teste_api.api.model.VeiculoModel;
import com.lemonsoftware.teste.teste_api.domain.model.Veiculo;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modellMapper(){
        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Veiculo.class, VeiculoModel.class)
        .addMappings(mapper -> mapper.map(Veiculo::getPlaca, VeiculoModel::setNumeroPlaca));

        return modelMapper;
    }

}
