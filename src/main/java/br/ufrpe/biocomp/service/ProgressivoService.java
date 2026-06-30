package br.ufrpe.biocomp.service;

import br.ufrpe.biocomp.model.NoFilogenetico;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgressivoService {
    private static final int g = -2;
    private static final int match = 1;
    private static final int mismatch = -1;

    public List<String> alinhamentoProgressivo(NoFilogenetico raiz){
        return alinhar(raiz);
    }

    private List<String> alinhar(NoFilogenetico no){
        // caso base da recursão
        // se é folha, retorna a lista original como 1 elemento
        if(no.isFolha()){
            List<String> elementoFolha = new ArrayList<>();
            elementoFolha.add(no.getSequencias().get(0).getConteudo());
            return elementoFolha;
        }

        // lado esquerdo
        List<String> esquerdo = alinhar(no.getFilhoEsquerdo());

        // lado direito
        List<String> direito = alinhar(no.getFilhoDireito());

        return alinharNos(esquerdo, direito);
    }

    private List<String> alinharNos(List<String> esquerdo, List<String> direito){
        int len1 = esquerdo.get(0).length();
        int len2 = direito.get(0).length();

        double[][] matriz = new double[len1 + 1][len2 + 1];

        for(int i = 1; i <= len1; i++){
            matriz[i][0] = g * i;
        }

        for(int j = 1; j <= len2; j++){
            matriz[0][j] = g * j;
        }

        for(int i = 1; i <= len1; i++){
            for(int j = 1; j <= len2; j++){
                double scoreColuna = calcularScore(esquerdo, i - 1, direito, j - 1);

                double diagonal = matriz[i - 1][j - 1] + scoreColuna;
                double cima = matriz[i - 1][j] + g; // Gap no perfil 2
                double esquerda = matriz[i][j - 1] + g; // Gap no perfil 1

                matriz[i][j] = Math.max(diagonal, Math.max(cima, esquerda));
            }
        }
        return traceback(matriz, esquerdo, direito);
    }

    private double calcularScore(List<String> perfil1, int i,List<String> perfil2, int j){
        double scoreTotal = 0.0;
        int comparacoes = 0;


        for(String seq1: perfil1){
            char c1 = seq1.charAt(i);
            for(String seq2: perfil2){
                char c2 = seq2.charAt(j);

                //Atribui a pontuação entre os caracteres
                if (c1 == '-' && c2 == '-') {
                    scoreTotal += 0;
                } else if (c1 == '-' || c2 == '-') {
                    scoreTotal += g;
                } else if (c1 == c2) {
                    scoreTotal += match;
                } else {
                    scoreTotal += mismatch;
                }
                comparacoes++;
            }
        }
        return scoreTotal / comparacoes;
    }

    private List<String> traceback(double[][] matriz, List<String> esquerdo, List<String> direito){
        int i = matriz.length - 1;
        int j = matriz[0].length - 1;

        StringBuilder[] alinhado1 =  new StringBuilder[esquerdo.size()];
        for(int k = 0; k < esquerdo.size(); k++){
            alinhado1[k] = new StringBuilder();
        }

        StringBuilder[] alinhado2 =  new StringBuilder[direito.size()];
        for(int k = 0; k < direito.size(); k++){
            alinhado2[k] = new StringBuilder();
        }

        double tolerancia = 0.0001;

        while( i > 0 || j > 0 ){
            if(i > 0 && j > 0){
                double scoreColuna = calcularScore(esquerdo, i - 1, direito, j - 1);

                // Se a nota veio da diagonal
                if(Math.abs(matriz[i][j] - (matriz[i - 1][j - 1] + scoreColuna)) < tolerancia){
                    for (int k = 0; k < esquerdo.size(); k++) {
                        alinhado1[k].append(esquerdo.get(k).charAt(i - 1));
                    }
                    for (int k = 0; k < direito.size(); k++) {
                        alinhado2[k].append(direito.get(k).charAt(j - 1));
                    }
                    i--;
                    j--;
                }

                // Verifica se veio de cima
                else if (Math.abs(matriz[i][j] - (matriz[i - 1][j] + g)) < tolerancia) {
                    for (int k = 0; k < esquerdo.size(); k++) {
                        alinhado1[k].append(esquerdo.get(k).charAt(i - 1));
                    }
                    for (int k = 0; k < direito.size(); k++) {
                        alinhado2[k].append('-');
                    }
                    i--;
                }
                // Se não foi diagonal nem cima, só pode ter vindo da esquerda
                else {
                    for (int k = 0; k < esquerdo.size(); k++) {
                        alinhado1[k].append('-');
                    }
                    for (int k = 0; k < direito.size(); k++) {
                        alinhado2[k].append(direito.get(k).charAt(j - 1));
                    }
                    j--;
                }
            }else if (i > 0) {
                // Chegou à borda superior, só pode subir (Gap na string 2)
                for (int k = 0; k < esquerdo.size(); k++) {
                    alinhado1[k].append(esquerdo.get(k).charAt(i - 1));
                }
                for (int k = 0; k < direito.size(); k++) {
                    alinhado2[k].append('-');
                }
                i--;
            } else {
                // Chegou à borda esquerda, só pode ir para a esquerda (Gap na string 1)
                for (int k = 0; k < esquerdo.size(); k++) {
                    alinhado1[k].append('-');
                }
                for (int k = 0; k < direito.size(); k++) {
                    alinhado2[k].append(direito.get(k).charAt(j - 1));
                }
                j--;
            }
        }
        List<String> resultado = new ArrayList<>();

        for(StringBuilder sb: alinhado1){
            resultado.add(sb.reverse().toString());
        }

        for(StringBuilder sb: alinhado2){
            resultado.add(sb.reverse().toString());
        }

        return resultado;
    }



}
