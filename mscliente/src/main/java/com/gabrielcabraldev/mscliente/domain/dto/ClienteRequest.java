package com.gabrielcabraldev.mscliente.domain.dto;

import com.gabrielcabraldev.mscliente.domain.Cliente;
import lombok.Data;

@Data
public class ClienteRequest {

    private String nome;
    private String cpf;
    private Integer idade;

    public Cliente toModel() {
        return new Cliente(cpf, nome, idade);
    }

}
