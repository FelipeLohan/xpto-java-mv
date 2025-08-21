package com.xpto.xpto.services;

import com.xpto.xpto.dtos.MovimentacaoCreateDTO;
import com.xpto.xpto.entities.Conta;
import com.xpto.xpto.entities.Movimentacao;
import com.xpto.xpto.exceptions.BusinessLogicException;
import com.xpto.xpto.repositories.ContaRepository;
import com.xpto.xpto.repositories.MovimentacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {
    
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    private ContaRepository contaRepository;

    /**
     * Registra uma nova movimentação para uma conta, simulando a integração.
     * @param dto Os dados da movimentação a ser criada.
     * @return A entidade Movimentacao que foi salva.
     */
    @Transactional
    public Movimentacao createMovimentacao(MovimentacaoCreateDTO dto) {
        // Busca a conta para associar a movimentação
        Conta conta = contaRepository.findById(dto.getContaId())
          .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com o ID: " + dto.getContaId()));

        // Regra de Negócio: Não permitir movimentações em contas inativas
        if (!conta.isAtivo()) {
          throw new BusinessLogicException("Não é possível registrar movimentação para uma conta inativa.");
        }
        
        Movimentacao newMovimentacao = new Movimentacao();
        newMovimentacao.setConta(conta);
        newMovimentacao.setDescricao(dto.getDescricao());
        newMovimentacao.setValor(dto.getValor());
        newMovimentacao.setTipoMovimentacao(dto.getTipoMovimentacao());
        newMovimentacao.setDataMovimentacao(LocalDateTime.now());
        
        return movimentacaoRepository.save(newMovimentacao);
    }
    
    /**
     * Lista todas as movimentações de uma conta.
     * @param contaId O ID da conta.
     * @return Uma lista de movimentações.
     */
    public List<Movimentacao> listMovimentacoesPorConta(Long contaId) {
      if (!contaRepository.existsById(contaId)) {
        throw new EntityNotFoundException("Conta não encontrada com o ID: " + contaId);
      }

      return movimentacaoRepository.findByContaId(contaId);
    }
}