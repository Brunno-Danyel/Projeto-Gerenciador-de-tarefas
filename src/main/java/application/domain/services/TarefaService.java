package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.exception.UsuarioNaoEncontradoException;
import application.domain.repositories.TarefaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
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


    public void criarTarefa(TarefaDTO tarefaDto) throws MessagingException {
        Long idResponsavel = tarefaDto.getIdUsuario();
        Usuario responsavel = userService.findById(idResponsavel);

        Tarefa tarefa = fromDto(tarefaDto);
        verificarDataFDS(tarefa);
        tarefa.setResponsavel(responsavel);

        repository.save(tarefa);
        emailService.envioDeEmailComAnexo(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return repository.findAll();
    }

    public void removerTarefa(Long tarefaId) {
        if (!repository.existsById(tarefaId)) {
            throw new TarefaNaoEncontradaException("Tarefa não encontrada!");
        }
        repository.deleteById(tarefaId);
    }

    public Tarefa atualizarTarefa(Long id, TarefaDTO tarefaDTO) {
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

    public Tarefa buscarTarefaPorId(Long tarefaId) {
        return repository.findById(tarefaId).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada!"));
    }

    public List<Tarefa> buscarDescricaoTarefa(String descricao) {
        return repository.findByDescricao(descricao).orElseThrow(() -> new TarefaNaoEncontradaException("Descrição da tarefa não encontrada!"));
    }

    public List<Tarefa> buscarTituloTarefa(String titulo) {
        return repository.findByTitulo(titulo).orElseThrow(() -> new TarefaNaoEncontradaException("Título da tarefa não encontrado!"));
    }

    public List<Tarefa> buscarStatusTarefa(String status) {
        return repository.status(status);
    }

    public Optional<List<Tarefa>> buscarResponsavelTarefa(Usuario responsavel) {
        return Optional.ofNullable(repository.findByResponsavel(responsavel)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Responsavel da tarefa não encontrado!")));
    }

    public void concluirTarefa(Long tarefaId) throws MessagingException {
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

    private LocalDate verificarDataFDS(Tarefa tarefa) {

        if (tarefa.getDataPrevistaConclusao().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            LocalDate novaDataPrevista = tarefa.getDataPrevistaConclusao().plusDays(2);
            tarefa.setDataPrevistaConclusao(novaDataPrevista);
        }
        if (tarefa.getDataPrevistaConclusao().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            LocalDate novaDataPrevista = tarefa.getDataPrevistaConclusao().plusDays(1);
            tarefa.setDataPrevistaConclusao(novaDataPrevista);
        }
        return tarefa.getDataPrevistaConclusao();
    }

    public List<Tarefa> filtro(Tarefa tarefa){
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(tarefa, matcher);
        return repository.findAll(example);
    }

    public static Tarefa fromDto(TarefaDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setTitulo(dto.getTitulo());
        tarefa.setPrioridade(dto.getPrioridade());
        tarefa.setDataPrevistaConclusao(java.time.LocalDate.now().plusDays(dto.getPrazoParaConclusaoEmDias()));
        tarefa.setDeadline(java.time.LocalDate.now());
        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        return tarefa;
    }


}




