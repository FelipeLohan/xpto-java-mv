package com.xpto.xpto.services;

import com.xpto.xpto.dtos.PessoaFisicaCreateDTO;
import com.xpto.xpto.dtos.PessoaJuridicaCreateDTO;
import com.xpto.xpto.entities.*;
import com.xpto.xpto.exceptions.BusinessLogicException;
import com.xpto.xpto.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ClienteService {

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    /**
     * Cria uma nova Pessoa Física, junto com sua primeira conta e a movimentação inicial.
     * A anotação @Transactional garante que todas as operações com o banco de dados
     * sejam executadas com sucesso, ou nenhuma delas será. É uma operação atômica.
     */
    @Transactional
    public PessoaFisica createPessoaFisica(PessoaFisicaCreateDTO dto) {
      // Regra: Valida se o CPF já existe para evitar duplicidade
      if (pessoaFisicaRepository.findByCpf(dto.getCpf()).isPresent()) {
        throw new BusinessLogicException("CPF já cadastrado no sistema.");
      }

      // 1. Cria a entidade Cliente (elo central)
      Cliente newCliente = new Cliente();
      newCliente.setTipoPessoa(TipoPessoa.PF);
      newCliente.setDataCadastro(LocalDate.now());
      Cliente clienteSalvo = clienteRepository.save(newCliente);

      // 2. Cria a entidade PessoaFisica
      PessoaFisica newPessoaFisica = new PessoaFisica();
      newPessoaFisica.setNome(dto.getNome());
      newPessoaFisica.setCpf(dto.getCpf());
      newPessoaFisica.setTelefone(dto.getTelefone());
      newPessoaFisica.setCliente(clienteSalvo);
      PessoaFisica savedPessoaFisica = pessoaFisicaRepository.save(newPessoaFisica);

      // 3. Cria a primeira Conta do cliente
      Conta newConta = new Conta();
      newConta.setCliente(clienteSalvo);
      newConta.setNomeBanco(dto.getNomeBanco());
      newConta.setAgencia(dto.getAgencia());
      newConta.setNumeroConta(dto.getNumeroConta());
      Conta contaSalva = contaRepository.save(newConta);

        // 4. Cria a Movimentação Inicial (Crédito) 
      Movimentacao newMovimentacaoInicial = new Movimentacao();
      newMovimentacaoInicial.setConta(contaSalva);
      newMovimentacaoInicial.setDescricao("Movimentação inicial de cadastro");
      newMovimentacaoInicial.setTipoMovimentacao(TipoMovimentacao.CREDITO);
      newMovimentacaoInicial.setValor(dto.getValorInicial());
      newMovimentacaoInicial.setDataMovimentacao(LocalDateTime.now());
      movimentacaoRepository.save(newMovimentacaoInicial);
        
      return savedPessoaFisica;
    }

    @Transactional
    public PessoaJuridica createPessoaJuridica(PessoaJuridicaCreateDTO dto) {
      // Regra: Valida se o CNPJ já existe para evitar duplicidade
      if (pessoaJuridicaRepository.findByCnpj(dto.getCnpj()).isPresent()) {
        throw new BusinessLogicException("CNPJ já cadastrado no sistema.");
      }

      // 1. Cria a entidade Cliente (elo central)
      Cliente newCliente = new Cliente();
      newCliente.setTipoPessoa(TipoPessoa.PJ);
      newCliente.setDataCadastro(LocalDate.now());
      Cliente clienteSalvo = clienteRepository.save(newCliente);

      // 2. Cria a entidade PessoaJuridica
      PessoaJuridica newPessoaJuridica = new PessoaJuridica();
      newPessoaJuridica.setRazaoSocial(dto.getRazaoSocial());
      newPessoaJuridica.setCnpj(dto.getCnpj());
      newPessoaJuridica.setTelefone(dto.getTelefone());
      newPessoaJuridica.setCliente(clienteSalvo);
      PessoaJuridica savedPessoaJuridica = pessoaJuridicaRepository.save(newPessoaJuridica);

      // 3. Cria a primeira Conta do cliente
      Conta newConta = new Conta();
      newConta.setCliente(clienteSalvo);
      newConta.setNomeBanco(dto.getNomeBanco());
      newConta.setAgencia(dto.getAgencia());
      newConta.setNumeroConta(dto.getNumeroConta());
      Conta contaSalva = contaRepository.save(newConta);

      // 4. Cria a Movimentação Inicial (Crédito) 
      Movimentacao newMovimentacaoInicial = new Movimentacao();
      newMovimentacaoInicial.setConta(contaSalva);
      newMovimentacaoInicial.setDescricao("Movimentação inicial de cadastro");
      newMovimentacaoInicial.setTipoMovimentacao(TipoMovimentacao.CREDITO);
      newMovimentacaoInicial.setValor(dto.getValorInicial());
      newMovimentacaoInicial.setDataMovimentacao(LocalDateTime.now());
      movimentacaoRepository.save(newMovimentacaoInicial);
        
      return savedPessoaJuridica;
    }
    
    // ... aqui viriam os métodos para atualizar, buscar e deletar clientes,
}