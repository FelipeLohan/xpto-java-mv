package com.xpto.xpto.services;

import com.xpto.xpto.entities.Conta;
import com.xpto.xpto.exceptions.BusinessLogicException;
import com.xpto.xpto.repositories.ContaRepository;
import com.xpto.xpto.repositories.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Realiza a exclusão lógica de uma conta.
     * A conta é inativada ao invés de ser removida do banco de dados.
     * @param id O ID da conta a ser inativada.
     */
    public void deleteLogicaConta(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException("Conta não encontrada com o ID: " + id));

        conta.setAtivo(false);
        contaRepository.save(conta);
    }

    /**
     * Atualiza os dados de uma conta, aplicando a regra de negócio do desafio.
     * @param id O ID da conta a ser atualizada.
     * @param contaAtualizada O objeto Conta com as novas informações.
     * @return A conta após a atualização.
     */
    public Conta updateConta(Long id, Conta contaAtualizada) {
        // Regra: "caso exista alguma movimentação associada, não permitir alteração" [cite: 36]
        boolean hasMovimentacao = !movimentacaoRepository.findByContaId(id).isEmpty();

        if (hasMovimentacao) {
            throw new BusinessLogicException("Não é permitido alterar contas que já possuem movimentações.");
        }

        Conta contaExists = contaRepository.findById(id)
          .orElseThrow(() -> new BusinessLogicException("Conta não encontrada com o ID: " + id));

        // Atualiza apenas os campos permitidos
        contaExists.setNomeBanco(contaAtualizada.getNomeBanco());
        contaExists.setAgencia(contaAtualizada.getAgencia());
        contaExists.setNumeroConta(contaAtualizada.getNumeroConta());

        return contaRepository.save(contaExists);
    }
}