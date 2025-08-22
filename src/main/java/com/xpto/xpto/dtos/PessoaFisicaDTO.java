package com.xpto.xpto.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PessoaFisicaDTO {
    // Dados da Pessoa Física
    private String nome;
    private String cpf;
    private String telefone;

    // Objeto aninhado para os dados do endereço
    private EnderecoDTO endereco;

    // Objeto aninhado para os dados da conta
    private ContaDTO conta;
  
    // Dados da movimentação inicial
    private BigDecimal valorInicial;
}