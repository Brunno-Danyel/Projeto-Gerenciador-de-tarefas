package application.domain.dto.model;

import application.domain.enumeration.PrioridadeEnum;
import application.domain.enumeration.StatusTarefa;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TarefaResponseDTO {


    private Long id;


    private String titulo;


    private String descricao;


    private List<UsuarioResponseDTO> responsavel;


    private PrioridadeEnum prioridade;


    private LocalDate dataAbertura;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataConclusao;


    private LocalDate dataPrevistaConclusao;


    private StatusTarefa status;

    private String organizador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDate dataUltimaAtualizacao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String usuarioAtualizacao;
}
