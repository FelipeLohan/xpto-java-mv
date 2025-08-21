package com.xpto.xpto.controllers;

import com.xpto.xpto.entities.Conta;
import com.xpto.xpto.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @DeleteMapping("/{id}/exclusao-logica")
    public ResponseEntity<Void> deletarContaLogicamente(@PathVariable Long id) {
        contaService.deleteLogicaConta(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        Conta contaAtualizada = contaService.updateConta(id, conta);
        return ResponseEntity.ok(contaAtualizada);
    }
}
