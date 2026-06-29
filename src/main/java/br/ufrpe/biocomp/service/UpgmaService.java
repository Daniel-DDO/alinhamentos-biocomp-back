package br.ufrpe.biocomp.service;

import br.ufrpe.biocomp.model.MatrizDistancia;
import br.ufrpe.biocomp.model.NoFilogenetico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpgmaService {

    private List<List<Double>> converterArrayParaLista(double[][] array) {
        List<List<Double>> lista = new ArrayList<>();
        for (double[] linhaArray : array) {
            List<Double> linhaLista = new ArrayList<>();
            for (double valor : linhaArray) {
                linhaLista.add(valor);
            }
            lista.add(linhaLista);
        }
        return lista;
    }

    /**
     * Executa o algoritmo UPGMA até que reste apenas o nó raiz da árvore filogenética.
     */
    public NoFilogenetico construirArvore(MatrizDistancia matrizInicial){
        int contador = 1;

        List<NoFilogenetico> nosAtivos = new ArrayList<>(matrizInicial.getNosAtivos());
        List<List<Double>> distancias = converterArrayParaLista(matrizInicial.getMatriz());

        // O algoritmo roda até sobrar somente um único nó
        while(nosAtivos.size() > 1){
            double menorDistancia = Double.MAX_VALUE;
            int index1 = -1;
            int index2 = -1;
            int n = nosAtivos.size();

            // Busca o par com menor distância na matriz
            for(int i = 0; i < n; i++){
                for(int j = i + 1; j < n; j++){ // Evita a diagonal
                    if(distancias.get(i).get(j) < menorDistancia){
                        menorDistancia = distancias.get(i).get(j);
                        index1 = i;
                        index2 = j;
                    }
                }
            }

            if (index1 == -1 || index2 == -1) break; // Trava de segurança

            // Cria os nós filhos
            NoFilogenetico filho1 = nosAtivos.get(index1);
            NoFilogenetico filho2 = nosAtivos.get(index2);
            double altura = menorDistancia / 2.0;

            String nomeCluster = "U" + contador++;
            NoFilogenetico novoCluster = new NoFilogenetico(nomeCluster, filho1, filho2, altura);

            List<NoFilogenetico> novosNosAtivos = new ArrayList<>();
            List<List<Double>> novasDistancias = new ArrayList<>();

            // Mantém os nós que não foram agrupados nesta iteração
            for (int i = 0; i < n; i++) {
                if (i != index1 && i != index2) {
                    novosNosAtivos.add(nosAtivos.get(i));
                }
            }
            // Adiciona o novo cluster no final
            novosNosAtivos.add(novoCluster);

            // Prepara a nova matriz preenchida com zeros
            int novoTamanho = novosNosAtivos.size();
            for (int i = 0; i < novoTamanho; i++) {
                List<Double> linhaVazia = new ArrayList<>();
                for (int j = 0; j < novoTamanho; j++) linhaVazia.add(0.0);
                novasDistancias.add(linhaVazia);
            }

            //  Calcula e preenche as distâncias da nova matriz
            int indiceNovoCluster = novoTamanho - 1;
            for (int i = 0; i < novoTamanho; i++) {
                for (int j = i + 1; j < novoTamanho; j++) {
                    double dist;

                    if (j == indiceNovoCluster) {
                        // Recalcula a distância ponderada do novo cluster para os restantes
                        int indexOriginalI = nosAtivos.indexOf(novosNosAtivos.get(i));
                        double distanciaFilho1 = distancias.get(indexOriginalI).get(index1);
                        double distanciaFilho2 = distancias.get(indexOriginalI).get(index2);

                        int peso1 = filho1.getSequencias().size();
                        int peso2 = filho2.getSequencias().size();
                        int pesoTotal = novoCluster.getSequencias().size();

                        dist = ((distanciaFilho1 * peso1) + (distanciaFilho2 * peso2)) / pesoTotal;
                    } else {
                        // Preserva a distância entre os nós que não foram alterados
                        int indexOriginalI = nosAtivos.indexOf(novosNosAtivos.get(i));
                        int indexOriginalJ = nosAtivos.indexOf(novosNosAtivos.get(j));
                        dist = distancias.get(indexOriginalI).get(indexOriginalJ);
                    }

                    // a distância entre nós é simétrica
                    novasDistancias.get(i).set(j, dist);
                    novasDistancias.get(j).set(i, dist);
                }
            }

            // Atualiza o estado para o próximo laço do while
            nosAtivos = novosNosAtivos;
            distancias = novasDistancias;
        }

        return nosAtivos.get(0);
    }
}