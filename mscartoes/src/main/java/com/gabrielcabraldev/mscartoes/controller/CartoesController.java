package com.gabrielcabraldev.mscartoes.controller;

import com.gabrielcabraldev.mscartoes.domain.Cartao;
import com.gabrielcabraldev.mscartoes.dto.CartaoRequest;
import com.gabrielcabraldev.mscartoes.domain.ClienteCartao;
import com.gabrielcabraldev.mscartoes.dto.CartoesPorClienteResponse;
import com.gabrielcabraldev.mscartoes.service.CartaoService;
import com.gabrielcabraldev.mscartoes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartoesController {

    private final CartaoService cartaoService;
    private final ClienteService clienteService;

    @GetMapping
    public String status(){
        return "MS Cartoes - OK";
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CartaoRequest cartaoRequest){
        Cartao cartao = cartaoRequest.toModel();
        cartaoService.salvar(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRenda(@RequestParam("renda") Long renda){
        List<Cartao> cartoes = cartaoService.getCartoes(renda);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getClientesPorCpf(@RequestParam("cpf") String cpf) {
        List<ClienteCartao> clienteCartaos = clienteService.listarClienterPorCpf(cpf);
        List<CartoesPorClienteResponse> cartoesPorClienteResponses = clienteCartaos.stream()
                .map(CartoesPorClienteResponse::fromModel)
                .toList();
        return ResponseEntity.ok(cartoesPorClienteResponses);
    }
}
