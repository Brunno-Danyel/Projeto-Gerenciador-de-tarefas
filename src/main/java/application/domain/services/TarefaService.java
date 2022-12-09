package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.ResourceNotFoundException;
import application.domain.exception.TarefaException;
import application.domain.repositories.TarefaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;

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
        tarefa.setResponsavel(usuario);
        return repository.save(tarefa);
    }

    public List<Tarefa> listTask() {
        return repository.findAll();
    }

    public void removeTask(Long tarefaId) {
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

    public List<Tarefa> searchName(String titulo) {
        return repository.findByTitulo(titulo);
    }

    public List<Tarefa> searchStatus(String status) {
        return repository.status(status);
    }

    private void updateData(Tarefa entity, Tarefa task) {
        entity.setTitulo(task.getTitulo());
        entity.setDescricao(task.getDescricao());
        entity.setResponsavel(task.getResponsavel());
        entity.setPrioridade(task.getPrioridade());
    }

    public void concluir(Long tarefaId) {
        Tarefa task = repository.findById(tarefaId)
                .orElseThrow(() -> new TarefaException("Tarefa não encontrada - Impossível concluir"));

        task.concluirTarefa();
        task.setStatus(StatusTarefa.CONCLUIDA);
        task.setDataConclusao(OffsetDateTime.now());

        repository.save(task);

    }


}
