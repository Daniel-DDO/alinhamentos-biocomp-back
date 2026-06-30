package br.ufrpe.biocomp.service;

import br.ufrpe.biocomp.model.Alinhamento;
import br.ufrpe.biocomp.model.MatrizDistancia;
import br.ufrpe.biocomp.model.NoFilogenetico;
import br.ufrpe.biocomp.model.Sequencia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrizDistanciaService {
    private final AlinhamentoService alinhamentoService;
    private static final int MATCH_SCORE = 1;


    @Autowired
    public MatrizDistanciaService(AlinhamentoService alinhamentoService) {
        this.alinhamentoService = alinhamentoService;
    }

    // Recebe as sequencias do FASTA e cria uma matriz de pesos iniciais
    public MatrizDistancia construirMatriz(List<Sequencia> Sequencias){
        int n = Sequencias.size();
        double[][] distancias = new double[n][n];

        List<NoFilogenetico> nosIniciais = new ArrayList<>();

        for(Sequencia sequencia : Sequencias){
            nosIniciais.add(new NoFilogenetico(sequencia));
        }

        for(int i = 0; i < n; i++){
            for(int j = i; j < n; j++){

                // A distância de uma sequencia para ela mesma é 0
                if(i==j){
                    distancias[i][j] = 0.0;
                }

                else{
                    Sequencia seqI = Sequencias.get(i);
                    Sequencia seqJ = Sequencias.get(j);

                    // Alinha as duas sequencias
                    Alinhamento alinhamento = alinhamentoService.NeedlemanWunsch(seqI, seqJ);
                    int ScoreIJ = alinhamento.getScore();

                    // A pontuação do alinhamento é a pontuação máxima - a obtida
                    int MaxI  = seqI.getConteudo().length() * MATCH_SCORE;
                    int MaxJ =  seqJ.getConteudo().length() * MATCH_SCORE;
                    int maiorScore = Math.max(MaxI, MaxJ);

                    // Converte o score para distancia
                    double distancia = 0.0;
                    if(maiorScore > 0){
                        distancia = 1.0 - ((double) ScoreIJ / maiorScore);
                    }

                    // Garante que o resultado seja positivo na matriz, caso o alinhamento dê negativo
                    distancia = Math.max(0.0, distancia);
                    distancias[i][j] = distancia;
                    distancias[j][i] = distancia;

                }
            }
        }
        return new MatrizDistancia(nosIniciais, distancias);
    }
}
