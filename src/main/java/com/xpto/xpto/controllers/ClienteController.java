package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.PessoaFisicaDTO;
import com.xpto.xpto.dtos.PessoaJuridicaDTO;
import com.xpto.xpto.entities.PessoaFisica;
import com.xpto.xpto.entities.PessoaJuridica;
import com.xpto.xpto.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/pessoas-fisicas")
    public ResponseEntity<PessoaFisica> criarPessoaFisica(@RequestBody PessoaFisicaDTO dto) {
        PessoaFisica novaPessoaFisica = clienteService.createPessoaFisica(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaPessoaFisica.getId())
                .toUri();

        return ResponseEntity.created(uri).body(novaPessoaFisica);
    }

    @PostMapping("/pessoas-juridicas")
    public ResponseEntity<PessoaJuridica> criarPessoaJuridica(@RequestBody PessoaJuridicaDTO dto) {
        PessoaJuridica novaPessoaJuridica = clienteService.createPessoaJuridica(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaPessoaJuridica.getId())
                .toUri();
        
        return ResponseEntity.created(uri).body(novaPessoaJuridica);
    }
}