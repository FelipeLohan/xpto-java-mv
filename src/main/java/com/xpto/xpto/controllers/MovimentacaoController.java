package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.MovimentacaoDTO;
import com.xpto.xpto.entities.Movimentacao;
import com.xpto.xpto.services.MovimentacaoService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movimentacoes")
public class MovimentacaoController {

    // MUDANÇA: Injeção de dependência via construtor
    private final MovimentacaoService movimentacaoService;

    /**
     * Registra uma nova movimentação.
     */
    @PostMapping
    public ResponseEntity<Movimentacao> registrar(@RequestBody MovimentacaoDTO dto) {
        Movimentacao novaMovimentacao = movimentacaoService.createMovimentacao(dto);

        // MUDANÇA: Construção da URI para o novo recurso
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaMovimentacao.getId())
                .toUri();

        // MUDANÇA: Retornando status 201 Created com o header Location
        return ResponseEntity.created(uri).body(novaMovimentacao);
    }

    /**
     * Lista todas as movimentações de uma conta específica.
     */
    // NOVO ENDPOINT: Expondo a funcionalidade de listagem do service
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<Movimentacao>> listarPorConta(@PathVariable Long contaId) {
        List<Movimentacao> movimentacoes = movimentacaoService.listMovimentacoesByConta(contaId);
        return ResponseEntity.ok(movimentacoes);
    }
}