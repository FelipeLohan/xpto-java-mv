package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.PessoaFisicaCreateDTO;
import com.xpto.xpto.dtos.PessoaJuridicaCreateDTO;
import com.xpto.xpto.entities.PessoaFisica;
import com.xpto.xpto.entities.PessoaJuridica;
import com.xpto.xpto.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/pessoas-fisicas")
    public ResponseEntity<PessoaFisica> criarPessoaFisica(@RequestBody PessoaFisicaCreateDTO dto) {
        PessoaFisica novaPessoaFisica = clienteService.createPessoaFisica(dto);
        return new ResponseEntity<>(novaPessoaFisica, HttpStatus.CREATED);
    }

    @PostMapping("/pessoas-juridicas")
    public ResponseEntity<PessoaJuridica> criarPessoaJuridica(@RequestBody PessoaJuridicaCreateDTO dto) {
        PessoaJuridica novaPessoaJuridica = clienteService.createPessoaJuridica(dto);
        return new ResponseEntity<>(novaPessoaJuridica, HttpStatus.CREATED);
    }
}
