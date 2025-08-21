package com.xpto.xpto.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pessoas_juridicas")
@Getter
@Setter
public class PessoaJuridica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoajuridica_seq")
    @SequenceGenerator(name = "pessoajuridica_seq", sequenceName = "SEQ_PESSOA_JURIDICA", allocationSize = 1)
    private Long id;

    @Column(name = "razao_social", nullable = false, length = 200)
    private String razaoSocial;

    @Column(name = "cnpj", nullable = false, unique = true, length = 14)
    private String cnpj;
    
    @Column(name = "telefone", length = 15)
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;
}