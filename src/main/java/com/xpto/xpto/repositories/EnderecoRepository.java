
package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * Busca todos os endereços associados a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Uma lista de endereços do cliente.
     */
    List<Endereco> findByClienteId(Long clienteId);
}