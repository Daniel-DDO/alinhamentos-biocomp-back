package br.ufrpe.biocomp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResultadoMsaDTO {
    private String arvoreNewick;

    private Map<String, String> alinhamentos;
}