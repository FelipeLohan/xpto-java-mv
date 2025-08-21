package com.xpto.xpto.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PessoaFisicaCreateDTO {
    // Dados da Pessoa Física
    private String nome;
    private String cpf;
    private String telefone;

    // Dados da primeira conta
    private String nomeBanco;
    private String agencia;
    private String numeroConta;
    
    // Dados da movimentação inicial
    private BigDecimal valorInicial;
}