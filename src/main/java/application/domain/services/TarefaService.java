package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.ResourceNotFoundException;
import application.domain.exception.TarefaException;
import application.domain.exception.UsuarioException;
import application.domain.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private BuscaTarefaService buscaService;


    public Tarefa createdTask(TarefaDTO tarefaDto) {
        Long idUsuario = tarefaDto.getIdUsuario();
        Usuario usuario = userService.findById(tarefaDto.getIdUsuario());


        Tarefa tarefa = tarefaDto.fromDto(tarefaDto);
        if(tarefaDto.getPrioridade() == null){
            throw new TarefaException("Campo prioridade não pode ser vazio!");
        }
        tarefa.setResponsavel(usuario);
        return repository.save(tarefa);
    }

    public List<Tarefa> listTask() {
        return repository.findAll();
    }

    public void removeTask(Long tarefaId) {
        if (!repository.existsById(tarefaId)) {
            throw new TarefaException("Tarefa não encontrada!");
        }
        repository.deleteById(tarefaId);
    }

    public Tarefa updateTask(Long tarefaId, Tarefa tarefa) {
        try {
            Tarefa entity = repository.getOne(tarefaId);
            updateData(entity, tarefa);
            return repository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(tarefaId);
        }
    }

    public Tarefa findById(Long tarefaId) {
        return repository.findById(tarefaId).orElseThrow(() -> new TarefaException("Tarefa não encontrada!"));
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
                .orElseThrow(() -> new TarefaException("Usuario da tarefa não encontrado/Usuario sem tarefa atribuida")));
    }


    private void updateData(Tarefa entity, Tarefa task) {
        entity.setTitulo(task.getTitulo());
        entity.setDescricao(task.getDescricao());
        entity.setResponsavel(task.getResponsavel());
        entity.setPrioridade(task.getPrioridade());
    }

    public void concluir(Long tarefaId) {
        Tarefa tarefa = repository.findById(tarefaId)
                .orElseThrow(() -> new TarefaException("Tarefa não encontrada - Impossível concluir"));

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            throw new TarefaException("Tarefa já concluída, por favor informe outro ID");
        }
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(OffsetDateTime.now());
        repository.save(tarefa);

    }

}


