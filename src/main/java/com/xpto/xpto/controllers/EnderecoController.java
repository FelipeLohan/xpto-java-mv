package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.EnderecoCreateDTO;
import com.xpto.xpto.entities.Endereco;
import com.xpto.xpto.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<Endereco> criar(@RequestBody EnderecoCreateDTO dto) {
        Endereco novoEndereco = enderecoService.createEndereco(dto);
        return new ResponseEntity<>(novoEndereco, HttpStatus.CREATED);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Endereco>> listarPorCliente(@PathVariable Long clienteId) {
        List<Endereco> enderecos = enderecoService.listEnderecosByCliente(clienteId);
        return ResponseEntity.ok(enderecos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deleteEndereco(id);
        return ResponseEntity.noContent().build();
    }
}
