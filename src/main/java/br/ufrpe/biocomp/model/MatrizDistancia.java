package br.ufrpe.biocomp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatrizDistancia {
    private List<NoFilogenetico> nosAtivos;

    private Double[][] matriz;

    public MatrizDistancia(List<NoFilogenetico> nosIniciais, Double matrizInicial[][]) {
        this.nosAtivos = new ArrayList<NoFilogenetico>();
        this.matriz = matrizInicial;

    }

    public int getTamanho(){
        return this.nosAtivos.size();
    }
}
