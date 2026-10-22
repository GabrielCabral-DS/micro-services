package com.gabrielcabraldev.mscartoes.dto;

import com.gabrielcabraldev.mscartoes.domain.Cartao;
import com.gabrielcabraldev.mscartoes.enums.BandeiraCartao;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartaoRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limiteBasico;

    public Cartao toModel() {
        return new Cartao(nome, bandeira, renda, limiteBasico);
    }
}
