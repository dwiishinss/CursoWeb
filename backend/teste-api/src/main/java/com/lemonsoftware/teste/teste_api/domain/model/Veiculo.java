package com.lemonsoftware.teste.teste_api.domain.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.lemonsoftware.teste.teste_api.domain.exception.StatusConflictException;
import com.lemonsoftware.teste.teste_api.domain.validation.ValidationGroups;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Veiculo {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @ConvertGroup(from = Default.class, to = ValidationGroups.ProprietarioId.class)
    @NotNull
    @ManyToOne
    private Proprietario proprietario;

    @NotBlank
    @Size(max = 20)
    private String marca;

    @NotBlank
    @Size(max = 20)
    private String modelo;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}")
    private String placa;

    @JsonProperty(access = Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private StatusVeiculo status;

    @JsonProperty(access = Access.READ_ONLY)
    private OffsetDateTime dataCadastro;

    @JsonProperty(access = Access.READ_ONLY)
    private OffsetDateTime dataApreensao;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)   
    private List<Autuacao> autuacoes = new ArrayList<>();
    

    public Autuacao adicionarAutuacao(Autuacao autuacao){
        autuacao.setDataOcorrencia(OffsetDateTime.now());
        autuacao.setVeiculo(this);
        getAutuacoes().add(autuacao);
        return autuacao;
    }

    public void apreender(){
        if(estaApreendido()){
            throw new StatusConflictException("Veiculo já se encontra apreendido");
        }

        setStatus(StatusVeiculo.APREENDIDO);
        setDataApreensao(OffsetDateTime.now());
    }

    public boolean estaApreendido(){
        return status.equals(StatusVeiculo.APREENDIDO);
    }

    public void liberar(){
        if(!estaApreendido()){
            throw new StatusConflictException("Veiculo não se encontra apreendido");
        }

        setStatus(StatusVeiculo.REGULAR);
        setDataApreensao(null);
    }

}
