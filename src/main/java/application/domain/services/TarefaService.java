package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.repositories.TarefaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;


    public void createdTask(TarefaDTO tarefaDto) throws MessagingException {
        Long idResponsavel = tarefaDto.getIdUsuario();
        Usuario responsavel = userService.findById(idResponsavel);

        Tarefa tarefa = fromDto(tarefaDto);
        verificarDataFDS(tarefa);
        tarefa.setResponsavel(responsavel);

        repository.save(tarefa);
        emailService.envioDeEmailComAnexo(tarefa);
    }

    public List<Tarefa> listTask() {
        return repository.findAll();
    }

    public void removeTask(Long tarefaId) {
        if (!repository.existsById(tarefaId)) {
            throw new TarefaNaoEncontradaException("Tarefa não encontrada!");
        }
        repository.deleteById(tarefaId);
    }

    public Tarefa atualizandoTarefa(Long id, TarefaDTO tarefaDTO) {
        return repository.findById(id).map(tarefa -> {
            if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
                throw new TarefaException("Impossível atualizar tarefas já CONCLUÍDAS!");
            }
            Long idResponsavel = tarefaDTO.getIdUsuario();
            Usuario responsavel = userService.findById(idResponsavel);

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
        return repository.findByDescricao(descricao).orElseThrow(() -> new TarefaNaoEncontradaException("Descrição da tarefa não encontrada!"));
    }

    public List<Tarefa> searchTitle(String titulo) {
        return repository.findByTitulo(titulo).orElseThrow(() -> new TarefaNaoEncontradaException("Título da tarefa não encontrado!"));
    }

    public List<Tarefa> searchStatus(String status) {
        return repository.status(status);
    }

    public Optional<List<Tarefa>> searchResponsavel(Usuario responsavel) {
        return Optional.ofNullable(repository.findByResponsavel(responsavel)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Responsavel da tarefa não encontrado/Usuario sem tarefa atribuida")));
    }


    public void concluir(Long tarefaId) throws MessagingException {
        Tarefa tarefa = repository.findById(tarefaId)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada - Impossível concluir"));

        if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            throw new TarefaException("Tarefa já concluída, por favor informe outro ID");
        }
        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(LocalDate.now());
        repository.save(tarefa);
        emailService.envioDeEmailTarefaConcluidaComAnexo(tarefa);
    }
    private LocalDate verificarDataFDS(Tarefa tarefa){

        if(tarefa.getDataPrevistaConclusao().getDayOfWeek().equals(DayOfWeek.SATURDAY)){
           LocalDate novaDataPrevista =  tarefa.getDataPrevistaConclusao().plusDays(2);
           tarefa.setDataPrevistaConclusao(novaDataPrevista);
        }
         if(tarefa.getDataPrevistaConclusao().getDayOfWeek().equals(DayOfWeek.SUNDAY)){
             LocalDate novaDataPrevista =  tarefa.getDataPrevistaConclusao().plusDays(2);
             tarefa.setDataPrevistaConclusao(novaDataPrevista);
        }
        return tarefa.getDataPrevistaConclusao();
    }

    public static Tarefa fromDto(TarefaDTO dto) {
        Tarefa task = new Tarefa();
        task.setDescricao(dto.getDescricao());
        task.setTitulo(dto.getTitulo());
        task.setPrioridade(dto.getPrioridade());
        task.setDataPrevistaConclusao(java.time.LocalDate.now().plusDays(dto.getPrazoParaConclusaoEmDias()));
        task.setDeadline(java.time.LocalDate.now());
        task.setStatus(StatusTarefa.EM_ANDAMENTO);
        return task;
    }

}


