package application.domain.services;

import application.domain.dto.TarefaDTO;
import application.domain.dto.model.TarefaResponseDTO;
import application.domain.dto.model.TarefaUpdateRequestDTO;
import application.domain.dto.model.UsuarioResponseDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.exception.UsuarioNaoEncontradoException;
import application.domain.repositories.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TarefaService {

    @Autowired
    private TarefaRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    public void criarTarefa(TarefaDTO tarefaRequestDto) throws MessagingException {
        Long idResponsavel = tarefaRequestDto.getIdResponsavel();
        Usuario responsavel = userService.buscarUsuarioPorId(idResponsavel);


        Tarefa tarefa = fromDto(tarefaRequestDto);
        verificarData(tarefa);
        tarefa.setResponsavel(responsavel);

        repository.save(tarefa);
        emailService.envioDeEmailComAnexo(tarefa);
    }

    public List<TarefaResponseDTO> listarTodasTarefas() {
        return repository.findAll().stream().map(this::converterParaTarefaResponse).collect(Collectors.toList());
    }

    public void removerTarefa(Long tarefaId) {
        if (!repository.existsById(tarefaId)) {
            throw new TarefaNaoEncontradaException("Tarefa ID:" + tarefaId + " não encontrada!");
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
            verificarData(tarefa);
            return repository.save(tarefa);
        }).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa não encontrada!"));
    }

    public TarefaResponseDTO buscarTarefaPorId(Long tarefaId) {
        return repository.findById(tarefaId).map(this::converterParaTarefaResponse)
                .orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa ID:" + tarefaId + " não encontrada!"));
    }

    public List<TarefaResponseDTO> buscarDescricaoTarefa(String descricao) {
        List<TarefaResponseDTO> listaDescricao = repository.findByDescricao(descricao).stream()
                .map(this::converterParaTarefaResponse).collect(Collectors.toList());
        if (listaDescricao.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com essa DESCRIÇÃO no momento!");

        }
        return listaDescricao;
    }

    public List<TarefaResponseDTO> buscarTituloTarefa(String titulo) {
        List<TarefaResponseDTO> listaTitulos = repository.findByTitulo(titulo).stream()
                .map(this::converterParaTarefaResponse).collect(Collectors.toList());
        if (listaTitulos.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com esse TÍTULO no momento!");
        }
        return listaTitulos;
    }

    public List<TarefaResponseDTO> buscarStatusTarefa(String status) {
        List<TarefaResponseDTO> listaTarefasStatus = repository.status(status).stream()
                .map(this::converterParaTarefaResponse).collect(Collectors.toList());
        if (listaTarefasStatus.isEmpty()) {
            throw new TarefaNaoEncontradaException("Não existe tarefas com esse STATUS no momento!");
        }
        return listaTarefasStatus;
    }

    public List<Tarefa> buscarResponsavelTarefa(Usuario responsavel) {
        if (responsavel == null) {
            throw new UsuarioNaoEncontradoException("Responsável não encontrado!");
        }
        List<Tarefa> tarefas = repository.findByResponsavel(responsavel);
        if (tarefas == null || tarefas.isEmpty()) {
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

        if (tarefa.getDataPrevistaConclusao().isBefore(LocalDate.now())) {
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
        tarefa.setDataAbertura(LocalDate.now());
        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        return tarefa;
    }

    private TarefaResponseDTO converterParaTarefaResponse(Tarefa tarefa) {
        Usuario responsavel = tarefa.getResponsavel();
        UsuarioResponseDTO usuarioResponseDTO = modelMapper.map(responsavel, UsuarioResponseDTO.class);
        TarefaResponseDTO tarefaResponseDTO = modelMapper.map(tarefa, TarefaResponseDTO.class);
        tarefaResponseDTO.setResponsavel(usuarioResponseDTO);
        return tarefaResponseDTO;
    }

}




