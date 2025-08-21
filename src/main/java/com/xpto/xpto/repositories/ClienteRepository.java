package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca um cliente por ID.
     * @param id O ID do cliente.
     * @return Uma Optional contendo o cliente, se encontrado.
     */
    Optional<Cliente> findById(Long id);
}
