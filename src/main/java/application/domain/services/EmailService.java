package application.domain.services;

import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Value("${spring.mail.username}")
    private String remetente;

    public void envioDeEmailTarefaAdicionada(Tarefa tarefa) {

        Long idUsuario = tarefa.getResponsavel().getId();
        Usuario responsavel = userService.findById(tarefa.getResponsavel().getId());


        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(remetente);
        email.setTo(responsavel.getLogin());
        email.setSubject("Tarefa adicionada com sucesso!");
        email.setText(
                "Número da tarefa: " + (tarefa.getId()) + "\n" +
                        "Título da Tarefa: " + tarefa.getTitulo() + " \n" +
                        " Descrição da tarefa: " + tarefa.getDescricao() + " \n" +
                        " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n" +
                        " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n" +
                        " Status da tarefa: " + tarefa.getStatus().toString() + " \n" +
                        " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n" +
                        " Data da abertura da tarefa: " + tarefa.getDeadline().toString());
        javaMailSender.send(email);
    }

    public void envioDeEmailTarefaConcluida(Tarefa tarefa) {

        Long idUsuario = tarefa.getResponsavel().getId();
        Usuario responsavel = userService.findById(tarefa.getResponsavel().getId());


        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(remetente);
        email.setTo(responsavel.getLogin());
        email.setSubject("Tarefa concluída com sucesso!");
        email.setText(
                "Número da tarefa: " + (tarefa.getId()) + "\n" +
                        "Título da Tarefa: " + tarefa.getTitulo() + " \n" +
                        " Descrição da tarefa: " + tarefa.getDescricao() + " \n" +
                        " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n" +
                        " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n" +
                        " Status da tarefa: " + tarefa.getStatus().toString() + " \n" +
                        " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n" +
                        " Data da abertura da tarefa: " + tarefa.getDeadline().toString() + " \n" +
                        " Data de conclusão da tarefa: " + tarefa.getDataConclusao().toString());
        javaMailSender.send(email);

    }
}
