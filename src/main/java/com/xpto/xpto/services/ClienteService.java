package com.xpto.xpto.services;

import com.xpto.xpto.dtos.PessoaFisicaDTO; 
import com.xpto.xpto.dtos.PessoaJuridicaDTO; 
import com.xpto.xpto.entities.*;
import com.xpto.xpto.exceptions.BusinessLogicException;
import com.xpto.xpto.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public PessoaFisica createPessoaFisica(PessoaFisicaDTO dto) {
        // Regra: Valida se o CPF já existe para evitar duplicidade
        if (pessoaFisicaRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new BusinessLogicException("CPF já cadastrado no sistema.");
        }

        // 1. Cria a entidade Endereco a partir do DTO aninhado
        Endereco newEndereco = new Endereco();
        newEndereco.setRua(dto.getEndereco().getRua());
        newEndereco.setNumero(dto.getEndereco().getNumero());
        newEndereco.setComplemento(dto.getEndereco().getComplemento());
        newEndereco.setBairro(dto.getEndereco().getBairro());
        newEndereco.setCidade(dto.getEndereco().getCidade());
        newEndereco.setUf(dto.getEndereco().getUf());
        newEndereco.setCep(dto.getEndereco().getCep());

        // 2. Cria a entidade Conta a partir do DTO aninhado
        Conta newConta = new Conta();
        newConta.setNomeBanco(dto.getConta().getNomeBanco());
        newConta.setAgencia(dto.getConta().getAgencia());
        newConta.setNumeroConta(dto.getConta().getNumeroConta());

        // 3. Cria a entidade Cliente e associa o endereço e a conta
        Cliente newCliente = new Cliente();
        newCliente.setTipoPessoa(TipoPessoa.PF);
        newCliente.setDataCadastro(LocalDate.now());
        newCliente.setEndereco(newEndereco);
        newCliente.setConta(newConta);
        
        // Associa o cliente de volta para manter a bidirecionalidade
        newEndereco.setCliente(newCliente);
        newConta.setCliente(newCliente);

        // 4. Cria a entidade PessoaFisica e associa o Cliente
        PessoaFisica newPessoaFisica = new PessoaFisica();
        newPessoaFisica.setNome(dto.getNome());
        newPessoaFisica.setCpf(dto.getCpf());
        newPessoaFisica.setTelefone(dto.getTelefone());
        newPessoaFisica.setCliente(newCliente);

        // 5. Salva a PessoaFisica. O JPA (com Cascade) salvará Cliente, Endereco e Conta automaticamente.
        PessoaFisica savedPessoaFisica = pessoaFisicaRepository.save(newPessoaFisica);

        Movimentacao newMovimentacaoInicial = new Movimentacao();
        // A conta salva pode ser acessada através do grafo de objetos
        newMovimentacaoInicial.setConta(savedPessoaFisica.getCliente().getConta());
        newMovimentacaoInicial.setDescricao("Movimentação inicial de cadastro");
        newMovimentacaoInicial.setTipoMovimentacao(TipoMovimentacao.CREDITO);
        newMovimentacaoInicial.setValor(dto.getValorInicial());
        newMovimentacaoInicial.setDataMovimentacao(LocalDateTime.now());
        movimentacaoRepository.save(newMovimentacaoInicial);
        
        return savedPessoaFisica;
    }

    @Transactional
    public PessoaJuridica createPessoaJuridica(PessoaJuridicaDTO dto) {
        if (pessoaJuridicaRepository.findByCnpj(dto.getCnpj()).isPresent()) {
            throw new BusinessLogicException("CNPJ já cadastrado no sistema.");
        }

        // A lógica é idêntica, apenas adaptada para Pessoa Jurídica
        Endereco newEndereco = new Endereco();
        newEndereco.setRua(dto.getEndereco().getRua());
        newEndereco.setNumero(dto.getEndereco().getNumero());
        newEndereco.setBairro(dto.getEndereco().getBairro());
        newEndereco.setCidade(dto.getEndereco().getCidade());
        newEndereco.setUf(dto.getEndereco().getUf());
        newEndereco.setCep(dto.getEndereco().getCep());

        Conta newConta = new Conta();
        newConta.setNomeBanco(dto.getConta().getNomeBanco());
        newConta.setAgencia(dto.getConta().getAgencia());
        newConta.setNumeroConta(dto.getConta().getNumeroConta());

        Cliente newCliente = new Cliente();
        newCliente.setTipoPessoa(TipoPessoa.PJ);
        newCliente.setDataCadastro(LocalDate.now());
        newCliente.setEndereco(newEndereco);
        newCliente.setConta(newConta);
        
        newEndereco.setCliente(newCliente);
        newConta.setCliente(newCliente);

        PessoaJuridica newPessoaJuridica = new PessoaJuridica();
        newPessoaJuridica.setRazaoSocial(dto.getRazaoSocial());
        newPessoaJuridica.setCnpj(dto.getCnpj());
        newPessoaJuridica.setTelefone(dto.getTelefone());
        newPessoaJuridica.setCliente(newCliente);

        PessoaJuridica savedPessoaJuridica = pessoaJuridicaRepository.save(newPessoaJuridica);

        Movimentacao newMovimentacaoInicial = new Movimentacao();
        newMovimentacaoInicial.setConta(savedPessoaJuridica.getCliente().getConta());
        newMovimentacaoInicial.setDescricao("Movimentação inicial de cadastro");
        newMovimentacaoInicial.setTipoMovimentacao(TipoMovimentacao.CREDITO);
        newMovimentacaoInicial.setValor(dto.getValorInicial());
        newMovimentacaoInicial.setDataMovimentacao(LocalDateTime.now());
        movimentacaoRepository.save(newMovimentacaoInicial);
        
        return savedPessoaJuridica;
    }
}