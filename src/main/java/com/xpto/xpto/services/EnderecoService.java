package com.xpto.xpto.services;

import com.xpto.xpto.dtos.EnderecoCreateDTO;
import com.xpto.xpto.entities.Cliente;
import com.xpto.xpto.entities.Endereco;
import com.xpto.xpto.repositories.ClienteRepository;
import com.xpto.xpto.repositories.EnderecoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Endereco createEndereco(EnderecoCreateDTO dto) {
        // Busca o cliente ao qual o endereço será associado
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
          .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + dto.getClienteId()));

        // Mapeia os dados do DTO para a entidade
        Endereco newEndereco = new Endereco();
        newEndereco.setCliente(cliente);
        newEndereco.setRua(dto.getRua());
        newEndereco.setNumero(dto.getNumero());
        newEndereco.setComplemento(dto.getComplemento());
        newEndereco.setBairro(dto.getBairro());
        newEndereco.setCidade(dto.getCidade());
        newEndereco.setUf(dto.getUf());
        newEndereco.setCep(dto.getCep());
        
        return enderecoRepository.save(newEndereco);
    }

    public List<Endereco> listEnderecosByCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new EntityNotFoundException("Cliente não encontrado com o ID: " + clienteId);
        }
        return enderecoRepository.findByClienteId(clienteId);
    }
    
    @Transactional
    public void deleteEndereco(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new EntityNotFoundException("Endereço não encontrado com o ID: " + id);
        }
        enderecoRepository.deleteById(id);
    }

    // Métodos para buscar por ID e para atualizar um endereço seguiriam a mesma lógica.
}