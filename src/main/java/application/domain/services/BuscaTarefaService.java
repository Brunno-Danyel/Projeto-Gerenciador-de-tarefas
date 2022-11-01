package application.domain.services;

import application.domain.entities.Tarefa;
import application.domain.exception.TarefaException;
import application.domain.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuscaTarefaService {


    @Autowired
    private TarefaRepository repository;

    public Tarefa findById(Long tarefaId) {
        return repository.findById(tarefaId).orElseThrow(() -> new TarefaException("Tarefa não encontrada"));
    }
}
