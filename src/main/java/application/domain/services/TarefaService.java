package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.dto.model.TarefaUpdateRequestDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.exception.UsuarioNaoEncontradoException;
import application.domain.repositories.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    public void criarTarefa(TarefaDTO tarefaRequestDto) throws MessagingException {
        Long idResponsavel = tarefaRequestDto.getIdResponsavel();
        Usuario responsavel = userService.buscarUsuarioPorId(idResponsavel);


        Tarefa tarefa = fromDto(tarefaRequestDto);
        verificarData(tarefa);
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

    public Tarefa atualizarTarefa(Long id, @Valid TarefaUpdateRequestDTO tarefaDTO) {
        return repository.findById(id).map(tarefa -> {
            if (tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
                throw new TarefaException("Impossível atualizar tarefas já CONCLUÍDAS!");
            }
            Long idResponsavel = tarefaDTO.getIdResponsavel();
            Usuario responsavel = userService.buscarUsuarioPorId(idResponsavel);

            String novoTitulo = tarefaDTO.getTitulo().isEmpty() ? tarefa.getTitulo() : tarefaDTO.getTitulo();
            String novaDescricao = tarefaDTO.getDescricao().isEmpty() ? tarefa.getDescricao() : tarefaDTO.getDescricao();
            LocalDate novaDataPrevistaConclusao = tarefaDTO.getDataPrevistaConclusao() == null ? tarefa.getDataPrevistaConclusao() : tarefaDTO.getDataPrevistaConclusao();

            tarefa.setTitulo(novoTitulo);
            tarefa.setDescricao(novaDescricao);
            tarefa.setDataPrevistaConclusao(novaDataPrevistaConclusao);
            tarefa.setResponsavel(responsavel);
            return repository.save(tarefa);
        }).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada!"));
    }

    public Tarefa buscarTarefaPorId(Long tarefaId) {
        return repository.findById(tarefaId).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa ID:" + tarefaId +" não encontrada!"));
    }

    public List<Tarefa> buscarDescricaoTarefa(String descricao) {
        List<Tarefa> listaDeDescricao = repository.findByDescricao(descricao);
        if (listaDeDescricao.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com essa DESCRIÇÃO no momento!");

        }
        return listaDeDescricao;
    }

    public List<Tarefa> buscarTituloTarefa(String titulo) {
        List<Tarefa> listaDeTitulos = repository.findByTitulo(titulo);
        if (listaDeTitulos.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com esse TÍTULO no momento!");
        }
        return listaDeTitulos;
    }

    public List<Tarefa> buscarStatusTarefa(String status) {
        List<Tarefa> tarefasStatus = repository.status(status);
        if (tarefasStatus.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com esse STATUS no momento!");
        }
        return tarefasStatus;
    }

    public List<Tarefa> buscarResponsavelTarefa(Usuario responsavel) {
         if(responsavel == null){
             throw new UsuarioNaoEncontradoException("Responsável não encontrado!");
         }
         List<Tarefa> tarefas = repository.findByResponsavel(responsavel);
         if(tarefas == null || tarefas.isEmpty()){
             throw new TarefaNaoEncontradaException("Esse responsável não possui tarefas!");
         }
        return tarefas;
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

    private LocalDate verificarData(Tarefa tarefa) {

        if(tarefa.getDataPrevistaConclusao().isBefore(LocalDate.now())){
            throw new TarefaException("A data prevista não pode ser anterior a data atual!");
        }
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

    public List<Tarefa> filtro(Tarefa tarefa) {
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
        tarefa.setDataPrevistaConclusao(dto.getDataPrevistaConclusao());
        tarefa.setDeadline(java.time.LocalDate.now());
        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        return tarefa;
    }

}




