package com.xpto.xpto.controllers;

import com.xpto.xpto.dtos.MovimentacaoCreateDTO;
import com.xpto.xpto.entities.Movimentacao;
import com.xpto.xpto.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @PostMapping
    public ResponseEntity<Movimentacao> registrar(@RequestBody MovimentacaoCreateDTO dto) {
        Movimentacao novaMovimentacao = movimentacaoService.createMovimentacao(dto);
        return new ResponseEntity<>(novaMovimentacao, HttpStatus.CREATED);
    }
}
