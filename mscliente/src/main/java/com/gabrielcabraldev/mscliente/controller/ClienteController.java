package com.gabrielcabraldev.mscliente.controller;

import com.gabrielcabraldev.mscliente.domain.dto.ClienteRequest;
import com.gabrielcabraldev.mscliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String status(){
        log.info("Obtendo o status do microserviço de clientes");
        return "MS Cliente - OK";
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ClienteRequest clienteRequest) {
        var cliente = clienteRequest.toModel();
        clienteService.save(cliente);
        URI headerlocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .query("cpf = {cpf}")
                .buildAndExpand(cliente.getCpf())
                .toUri();
        return ResponseEntity.created(headerlocation).build();
    }

    @GetMapping(params = "cpf")
    public ResponseEntity dadosCliente(String cpf){
        var cliente = clienteService.findByCpf(cpf);
        if (cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }
}
