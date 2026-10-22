package com.gabrielcabraldev.msavaliadorcredito.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    @Value("${mq.queue.emissao-cartoes}")
    private String emissaoCatoes;

    @Bean
    public Queue EmissaoCartoesQueue() {
        return new Queue(emissaoCatoes, true);
    }
}
