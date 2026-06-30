package br.ufrpe.biocomp.service;

import br.ufrpe.biocomp.model.*;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MsaService {
    private final ProgressivoService progressivoService;
    private final UpgmaService upgmaService;
    private final AlinhamentoService alinhamentoService;
    private final MatrizDistanciaService matrizDistanciaService;

    public MsaService(ProgressivoService progressivoService, UpgmaService upgmaService, AlinhamentoService alinhamentoService, MatrizDistanciaService matrizDistanciaService) {
        this.progressivoService = progressivoService;
        this.upgmaService = upgmaService;
        this.alinhamentoService = alinhamentoService;
        this.matrizDistanciaService = matrizDistanciaService;

    }

    public ResultadoMsaDTO alinhamentoMultiploSequencias(List<Sequencia> sequencias){
        if(sequencias == null || sequencias.isEmpty() || sequencias.size() < 2){
            throw new IllegalArgumentException("É necessário pelo menos 2 sequencias.");
        }

        System.out.println("Calculando matriz de distância: ");
        MatrizDistancia matrizInicial = matrizDistanciaService.construirMatriz(sequencias);
        System.out.println("[SUCESSO] construçao da matriz bem sucedida");

        System.out.println("Construindo árvore filogenética: ");
        NoFilogenetico raiz = upgmaService.construirArvore(matrizInicial);
        System.out.println("[SUCESSO] construção da árevore bem sucedida");

        System.out.println("Fazendo alinhamento Progressivo: ");
        List<String> alinhamentoFinal = progressivoService.alinhamentoProgressivo(raiz);
        System.out.println("[SUCESSO] Construção do alinhamento bem sucedido.");

        List<Sequencia> sequenciasOrdernadas = raiz.getSequencias();
        Map<String, String> resultado = new LinkedHashMap<>();

        for(int i = 0; i < alinhamentoFinal.size(); i++){
            String identificador = sequenciasOrdernadas.get(i).getId();
            String sequenciaGaps = alinhamentoFinal.get(i);
            resultado.put(identificador, sequenciaGaps);
        }
        String arvore = raiz.formatadoNewick() + ";";

        System.out.println("[SUCESSO] alinhamento múltiplo concluido com sucesso.");
        System.out.println("Ávore gerada: " + arvore);
        return new ResultadoMsaDTO(arvore, resultado);

    }
}
