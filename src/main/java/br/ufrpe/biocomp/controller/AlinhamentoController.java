package br.ufrpe.biocomp.controller;

import br.ufrpe.biocomp.dto.request.NeedlemanWunschRequest;
import br.ufrpe.biocomp.model.Alinhamento;
import br.ufrpe.biocomp.service.AlinhamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/alinhamento")
public class AlinhamentoController {

    @Autowired
    private AlinhamentoService alinhamentoService;

    @PostMapping("/needleman-wunsch")
    public ResponseEntity<Alinhamento> NeedlemanWunsch(@RequestBody NeedlemanWunschRequest request) {
        System.out.println("Alinhamento Needleman Wunsch\n"+LocalDateTime.now().toString()+"\n");
        return ResponseEntity.status(HttpStatus.CREATED).body(alinhamentoService.NeedlemanWunsch(request.getSequencia1(), request.getSequencia2()));
    }
}
