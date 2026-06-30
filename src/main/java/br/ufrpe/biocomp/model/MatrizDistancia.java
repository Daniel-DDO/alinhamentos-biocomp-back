package br.ufrpe.biocomp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatrizDistancia {
    private List<NoFilogenetico> nosAtivos;

    private double[][] matriz;

    public MatrizDistancia(List<NoFilogenetico> nosIniciais, double matrizInicial[][]) {
        this.nosAtivos = nosIniciais;
        this.matriz = matrizInicial;

    }

    public int getTamanho(){
        return this.nosAtivos.size();
    }
}
