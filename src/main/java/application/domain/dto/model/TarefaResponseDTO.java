package application.domain.dto.model;

import application.domain.enumeration.PrioridadeEnum;
import application.domain.enumeration.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TarefaResponseDTO {


    private Long id;


    private String titulo;


    private String descricao;


    private UsuarioResponseDTO responsavel;


    private PrioridadeEnum prioridade;


    private LocalDate dataAbertura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataConclusao;


    private LocalDate dataPrevistaConclusao;


    private StatusTarefa status;
}
