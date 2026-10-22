package com.gabrielcabraldev.msavaliadorcredito.domain;

import lombok.Data;

@Data
public class CartaoCliente {

    private String nome;
    private String bandeira;
    private String limiteLiberado;
}
