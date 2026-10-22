package com.gabrielcabraldev.mscartoes.repository;

import com.gabrielcabraldev.mscartoes.domain.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByRendaLessThanEqual(Object renda);
}
