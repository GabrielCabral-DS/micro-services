package com.gabrielcabraldev.mscartoes.dto;

import com.gabrielcabraldev.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromModel(ClienteCartao clienteCartao) {
        CartoesPorClienteResponse dto = new CartoesPorClienteResponse();
        dto.setNome(clienteCartao.getCartao().getNome());
        dto.setBandeira(clienteCartao.getCartao().getBandeira().name());
        dto.setLimiteLiberado(clienteCartao.getLimite());
        return dto;
    }
}
