package application.domain.services;

import application.domain.enumeration.StatusTarefa;
import application.domain.repositories.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScheduledJobService {

    private static final String TAREFAS_ATRASO = "0 0 8 1/1 * ?";


    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 35 10 1/1 * ?")
    private void converterTarefasAtrasadas() {
        tarefaRepository.findAll().stream().map(tarefas -> {
            if (tarefas.getDataPrevistaConclusao() != null && tarefas.getDataPrevistaConclusao().isBefore(LocalDate.now()) && tarefas.getDataConclusao() == null) {
                tarefas.setStatus(StatusTarefa.ATRASADA);
            }
            tarefaRepository.save(tarefas);
            return tarefas;
        }).collect(Collectors.toList());
        log.info("Converção realizada com sucesso!");
    }

    @Scheduled(cron = "0 36 10 1/1 * ?")
    private void enviarTarefasEmAtraso() {
        tarefaRepository.findAll().stream().map(tarefas -> {
            if (tarefas.getStatus().equals(StatusTarefa.ATRASADA)) {
                try {
                    emailService.envioDeEmailTarefaAtrasada(tarefas);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
            return tarefas;
        }).collect(Collectors.toList());
        log.info("E-mail enviado com sucesso!");
    }


}
