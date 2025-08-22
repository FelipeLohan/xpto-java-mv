package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    /**
     * Busca a conta associado a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Um Optional contendo a conta se encontrada, ou um Optional vazio caso contrário.
     */
    Optional<Conta> findByClienteId(Long clienteId);
}