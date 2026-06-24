package br.ufrpe.biocomp.dto.request;

import br.ufrpe.biocomp.model.Sequencia;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NeedlemanWunschRequest {
    private Sequencia sequencia1;
    private Sequencia sequencia2;
}
