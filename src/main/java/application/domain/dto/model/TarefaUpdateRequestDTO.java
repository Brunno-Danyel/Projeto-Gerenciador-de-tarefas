package application.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarefaUpdateRequestDTO {

    private Long id;

    private String titulo;

    private String descricao;

    @NotNull(message = "{campo.responsavel.obrigatorio}")
    private Long idResponsavel;

    private Integer prazoParaConclusaoEmDias;

}
