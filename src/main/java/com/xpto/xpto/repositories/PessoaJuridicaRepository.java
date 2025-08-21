package com.xpto.xpto.repositories;

import com.xpto.xpto.entities.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {
    
    /**
     * Busca uma pessoa jurídica por CNPJ.
     * @param cnpj O CNPJ da pessoa jurídica.
     * @return Uma Optional contendo a pessoa jurídica, se encontrada.
     */
    Optional<PessoaJuridica> findByCnpj(String cnpj);

    /**
     * Busca uma pessoa jurídica por ID.
     * @param id O ID da pessoa jurídica.
     * @return Uma Optional contendo a pessoa jurídica, se encontrada.
     */
    Optional<PessoaJuridica> findById(Long id);
}
