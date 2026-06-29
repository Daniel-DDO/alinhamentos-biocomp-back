package br.ufrpe.biocomp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NoFilogenetico {
    private String nome;
    private List<Sequencia> sequencias;
    private double altura;
    private NoFilogenetico filhoEsquerdo;
    private NoFilogenetico filhoDireito;

    // Construtor para uma folha
    public NoFilogenetico(Sequencia sequencia) {
        this.nome = sequencia.getId();
        this.sequencias = new ArrayList<>();
        this.sequencias.add(sequencia);
        this.altura = 0.0;
        this.filhoEsquerdo = null;
        this.filhoDireito = null;
    }
    // Construtor para um nó interno do upgma
    public NoFilogenetico(String nome, NoFilogenetico filhoEsquerdo, NoFilogenetico filhoDireito, double altura) {
        this.nome = nome;
        this.altura = altura;
        this.filhoEsquerdo = filhoEsquerdo;
        this.filhoDireito = filhoDireito;

        this.sequencias = new ArrayList<>();
        if(this.filhoEsquerdo != null) {this.sequencias.addAll(this.filhoEsquerdo.sequencias);}
        if(filhoDireito != null) {this.sequencias.addAll(filhoDireito.sequencias);}

    }

    public boolean isFolha(){
        return this.filhoEsquerdo == null && this.filhoDireito == null;
    }


}
