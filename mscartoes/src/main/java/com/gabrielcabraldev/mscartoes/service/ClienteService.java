package com.gabrielcabraldev.mscartoes.service;

import com.gabrielcabraldev.mscartoes.domain.ClienteCartao;
import com.gabrielcabraldev.mscartoes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public List<ClienteCartao> listarClienterPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }


}
