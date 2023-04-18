package application.domain.entities;

import application.domain.enumeration.PrioridadeEnum;
import application.domain.enumeration.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "tarefa_responsavel",
            joinColumns = @JoinColumn(name = "tarefa_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private List<Usuario> responsavel = new ArrayList<>();

    @Column(name = "tb_prioridade_tarefa")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private PrioridadeEnum prioridade;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    @Column(name = "tb_data_abertura_tarefa")
    private LocalDate dataAbertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "tb_status")
    private StatusTarefa status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "tb_data_conclusao_tarefa")
    private LocalDate dataConclusao;

    @Column(name = "tb_data_prevista")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDate dataPrevistaConclusao;


}
