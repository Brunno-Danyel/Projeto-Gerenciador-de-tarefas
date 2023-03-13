package application.domain.services;

import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    @Value("${spring.mail.username}")
    private String remetente;


    public void envioDeEmailComAnexo(Tarefa tarefa) throws MessagingException {
        Usuario responsavel = userService.findById(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa adicionada com sucesso!");
        helper.setText(
             //   "--> Assim como os habitantes de Konoha confiam em seu hokage, estou confiando essa tarefa a você, boa tarefa! <--" + "\n\n" +
                "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                        "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                        " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                        " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                        " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                        " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                        " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                        " Data da abertura da tarefa: " + tarefa.getDeadline().toString() + "\n\n" +
                        " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao());

        helper.addAttachment("narutoSorrindo.jpg", new ClassPathResource("arquivos/narutoSorrindo.jpg"));
        javaMailSender.send(email);
    }

    public void envioDeEmailTarefaConcluidaComAnexo(Tarefa tarefa) throws MessagingException {
        Usuario responsavel = userService.findById(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa concluída com sucesso!");
        helper.setText(
                "--> Parabéns por ter concluído a tarefa! <--" + "\n\n" +
                        "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                        "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                        " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                        " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                        " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                        " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                        " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                        " Data da abertura da tarefa: " + tarefa.getDeadline().toString() + "\n\n" +
                        " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao() + "\n\n" +
                        " Data de conclusão da tarefa: " + tarefa.getDataConclusao().toString());

        helper.addAttachment("narutoJoinha.jpg", new ClassPathResource("arquivos/narutoJoinha.jpg"));
        javaMailSender.send(email);
    }
    public void envioDeEmailTarefaAtrasada(Tarefa tarefa) throws MessagingException {

        Usuario responsavel = userService.findById(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa em atraso!");
        helper.setText(
                "--> Tarefa em Atraso, por favor concluir tarefa. <--" + "\n\n" +
                        "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                        "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                        " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                        " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                        " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                        " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                        " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                        " Data da abertura da tarefa: " + tarefa.getDeadline().toString() + "\n\n" +
                        " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao());

        helper.addAttachment("narutoChateado.jpg", new ClassPathResource("arquivos/narutoChateado.jpg"));
        javaMailSender.send(email);
    }
}
