package com.xpto.xpto.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contas")
@Getter
@Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    @SequenceGenerator(name = "conta_seq", sequenceName = "SEQ_CONTA", allocationSize = 1)
    private Long id;

    @Column(name = "nome_banco", nullable = false)
    private String nomeBanco;

    @Column(name = "agencia", nullable = false, length = 10)
    private String agencia;

    @Column(name = "numero_conta", nullable = false, length = 20)
    private String numeroConta;
    
    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @JsonBackReference
    private Cliente cliente;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Movimentacao> movimentacoes;
}