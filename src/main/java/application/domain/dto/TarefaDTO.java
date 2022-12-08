package application.domain.dto;

import application.domain.entities.Tarefa;
import application.domain.enumeration.PrioridadeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;


@Getter
@Setter
public class TarefaDTO {

    private Long id;

    @NotEmpty(message = "{campo.titulo.obrigatorio}")
    private String titulo;

    @NotEmpty(message = "{campo.descricao.obirgatorio}")
    private String descricao;

    @NotNull(message = "{campo.responsavel.obrigatorio}")
    private Long idUsuario;

    @NotNull(message = "{campo.prioridade.obrigatorio}")
    private PrioridadeEnum prioridade;

    private OffsetDateTime deadline;


    public static Tarefa fromDto(TarefaDTO dto) {
        Tarefa task = new Tarefa();
        task.setDescricao(dto.getDescricao());
        task.setDeadline(dto.getDeadline());
        task.setTitulo(dto.getTitulo());
        task.setPrioridade(dto.getPrioridade());

        return task;
    }
}
