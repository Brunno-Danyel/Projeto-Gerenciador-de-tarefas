package application.domain.repositories;

import application.domain.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {


    List<Tarefa> findByDescricao(String descricao);

    List<Tarefa> findByTitulo(String titulo);

    List<Tarefa> findByStatus(String status);
}
