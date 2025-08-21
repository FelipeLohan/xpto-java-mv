package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    /**
     * Busca todas as contas associadas a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Uma lista de contas do cliente.
     */
    List<Conta> findByClienteId(Long clienteId);
}