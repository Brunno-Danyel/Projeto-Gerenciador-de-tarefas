package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.dto.TarefaDtoResponseData;
import application.domain.dto.TarefaDtoResponseDataEmAndamento;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.repositories.TarefaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    public void createdTask(TarefaDTO tarefaDto) {
        Long idUsuario = tarefaDto.getIdUsuario();
        Usuario responsavel = userService.findById(tarefaDto.getIdUsuario());


        Tarefa tarefa = tarefaDto.fromDto(tarefaDto);
        tarefa.setResponsavel(responsavel);
        repository.save(tarefa);
    }

    public List<Tarefa> listTask() {
        return repository.findAll();
    }

    public List<TarefaDtoResponseData> listarTarefas(){
        return repository.findAll().stream().map(tarefa -> {
            if(tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)){
                return modelMapper.map(tarefa, TarefaDtoResponseData.class);
            }
            return modelMapper.map(tarefa, TarefaDtoResponseData.class);
        }).collect(Collectors.toList());
    }

    public void removeTask(Long tarefaId) {
        if (!repository.existsById(tarefaId)) {
            throw new TarefaNaoEncontradaException("Tarefa não encontrada!");
        }
        repository.deleteById(tarefaId);
    }

    public Tarefa atualizandoTarefa(Long id, TarefaDTO tarefaDTO) {
        return repository.findById(id).map(tarefa -> {
            Long idResponsavel = tarefaDTO.getIdUsuario();
            Usuario responsavel = userService.findById(tarefaDTO.getIdUsuario());

            tarefa.setTitulo(tarefaDTO.getTitulo());
            tarefa.setDescricao(tarefaDTO.getDescricao());
            tarefa.setPrioridade(tarefaDTO.getPrioridade());
            tarefa.setResponsavel(responsavel);
            return tarefa;
        }).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada!"));
    }

    public Tarefa findById(Long tarefaId) {
        return repository.findById(tarefaId).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada!"));
    }

    public List<Tarefa> searchDescription(String descricao) {
        return repository.findByDescricao(descricao);
    }

    public List<Tarefa> searchTitle(String titulo) {
        return repository.findByTitulo(titulo);
    }

    public List<Tarefa> searchStatus(String status) {
        return repository.status(status);
    }

    public Optional<List<Tarefa>> searchResponsavel(Usuario responsavel) {
        return Optional.ofNullable(repository.findByResponsavel(responsavel)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Responsavel da tarefa não encontrado/Usuario sem tarefa atribuida")));
    }


    public void concluir(Long tarefaId) {
        Tarefa tarefa = repository.findById(tarefaId)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada - Impossível concluir"));

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            throw new TarefaException("Tarefa já concluída, por favor informe outro ID");
        }
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(OffsetDateTime.now());
        repository.save(tarefa);

    }

}


