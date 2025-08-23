package com.xpto.xpto.services;

import com.xpto.xpto.dtos.ContaDTO;
import com.xpto.xpto.entities.Conta;
import com.xpto.xpto.exceptions.BusinessLogicException;
import com.xpto.xpto.repositories.ContaRepository;
import com.xpto.xpto.repositories.MovimentacaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    /**
     * 1. Busca a conta de um cliente específico pelo ID do cliente.
     * @param clienteId O ID do cliente.
     * @return A entidade Conta encontrada.
     * @throws EntityNotFoundException se nenhuma conta for encontrada para o cliente.
     */
    public Conta findByClienteId(Long clienteId) {
        return contaRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada para o cliente com ID: " + clienteId));
    }

    /**
     * 2. Desativa uma conta (exclusão lógica), alterando seu status para inativo.
     * @param contaId O ID da conta a ser desativada.
     * @return A entidade Conta após a desativação.
     */
    @Transactional
    public Conta desativarConta(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com o ID: " + contaId));

        conta.setAtivo(false);
        return contaRepository.save(conta);
    }

    /**
     * 3. Ativa uma conta que foi desativada anteriormente.
     * @param contaId O ID da conta a ser ativada.
     * @return A entidade Conta após a ativação.
     */
    @Transactional
    public Conta ativarConta(Long contaId) {
        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com o ID: " + contaId));

        conta.setAtivo(true);
        return contaRepository.save(conta);
    }

    /**
     * 4. Atualiza os dados da conta de um cliente específico.
     * A conta é localizada pelo ID do cliente.
     * @param clienteId O ID do cliente cuja conta será atualizada.
     * @param contaDTO O DTO com as novas informações da conta.
     * @return A entidade Conta após a atualização.
     * @throws BusinessLogicException se a conta já possuir movimentações.
     */
    @Transactional
    public Conta updateByClienteId(Long clienteId, ContaDTO contaDTO) {
        // Primeiro, busca a conta usando o ID do cliente
        Conta contaExistente = findByClienteId(clienteId);

        boolean hasMovimentacao = !movimentacaoRepository.findByContaId(contaExistente.getId()).isEmpty();

        if (hasMovimentacao) {
            throw new BusinessLogicException("Não é permitido alterar contas que já possuem movimentações.");
        }

        // Atualiza os campos permitidos
        contaExistente.setNomeBanco(contaDTO.getNomeBanco());
        contaExistente.setAgencia(contaDTO.getAgencia());
        contaExistente.setNumeroConta(contaDTO.getNumeroConta());

        return contaRepository.save(contaExistente);
    }
}