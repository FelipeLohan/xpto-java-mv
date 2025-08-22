package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.EnderecoDTO;
import com.xpto.xpto.entities.Endereco;
import com.xpto.xpto.services.EnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enderecos")
public class EnderecoController {

    // MUDANÇA: Injeção de dependência via construtor
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    /**
     * Busca o endereço de um cliente específico.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Endereco> buscarPorClienteId(@PathVariable Long clienteId) {
        // MUDANÇA: Chama o método correto do service e retorna um único Endereco, não uma lista.
        Endereco endereco = enderecoService.findByClienteId(clienteId);
        return ResponseEntity.ok(endereco);
    }

    /**
     * Atualiza o endereço de um cliente específico.
     */
    @PutMapping("/cliente/{clienteId}")
    public ResponseEntity<Endereco> atualizarPorClienteId(@PathVariable Long clienteId, @RequestBody EnderecoDTO dto) {
        // MUDANÇA: Endpoint adicionado para expor a funcionalidade de atualização do service.
        Endereco enderecoAtualizado = enderecoService.updateByClienteId(clienteId, dto);
        return ResponseEntity.ok(enderecoAtualizado);
    }
}