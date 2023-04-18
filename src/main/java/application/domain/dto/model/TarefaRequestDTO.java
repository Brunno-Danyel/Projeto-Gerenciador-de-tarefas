package application.domain.dto.model;

import application.domain.enumeration.PrioridadeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class TarefaRequestDTO {

    private Long id;

    @NotEmpty(message = "{campo.titulo.obrigatorio}")
    private String titulo;

    @NotEmpty(message = "{campo.descricao.obirgatorio}")
    private String descricao;

    @NotNull(message = "{campo.prioridade.obrigatorio}")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PrioridadeEnum prioridade;

    @NotNull(message = "{campo.dataPrevista.obrigatorio}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDate dataPrevistaConclusao;
}
