package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    /**
     * Busca todas as movimentações de uma conta específica.
     * @param contaId O ID da conta.
     * @return Uma lista de movimentações da conta.
     */
    List<Movimentacao> findByContaId(Long contaId);

    /**
     * Busca movimentações de uma conta dentro de um período específico.
     * Útil para os relatórios.
     * @param contaId O ID da conta.
     * @param dataInicio A data inicial do período.
     * @param dataFim A data final do período.
     * @return Uma lista de movimentações no período especificado.
     */
    List<Movimentacao> findByContaIdAndDataMovimentacaoBetween(Long contaId, LocalDateTime dataInicio, LocalDateTime dataFim);
}