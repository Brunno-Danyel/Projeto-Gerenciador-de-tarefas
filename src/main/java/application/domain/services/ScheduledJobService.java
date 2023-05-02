package application.domain.services;

public interface ScheduledJobService {

    void converterTarefasAtrasadas();

    void enviarTarefasEmAtraso();
}
