package com.gabrielcabraldev.msavaliadorcredito.exception;

import lombok.Getter;

public class ErroComunicacaoMicroServicesException extends Exception{
    @Getter
    private Integer status;

    public ErroComunicacaoMicroServicesException(String mensagem, Integer status) {
        super(mensagem);
        this.status = status;
    }
}
