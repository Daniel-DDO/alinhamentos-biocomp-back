package br.ufrpe.biocomp.controller;

import br.ufrpe.biocomp.model.ResultadoMsaDTO;
import br.ufrpe.biocomp.model.Sequencia;
import br.ufrpe.biocomp.service.MsaService;
import br.ufrpe.biocomp.util.FastaParser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/msa")
@CrossOrigin(origins = "*")
public class MsaController {
    private final MsaService msaService;
    private final FastaParser fastaParser;

    public MsaController(MsaService msaService, FastaParser fastaParser) {
        this.msaService = msaService;
        this.fastaParser = fastaParser;
    }

    @PostMapping(value = "/alinhar-texto", consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> alinharTextoFasta(@RequestBody String conteudoFasta){
        try{
            String textoLimpo = conteudoFasta.replace("\"", "")
                    .replace("\\n", "\n")
                    .replace("\\r", "")
                    .trim();

            // Faz o parse do texto limpo
            List<Sequencia> sequencias = fastaParser.parseFastaString(textoLimpo);

            if(sequencias == null || sequencias.size() < 2){
                return ResponseEntity.badRequest().body("Erro de validação. O texto FASTA é inválido");
            }

            ResultadoMsaDTO resultado = msaService.alinhamentoMultiploSequencias(sequencias);
            return ResponseEntity.ok(resultado);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro interno: " +e.getMessage());
        }
    }
}
