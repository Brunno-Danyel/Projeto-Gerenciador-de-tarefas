package application.domain.services;

import application.domain.dto.model.EnvioDeEmail;
import application.domain.entities.Tarefa;

import javax.mail.MessagingException;

public interface EmailService {

    void envioDeEmailComAnexo(Tarefa tarefa) throws MessagingException;

    void envioDeEmailTarefaConcluidaComAnexo(Tarefa tarefa) throws MessagingException;

    void envioDeEmailTarefaAtrasada(Tarefa tarefa) throws MessagingException;

    void envioDeEmailUnitario(EnvioDeEmail envioDeEmail) throws MessagingException;

    void enviarTarefaParaTodos(Long tarefaId) throws MessagingException;

    }
