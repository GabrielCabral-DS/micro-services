package com.gabrielcabraldev.msavaliadorcredito.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DadosAvaliacao {

    private String cpf;
    private Long renda;
}
