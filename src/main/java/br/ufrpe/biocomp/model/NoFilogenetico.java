package br.ufrpe.biocomp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public String formatadoNewick(){
        if(isFolha()){
            return this.nome;
        }
        else{
            String esquerdo = filhoEsquerdo.formatadoNewick();
            String direito = filhoDireito.formatadoNewick();

            double distanciaEsquerdo = this.altura - this.filhoEsquerdo.getAltura();
            double distanciaDireito = this.altura - this.filhoDireito.getAltura();

            return String.format(Locale.US, "(%s:%.3f,%s:%.3f)%s",
                    esquerdo, distanciaEsquerdo,
                    direito, distanciaDireito,
                    this.nome);
        }
    }
}
