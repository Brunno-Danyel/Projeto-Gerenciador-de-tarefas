package application.domain.repositories;

import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {


    @Query(value = "select * from tarefa t join usuario u on t.tb_id_user = u.id  where  t.tb_descricao_tarefa like %:descricao% ", nativeQuery = true)
    Optional<List<Tarefa>> findByDescricao(String descricao);

    @Query(value = "select * from tarefa t join usuario u on t.id_user = u.id  where  t.titulo_tarefa like %:titulo% ", nativeQuery = true)
    Optional<List<Tarefa>> findByTitulo(String titulo);

    @Query(value = "select * from tarefa t join usuario u on t.tb_id_user = u.id  where t.tb_status like %:status%", nativeQuery = true)
    List<Tarefa> status(@Param(value = "status") String status);

    Optional<List<Tarefa>> findByResponsavel(Usuario responsavel);
}
