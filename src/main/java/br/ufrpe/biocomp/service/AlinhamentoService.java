package br.ufrpe.biocomp.service;

import br.ufrpe.biocomp.model.Alinhamento;
import br.ufrpe.biocomp.model.Sequencia;
import org.springframework.stereotype.Service;

@Service
public class AlinhamentoService {
    private static final int g = -2;
    private static final int match = 1;
    private static final int mismatch = -1;

    public Alinhamento NeedlemanWunsch(Sequencia  S1, Sequencia S2) {

        // Variáveis auxiliares
        final String seq1 = S1.getConteudo();
        final String seq2 = S2.getConteudo();

        final int lenS1 = S1.getConteudo().length();
        final int lenS2 = S2.getConteudo().length();

        // a matriz precisa ter tamanho = (len(sequencia) + 1) para caber o primeiro alinhamento
        // que é o vazio.
        int[][] matriz = new int[lenS1 + 1][lenS2 + 1];

        // primeira linha e primeira coluna recebem o peso do gap.
        for(int i = 0; i <= lenS1; i++) {
            matriz[i][0] = g * i;
        }

        for(int j = 0; j <= lenS2; j++) {
            matriz[0][j] = g * j;
        }

        // cria a matriz utilizando comparação de Max entre as sequências.
        for(int i = 1; i <= lenS1; i++) {
            for(int j = 1; j <= lenS2; j++) {

                int matchScore = (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? match : mismatch;

                int diagonal = matriz[i - 1][j - 1] + matchScore;
                int cima     = matriz[i - 1][j] + g;
                int esquerda = matriz[i][j - 1] + g;

                matriz[i][j] = Math.max(diagonal, Math.max(cima, esquerda));

            }
        }
        // A melhor pontuação é encotrada na última posição da matriz.
        int ScoreFinal = matriz[lenS1][lenS2];

        // Traceback do melhor alinhamento
        StringBuilder alinhamento1 = new StringBuilder();
        StringBuilder alinhamento2 = new StringBuilder();

        int i = lenS1;
        int j = lenS2;

        while(i > 0 || j > 0) {
            if (i > 0 && j > 0 && matriz[i][j] == matriz[i - 1][j - 1] + ((seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? match : mismatch)) {
                alinhamento1.append(seq1.charAt(i - 1));
                alinhamento2.append(seq2.charAt(j - 1));
                i--;
                j--;
            }
            // Se veio de cima (Gap na sequência 2)
            else if (i > 0 && matriz[i][j] == matriz[i - 1][j] + g) {
                alinhamento1.append(seq1.charAt(i - 1));
                alinhamento2.append('-');
                i--;
            }
            // Se veio da esquerda (Gap na sequência 1)
            else {
                alinhamento1.append('-');
                alinhamento2.append(seq2.charAt(j - 1));
                j--;
            }
        }

        // Como caminhamos do fim para o começo, invertemos a string aqui.
        String seq1alinhada = alinhamento1.reverse().toString();
        String seq2alinhada = alinhamento2.reverse().toString();

        Alinhamento resultado = new Alinhamento(S1, S2, ScoreFinal, seq1alinhada, seq2alinhada);

        // retorna o objeto com o melhor alinhamento entre as duas sequências.
        return resultado;

    }
}
