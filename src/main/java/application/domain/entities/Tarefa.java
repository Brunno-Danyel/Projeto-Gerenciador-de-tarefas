package application.domain.entities;

import application.domain.enumeration.PrioridadeEnum;
import application.domain.enumeration.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    // @Column(name = "responsavel_tarefa")
    private Usuario responsavel;

    @Column(name = "tb_prioridade_tarefa")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    @Column(name = "tb_data_tarefa")
    private OffsetDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_status")
    private StatusTarefa status;

    public void concluirTarefa() {

        setStatus(StatusTarefa.CONCLUIDA);
    }
}
