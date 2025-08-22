package com.xpto.xpto.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PessoaJuridicaDTO {
    // Dados da Pessoa Juridica
    private String razaoSocial;
    private String cnpj;
    private String telefone;

    // Objeto aninhado para os dados do endereço
    private EnderecoDTO endereco;

    // Objeto aninhado para os dados da conta
    private ContaDTO conta;
    
    // Dados da movimentação inicial
    private BigDecimal valorInicial;
}