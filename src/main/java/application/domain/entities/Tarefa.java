package application.domain.entities;

import application.domain.enumeration.PrioridadeEnum;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_numero_tarefa")
    private Long id;

    @Column(name = "tb_titulo_tarefa")
    private String titulo;

    @Column(name = "tb_descricao_tarefa")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "tb_id_user")
    private Usuario responsavel;

    @Column(name = "tb_prioridade_tarefa")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    @Column(name = "tb_data_tarefa")
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_status")
    private StatusTarefa status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    @Column(name = "tb_data_conclusao_tarefa")
    private LocalDate dataConclusao;

    @Column(name = "tb_data_prevista")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDate dataPrevistaConclusao;


}
