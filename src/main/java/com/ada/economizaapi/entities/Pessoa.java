package com.ada.economizaapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToOne
    @JoinColumn(name = "localizacao_id")
    private Localizacao localizacao;
    private Double custoPorDistancia;
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "pessoa")
    private List<ListaCompra> listasCompra = new ArrayList<>();

    public Pessoa(Long id, String nome, Localizacao localizacao, Double custoPorDistancia) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.custoPorDistancia = custoPorDistancia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(id, pessoa.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
