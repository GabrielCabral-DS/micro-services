package com.gabrielcabraldev.mscartoes.repository;

import com.gabrielcabraldev.mscartoes.domain.ClienteCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteCartao, Long> {
     List<ClienteCartao> findByCpf(String cpf);
}
