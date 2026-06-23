package br.ufrpe.biocomp.model;

import lombok.Data;

@Data
public class Alinhamento {
    private Sequencia S1;
    private Sequencia S2;
    private int score;

    private String sequenciaAlinhada1;
    private String sequenciaAlinhada2;

    public Alinhamento(Sequencia seq1, Sequencia seq2,int Score, String seqAlinhada1, String seqAlinhada2) {
        this.S1 = seq1;
        this.S2 = seq2;
        this.score = Score;
        this.sequenciaAlinhada1 = seqAlinhada1;
        this.sequenciaAlinhada2 = seqAlinhada2;
    }
}
