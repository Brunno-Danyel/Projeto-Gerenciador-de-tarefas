package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.dto.model.TarefaRequestDTO;
import application.domain.dto.model.TarefaResponseDTO;
import application.domain.dto.model.TarefaUpdateRequestDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;

import javax.mail.MessagingException;
import java.util.List;

public interface TarefaService {

    void criarTarefa(TarefaDTO tarefaDTO) throws MessagingException;

    void cadastrarTarefaParaTodos(TarefaRequestDTO tarefaRequestDTO) throws MessagingException;

    List<TarefaResponseDTO> listarTodasTarefas();

    void removerTarefa(Long idTarefa);

    TarefaResponseDTO atualizarTarefa(Long id, TarefaUpdateRequestDTO tarefaUpdateRequestDTO);

    TarefaResponseDTO buscarTarefaPorId(Long id);

    List<TarefaResponseDTO> buscarDescricaoTarefa(String descricao);

    List<TarefaResponseDTO> buscarTituloTarefa(String titulo);

    List<TarefaResponseDTO> buscarStatusTarefa(String status);

    List<TarefaResponseDTO> buscarResponsavelTarefa(Usuario responsavel);

    void concluirTarefa(Long tarefaId) throws MessagingException;

    List<Tarefa> filtro(Tarefa tarefa);
}
