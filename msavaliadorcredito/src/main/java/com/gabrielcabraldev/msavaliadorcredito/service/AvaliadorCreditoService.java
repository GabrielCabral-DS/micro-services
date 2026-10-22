package com.gabrielcabraldev.msavaliadorcredito.service;

import com.gabrielcabraldev.msavaliadorcredito.domain.*;
import com.gabrielcabraldev.msavaliadorcredito.exception.DadosClienteNotFoundException;
import com.gabrielcabraldev.msavaliadorcredito.exception.ErroComunicacaoMicroServicesException;
import com.gabrielcabraldev.msavaliadorcredito.exception.ErroSolicitacaoCartaoException;
import com.gabrielcabraldev.msavaliadorcredito.infra.CartoesResourceClient;
import com.gabrielcabraldev.msavaliadorcredito.infra.ClienteResourceClient;
import com.gabrielcabraldev.msavaliadorcredito.infra.mqueue.SolicitcaoEmissaoCartaoPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;
    private final SolicitcaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException{

    try {
        ResponseEntity<DadosCliente> response = clienteResourceClient.consultaSituacaoCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesResourceClient.getClientesPorCpf(cpf);

        return SituacaoCliente.builder()
                .cliente(response.getBody())
                .cartoes(cartoesResponse.getBody())
                .build();
    }catch (FeignException.FeignClientException e){
        int status = e.status();
        if (HttpStatus.NOT_FOUND.value() == status){
            throw new DadosClienteNotFoundException();
        }
        throw new ErroComunicacaoMicroServicesException(e.getMessage(), status);
    }

    }

    public RetornoAvaliacaoCliente retornoAvaliacaoCliente (String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {
        try {
            ResponseEntity<DadosCliente> response = clienteResourceClient.consultaSituacaoCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourceClient.getCartoes(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listarCartoesAprovados = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = response.getBody();

                BigDecimal limiteAprovado = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovadoFinal = fator.multiply(limiteAprovado);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovadoFinal);

                return aprovado;

            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listarCartoesAprovados);


        }catch (FeignException.FeignClientException e){
            int status = e.status();
            if (HttpStatus.NOT_FOUND.value() == status){
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroServicesException(e.getMessage(), status);
        }

    }

    public Object solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        try {
            emissaoCartaoPublisher.solicitarEmissaoCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }

}
