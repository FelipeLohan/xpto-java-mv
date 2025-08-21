package com.xpto.xpto.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoCreateDTO {
    private Long clienteId;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
}