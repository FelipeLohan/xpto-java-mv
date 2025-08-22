package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    /**
     * Busca todas as movimentações de um cliente em um período específico.
     * A query faz a junção (JOIN) de Movimentacao com Conta para filtrar pelo ID do cliente.
     * @param clienteId O ID do cliente.
     * @param inicio A data/hora de início do período.
     * @param fim A data/hora de fim do período.
     * @return Uma lista de movimentações do cliente no período.
     */
    @Query("SELECT m FROM Movimentacao m JOIN m.conta c WHERE c.cliente.id = :clienteId AND m.dataMovimentacao BETWEEN :inicio AND :fim")
    List<Movimentacao> findMovimentacoesByClienteAndPeriodo(@Param("clienteId") Long clienteId, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    /**
     * Busca a primeira movimentação de um cliente, ordenada pela data.
     * Essencial para encontrar o "Saldo Inicial" do relatório.
     * A cláusula 'LIMIT 1' é específica do HQL (Hibernate) e pode não ser portável para todos os provedores JPA, mas é comum em projetos Spring/Hibernate.
     * @param clienteId O ID do cliente.
     * @return Um Optional contendo a primeira movimentação, se existir.
     */
    @Query(value = "SELECT m FROM Movimentacao m JOIN m.conta c WHERE c.cliente.id = :clienteId ORDER BY m.dataMovimentacao ASC LIMIT 1")
    Optional<Movimentacao> findFirstMovimentacaoByClienteId(@Param("clienteId") Long clienteId);
    
    /**
     * Calcula o saldo total atual de um cliente.
     * Utiliza uma expressão CASE para somar valores de CRÉDITO e subtrair valores de DÉBITO.
     * A função COALESCE garante que, se não houver movimentações, o resultado seja 0 em vez de NULL.
     * @param clienteId O ID do cliente.
     * @return O saldo total do cliente como um BigDecimal.
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN m.tipoMovimentacao = 'CREDITO' THEN m.valor ELSE -m.valor END), 0.0) FROM Movimentacao m JOIN m.conta c WHERE c.cliente.id = :clienteId")
    BigDecimal calcularSaldoTotalCliente(@Param("clienteId") Long clienteId);
}