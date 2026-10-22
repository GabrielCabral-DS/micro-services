package com.gabrielcabraldev.mscartoes.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcabraldev.mscartoes.domain.Cartao;
import com.gabrielcabraldev.mscartoes.domain.ClienteCartao;
import com.gabrielcabraldev.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import com.gabrielcabraldev.mscartoes.repository.CartaoRepository;
import com.gabrielcabraldev.mscartoes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository cartaoRepository;
    private final ClienteRepository clienteRepository;

    @RabbitListener(queues = "${mq.queue.emissao-cartoes}")
    public void receberSolicitacaoEmissaoCartao(@Payload String payload) {
       try {
           var mapper = new ObjectMapper();
           DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
           Cartao cartao = cartaoRepository.findById(dados.getIdCartao()).orElseThrow();
           ClienteCartao clienteCartao = new ClienteCartao();
           clienteCartao.setCartao(cartao);
           clienteCartao.setCpf(dados.getCpf());
           clienteCartao.setLimite(dados.getLimiteLiberado());
           clienteRepository.save(clienteCartao);
       }catch (JsonProcessingException e){
           e.printStackTrace();
       }
    }
}
