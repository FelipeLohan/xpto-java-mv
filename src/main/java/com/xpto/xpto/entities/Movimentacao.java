package com.xpto.xpto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "movimentacoes")
@Getter
@Setter
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimentacao_seq")
    @SequenceGenerator(name = "movimentacao_seq", sequenceName = "SEQ_MOVIMENTACAO", allocationSize = 1)
    private Long id;
    
    @Column(name = "descricao", nullable = false, length = 255)
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", nullable = false)
    @JsonBackReference
    private Conta conta;
}