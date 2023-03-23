package application.domain.controller;

import application.domain.dto.TarefaDTO;
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
import java.util.Optional;

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

    @GetMapping("/listar")
    public List<Tarefa> listarTodasTarefas() {
        List<Tarefa> list = service.listarTodasTarefas();
        return list;
    }

    @GetMapping("id/{tarefaId}")
    public ResponseEntity<Tarefa> buscarTarefaPorId(@PathVariable Long tarefaId) {
        Tarefa tarefa = service.buscarTarefaPorId(tarefaId);
        return ResponseEntity.ok().body(tarefa);
    }

    @GetMapping("responsavel/{responsavel}")
    public ResponseEntity<Optional<List<Tarefa>>> buscarResponsavelTarefa(@PathVariable Usuario responsavel) {
        Optional<List<Tarefa>> tarefa = service.buscarResponsavelTarefa(responsavel);
        return ResponseEntity.ok().body(tarefa);
    }


    @GetMapping("descricao/{descricao}")
    public ResponseEntity<List<Tarefa>> buscarDescricaoTarefa(@PathVariable String descricao) {
        List<Tarefa> listDescricaoTask = service.buscarDescricaoTarefa(descricao);
        return ResponseEntity.ok().body(listDescricaoTask);
    }

    @GetMapping("titulo/{titulo}")
    public ResponseEntity<List<Tarefa>> buscarTituloTarefa(@PathVariable String titulo) {
        List<Tarefa> listTitleTask = service.buscarTituloTarefa(titulo);
        return ResponseEntity.ok().body(listTitleTask);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Tarefa>> buscarStatusTarefa(@PathVariable String status) {
        List<Tarefa> listStatusTask = service.buscarStatusTarefa(status);
        return ResponseEntity.ok().body(listStatusTask);
    }

    @GetMapping("/filtro")
    public List<Tarefa> filtro(Tarefa tarefa) {
        List<Tarefa> tarefasFiltradas = service.filtro(tarefa);
        return tarefasFiltradas;
    }

    @DeleteMapping("/{tarefaId}")
    public ResponseEntity<Void> removerTarefa(@PathVariable Long tarefaId) {
        service.removerTarefa(tarefaId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long tarefaId, @RequestBody @Valid TarefaUpdateRequestDTO tarefaDTO) {
        Tarefa tarefa = service.atualizarTarefa(tarefaId, tarefaDTO);
        return ResponseEntity.ok().body(tarefa);
    }

    @PutMapping("concluir/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluirTarefa(@PathVariable Long tarefaId) throws MessagingException {
        service.concluirTarefa(tarefaId);
    }

}
