package application.domain.dto.model;

import application.domain.enumeration.PrioridadeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarefaUpdateRequestDTO {

    private Long id;

    private String titulo;

    private String descricao;

    private Long idResponsavel;

    private PrioridadeEnum prioridade;

    private Integer prazoParaConclusaoEmDias;

}
