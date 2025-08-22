package com.xpto.xpto.dtos;

import com.xpto.xpto.entities.TipoMovimentacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MovimentacaoDTO {
    private Long contaId;
    private String descricao;
    private BigDecimal valor;
    private TipoMovimentacao tipoMovimentacao;
}