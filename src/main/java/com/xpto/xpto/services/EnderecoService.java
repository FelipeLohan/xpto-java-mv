package com.xpto.xpto.services;

import com.xpto.xpto.dtos.EnderecoDTO;
import com.xpto.xpto.entities.Endereco;
import com.xpto.xpto.repositories.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    /**
     * Busca o endereço associado a um ID de cliente específico.
     * @param clienteId O ID do cliente.
     * @return Um Optional contendo o endereço se encontrado, ou um Optional vazio caso contrário.
     */
    public Endereco findByClienteId(Long clienteId) {
      return enderecoRepository.findByClienteId(clienteId)
              .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado para o cliente com ID: " + clienteId));
  }
    
  /**
     * Atualiza o endereço de um cliente específico.
     * A anotação @Transactional garante que a operação seja atômica.
     * @param clienteId O ID do cliente cujo endereço será atualizado.
     * @param enderecoDTO O DTO contendo os novos dados do endereço.
     * @return A entidade Endereco após a atualização.
     */
    @Transactional
    public Endereco updateByClienteId(Long clienteId, EnderecoDTO enderecoDTO) {
        // 1. Busca o endereço existente.
        // Se não encontrar, o método findByClienteId já lançará a exceção.
        Endereco enderecoExistente = findByClienteId(clienteId);

        // 2. Atualiza os campos do objeto encontrado com os dados do DTO.
        enderecoExistente.setRua(enderecoDTO.getRua());
        enderecoExistente.setNumero(enderecoDTO.getNumero());
        enderecoExistente.setComplemento(enderecoDTO.getComplemento());
        enderecoExistente.setBairro(enderecoDTO.getBairro());
        enderecoExistente.setCidade(enderecoDTO.getCidade());
        enderecoExistente.setUf(enderecoDTO.getUf());
        enderecoExistente.setCep(enderecoDTO.getCep());

        // 3. Salva a entidade atualizada.
        return enderecoRepository.save(enderecoExistente);
    }
}