package com.lemonsoftware.teste.teste_api.api.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProprietarioIdModel {
    
    @NotNull
    private Long id;

}
