package br.ufrpe.biocomp.model;

import lombok.Data;

@Data
public class Sequencia {
    private String id;
    private String fasta;
    private String conteudo;

    public Sequencia(String idAtual, String fastaCompleto, String conteudoStr) {
        this.id = idAtual;
        this.fasta = fastaCompleto;
        this.conteudo = conteudoStr;
    }
}
