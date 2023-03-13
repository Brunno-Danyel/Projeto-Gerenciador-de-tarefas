package application.domain.services;

import application.domain.entities.Tarefa;
import application.domain.enumeration.StatusTarefa;
import application.domain.repositories.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScheduledJobService {

    private static final String TAREFAS_ATRASO = "0 0 8 1/1 * ?";

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Scheduled(cron = TAREFAS_ATRASO)
    private void converterTarefasAtrasadas() {
        List<Tarefa> tarefasVerificadas = tarefaService.listTask().stream().map(tarefas ->{
            if(tarefas.getDataPrevistaConclusao() != null && tarefas.getDataPrevistaConclusao().isBefore(LocalDate.now())){
                tarefas.setStatus(StatusTarefa.ATRASADA);
            }
            tarefaRepository.save(tarefas);
            return tarefas;
        }).collect(Collectors.toList());
    log.info("Agendamento realizado!");
    }



}
