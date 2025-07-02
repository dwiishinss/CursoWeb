package com.lemonsoftware.lemonmoney.lemonmoney_api.api.repository.filter;

public class PessoaFilter {
    
    private boolean ativo = false;
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
