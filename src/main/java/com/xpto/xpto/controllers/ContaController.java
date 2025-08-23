package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.ContaDTO;
import com.xpto.xpto.entities.Conta;
import com.xpto.xpto.services.ContaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/contas")
public class ContaController {

    private final ContaService contaService;

    /**
     * Busca a conta de um cliente específico.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Conta> buscarPorClienteId(@PathVariable Long clienteId) {
        Conta conta = contaService.findByClienteId(clienteId);
        return ResponseEntity.ok(conta);
    }

    /**
     * Desativa (exclusão lógica) uma conta.
     */
    @PutMapping("/{contaId}/desativar")
    public ResponseEntity<Conta> desativarConta(@PathVariable Long contaId) {
        Conta contaDesativada = contaService.desativarConta(contaId);
        return ResponseEntity.ok(contaDesativada);
    }

    /**
     * Ativa uma conta previamente desativada.
     */
    @PutMapping("/{contaId}/ativar")
    public ResponseEntity<Conta> ativarConta(@PathVariable Long contaId) {
        Conta contaAtivada = contaService.ativarConta(contaId);
        return ResponseEntity.ok(contaAtivada);
    }

    /**
     * Atualiza os dados de uma conta, buscando-a pelo ID do cliente.
     */
    @PutMapping("/cliente/{clienteId}")
    public ResponseEntity<Conta> atualizarPorClienteId(@PathVariable Long clienteId, @RequestBody ContaDTO dto) {
        Conta contaAtualizada = contaService.updateByClienteId(clienteId, dto);
        return ResponseEntity.ok(contaAtualizada);
    }
}