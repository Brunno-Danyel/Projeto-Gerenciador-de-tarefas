package application.domain.controller;

import application.domain.dto.TarefaDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaException;
import application.domain.repositories.TarefaRepository;
import application.domain.services.TarefaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService service;

    @Autowired
    private TarefaRepository repository;

    @PostMapping("/tarefa")
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa createdTask(@RequestBody @Valid TarefaDTO tarefaDto)  {
        return service.createdTask(tarefaDto);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Tarefa>> listTask() {
        List<Tarefa> list = service.listTask();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("id/{tarefaId}")
    public ResponseEntity<Tarefa> findByNumber(@PathVariable Long tarefaId) {
        Tarefa task = service.findById(tarefaId);
        return ResponseEntity.ok().body(task);
    }

    @GetMapping("nome/{nomeUsuario}")
    public ResponseEntity<List<Tarefa>> findByNome(@PathVariable String nomeUsuario) {
        List<Tarefa> listNameUser = service.searchName(nomeUsuario);
        return ResponseEntity.ok().body(listNameUser);
    }

    @GetMapping("descricao/{descricao}")
    public List<Tarefa> findByDescription(@PathVariable String descricao) {
        List<Tarefa> listDescricaoTask = service.searchDescription(descricao);
        return listDescricaoTask;
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Tarefa>> findByStatusTarefa(@PathVariable String status){
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
        if (!repository.existsById(tarefaId)) {
            return ResponseEntity.notFound().build();
        }

        service.removeTask(tarefaId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{tarefaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Tarefa> updateTask(@PathVariable Long tarefaId, @RequestBody @Valid Tarefa task) {
        task = service.updateTask(tarefaId, task);
        return ResponseEntity.ok().body(task);

    }

    @PutMapping("/{tarefaId}/concluir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void concluir(@PathVariable Long tarefaId) {
           service.concluir(tarefaId);
    }
}
