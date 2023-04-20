package application.domain.controller;

import application.domain.dto.TarefaDTO;
import application.domain.dto.model.TarefaRequestDTO;
import application.domain.dto.model.TarefaResponseDTO;
import application.domain.dto.model.TarefaUpdateRequestDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.repositories.TarefaRepository;
import application.domain.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService service;

    @Autowired
    private TarefaRepository repository;


    @PostMapping("/tarefa")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarTarefa(@RequestBody @Valid TarefaDTO tarefaDto) throws MessagingException {
        service.criarTarefa(tarefaDto);
    }

    @PostMapping("/tarefa/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarTarefaParaTodos(@RequestBody @Valid TarefaRequestDTO tarefaDto) throws MessagingException {
        service.cadastrarTarefaParaTodos(tarefaDto);
    }

    @GetMapping("/listar")
    public List<TarefaResponseDTO> listarTodasTarefas() {
        List<TarefaResponseDTO> tarefaResponseDTOList = service.listarTodasTarefas();
        return tarefaResponseDTOList;
    }

    @GetMapping("id/{tarefaId}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefaPorId(@PathVariable Long tarefaId) {
        TarefaResponseDTO tarefaResponseDTO = service.buscarTarefaPorId(tarefaId);
        return ResponseEntity.ok().body(tarefaResponseDTO);
    }

    @GetMapping("responsavel/{responsavel}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarResponsavelTarefa(@PathVariable Usuario responsavel) {
        List<TarefaResponseDTO> tarefa = service.buscarResponsavelTarefa(responsavel);
        return ResponseEntity.ok().body(tarefa);
    }


    @GetMapping("descricao/{descricao}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarDescricaoTarefa(@PathVariable String descricao) {
        List<TarefaResponseDTO> listDescricaoTask = service.buscarDescricaoTarefa(descricao);
        return ResponseEntity.ok().body(listDescricaoTask);
    }

    @GetMapping("titulo/{titulo}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarTituloTarefa(@PathVariable String titulo) {
        List<TarefaResponseDTO> listTitleTask = service.buscarTituloTarefa(titulo);
        return ResponseEntity.ok().body(listTitleTask);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<TarefaResponseDTO>> buscarStatusTarefa(@PathVariable String status) {
        List<TarefaResponseDTO> listStatusTask = service.buscarStatusTarefa(status);
        return ResponseEntity.ok().body(listStatusTask);
    }

    @GetMapping("/filtro")
    public List<Tarefa> filtro(Tarefa tarefa) {
        List<Tarefa> tarefasFiltradas = service.filtro(tarefa);
        return tarefasFiltradas;
    }

    @DeleteMapping("remover/{tarefaId}")
    public ResponseEntity<Void> removerTarefa(@PathVariable Long tarefaId) {
        service.removerTarefa(tarefaId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<TarefaResponseDTO> atualizarTarefa(@PathVariable Long tarefaId, @RequestBody @Valid TarefaUpdateRequestDTO tarefaDTO) {
        TarefaResponseDTO tarefa = service.atualizarTarefa(tarefaId, tarefaDTO);
        return ResponseEntity.ok().body(tarefa);
    }

    @PutMapping("concluir/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluirTarefa(@PathVariable Long tarefaId) throws MessagingException {
        service.concluirTarefa(tarefaId);
    }

}
