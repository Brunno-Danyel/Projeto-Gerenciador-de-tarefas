package application.domain.controller;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.repositories.TarefaRepository;
import application.domain.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService service;

    @Autowired
    private TarefaRepository repository;


    @PostMapping("/tarefa")
    @ResponseStatus(HttpStatus.CREATED)
    public void createdTask(@RequestBody @Valid TarefaDTO tarefaDto) throws MessagingException {
         service.createdTask(tarefaDto);
    }

    @GetMapping("/listar")
    public List<Tarefa> listarTarefasConcluidas() {
        List<Tarefa> list = service.listTask();
        return list;
    }

    @GetMapping("id/{tarefaId}")
    public ResponseEntity<Tarefa> findByNumber(@PathVariable Long tarefaId) {
        Tarefa tarefa = service.findById(tarefaId);
        return ResponseEntity.ok().body(tarefa);
    }

    @GetMapping("responsavel/{responsavel}")
    public ResponseEntity<Optional<List<Tarefa>>> findByResponsavel(@PathVariable Usuario responsavel) {
        Optional<List<Tarefa>> tarefa = service.searchResponsavel(responsavel);
        return ResponseEntity.ok().body(tarefa);
    }


    @GetMapping("descricao/{descricao}")
    public ResponseEntity<List<Tarefa>> findByDescription(@PathVariable String descricao) {
        List<Tarefa> listDescricaoTask = service.searchDescription(descricao);
        return ResponseEntity.ok().body(listDescricaoTask);
    }

    @GetMapping("titulo/{titulo}")
    public ResponseEntity<List<Tarefa>> findByTitle(@PathVariable String titulo) {
        List<Tarefa> listTitleTask = service.searchTitle(titulo);
        return ResponseEntity.ok().body(listTitleTask);
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Tarefa>> findByStatusTarefa(@PathVariable String status) {
        List<Tarefa> listStatusTask = service.searchStatus(status);
        return ResponseEntity.ok().body(listStatusTask);
    }

    @GetMapping("/filtro")
    public List<Tarefa> find(Tarefa tarefa) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(tarefa, matcher);
        return repository.findAll(example);
    }

    @DeleteMapping("/{tarefaId}")
    public ResponseEntity<Void> removeTask(@PathVariable Long tarefaId) {
        service.removeTask(tarefaId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Tarefa> updateTask(@PathVariable Long tarefaId, @RequestBody @Valid TarefaDTO tarefaDTO) {
        Tarefa tarefa = service.atualizandoTarefa(tarefaId, tarefaDTO);
        return ResponseEntity.ok().body(tarefa);
    }

    @PutMapping("concluir/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluir(@PathVariable Long tarefaId) throws MessagingException {
        service.concluir(tarefaId);
    }

}
