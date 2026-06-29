package br.ufrpe.biocomp.util;

import br.ufrpe.biocomp.model.Sequencia;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilitário responsável por ler textos no formato FASTA e converter
 * numa lista de objetos Sequencia para o algoritmo processar.
 */
@Component
public class FastaParser {

    public List<Sequencia> parseFastaString(String conteudoFasta) throws Exception {
        List<Sequencia> sequencias = new ArrayList<>();

        if (conteudoFasta == null || conteudoFasta.trim().isEmpty()) {
            return sequencias;
        }

        try (BufferedReader reader = new BufferedReader(new StringReader(conteudoFasta))) {
            String linha;
            String idAtual = null;
            StringBuilder sequenciaAtual = new StringBuilder();

            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                if (linha.startsWith(">")) {
                    // Guarda a sequência anterior (se existir) antes de começar uma nova
                    if (idAtual != null && sequenciaAtual.length() > 0) {

                        String conteudoStr = sequenciaAtual.toString();
                        // Reconstrói a representação original do FASTA para aquela sequência
                        String fastaCompleto = ">" + idAtual + "\n" + conteudoStr;

                        // Passa os 3 parâmetros exigidos pela classe Sequencia
                        sequencias.add(new Sequencia(idAtual, fastaCompleto, conteudoStr));

                        sequenciaAtual.setLength(0); // Limpa o buffer
                    }
                    // O novo ID é a linha sem o caractere '>'
                    idAtual = linha.substring(1).trim();
                } else {
                    // Converte para maiúsculas para manter a consistência no alinhamento
                    sequenciaAtual.append(linha.toUpperCase());
                }
            }

            // Não esquecer de guardar a última sequência lida no ficheiro
            if (idAtual != null && sequenciaAtual.length() > 0) {
                String conteudoStr = sequenciaAtual.toString();
                String fastaCompleto = ">" + idAtual + "\n" + conteudoStr;

                sequencias.add(new Sequencia(idAtual, fastaCompleto, conteudoStr));
            }
        }

        return sequencias;
    }
}