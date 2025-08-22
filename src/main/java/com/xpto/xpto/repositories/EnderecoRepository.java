
package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * Busca o endereço associado a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Um Optional contendo o endereço se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Endereco> findByClienteId(Long clienteId);
}