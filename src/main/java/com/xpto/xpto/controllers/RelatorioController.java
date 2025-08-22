package com.xpto.xpto.controllers;

import com.xpto.xpto.services.RelatorioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * Endpoint para solicitar a geração de um relatório de saldo em arquivo .txt.
     * @param clienteId O ID do cliente para o qual o relatório será gerado.
     * @param dataInicio A data de início do período (formato AAAA-MM-DD).
     * @param dataFim A data de fim do período (formato AAAA-MM-DD).
     * @return Uma mensagem de sucesso ou erro.
     */
    @PostMapping("/cliente/{clienteId}/gerar-arquivo")
    public ResponseEntity<String> gerarArquivoRelatorioSaldoCliente(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        try {
            relatorioService.gerarRelatorioSaldoCliente(clienteId, dataInicio, dataFim);
            return ResponseEntity.ok("Relatório gerado com sucesso na pasta 'relatorios' do projeto.");
        
        } catch (EntityNotFoundException e) {
            // Retorna 404 Not Found se o cliente ou a conta não forem encontrados
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        
        } catch (IOException e) {
            // Retorna 500 Internal Server Error se houver um erro ao escrever o arquivo
            // Em um sistema real, o erro 'e' seria logado para análise.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Falha ao gerar o arquivo de relatório: " + e.getMessage());
        }
    }
}