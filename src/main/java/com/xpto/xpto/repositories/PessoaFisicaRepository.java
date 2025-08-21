package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    /**
     * Busca uma pessoa física por ID.
     * @param id O ID da pessoa física.
     * @return Uma Optional contendo a pessoa física, se encontrada.
     */
    Optional<PessoaFisica> findById(Long id);

    /**
     * Busca uma pessoa física por CPF.
     * @param cpf O CPF da pessoa física.
     * @return Uma Optional contendo a pessoa física, se encontrada.
     */
    Optional<PessoaFisica> findByCpf(String cpf);
}
