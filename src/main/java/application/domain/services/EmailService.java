package application.domain.services;

import application.domain.dto.model.EnvioDeEmail;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.TarefaNaoEncontradaException;
import application.domain.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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

    @Autowired
    private TarefaRepository tarefaRepository;

    @Value("${spring.mail.username}")
    private String remetente;


    public void envioDeEmailComAnexo(Tarefa tarefa) throws MessagingException {
        Usuario responsavel = userService.buscarUsuarioPorId(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa adicionada com sucesso!");
        helper.setText(corpoMessagem(tarefa));

        helper.addAttachment("narutoSorrindo.jpg", new ClassPathResource("arquivos/narutoSorrindo.jpg"));
        javaMailSender.send(email);
    }

    public void envioDeEmailTarefaConcluidaComAnexo(Tarefa tarefa) throws MessagingException {
        Usuario responsavel = userService.buscarUsuarioPorId(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa concluída com sucesso!");
        helper.setText(corpoMessagem(tarefa));

        helper.addAttachment("narutoJoinha.jpg", new ClassPathResource("arquivos/narutoJoinha.jpg"));
        javaMailSender.send(email);
    }

    public void envioDeEmailTarefaAtrasada(Tarefa tarefa) throws MessagingException {

        Usuario responsavel = userService.buscarUsuarioPorId(tarefa.getResponsavel().getId());

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(responsavel.getLogin());
        helper.setSubject("Tarefa em atraso!");
        helper.setText(corpoMessagem(tarefa));

        helper.addAttachment("narutoChateado.jpg", new ClassPathResource("arquivos/narutoChateado.jpg"));
        javaMailSender.send(email);
    }

    public void envioDeEmailUnitario(EnvioDeEmail envioDeEmail) throws MessagingException {

        Long idTarefa = envioDeEmail.getIdTarefa();
        Long idUsuario = envioDeEmail.getIdUsuario();

        Usuario usuario = userService.buscarUsuarioPorId(idUsuario);
        Tarefa tarefa = tarefaRepository.findById(idTarefa).orElseThrow(() -> new TarefaNaoEncontradaException("Tarefa ID:" + idTarefa + " não encontrada!"));

        MimeMessage email = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(email, true);

        helper.setFrom(remetente);
        helper.setTo(usuario.getLogin());
        helper.setSubject("Envio de tarefa!");
        helper.setText(corpoMessagem(tarefa));

        if(tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)) {
            helper.addAttachment("narutoJoinha.jpg", new ClassPathResource("arquivos/narutoJoinha.jpg"));
        }

        if(tarefa.getStatus().equals(StatusTarefa.EM_ANDAMENTO)){
            helper.addAttachment("narutoSorrindo.jpg", new ClassPathResource("arquivos/narutoSorrindo.jpg"));
        }

        if(tarefa.getStatus().equals(StatusTarefa.ATRASADA)){
            helper.addAttachment("narutoChateado.jpg", new ClassPathResource("arquivos/narutoChateado.jpg"));
        }
        javaMailSender.send(email);
    }

    public String corpoMessagem(Tarefa tarefa){
       String mensagem = new String();
        if(tarefa.getStatus().equals(StatusTarefa.ATRASADA)) {
             mensagem = "--> Tarefa em Atraso, por favor concluir tarefa. <--" + "\n\n" +
                    "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                    "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                    " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                    " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                    " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                    " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                    " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                    " Data da abertura da tarefa: " + tarefa.getDataAbertura().toString() + "\n\n" +
                    " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao();
        }

        if(tarefa.getStatus().equals(StatusTarefa.EM_ANDAMENTO)){
            mensagem = "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                    "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                    " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                    " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                    " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                    " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                    " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                    " Data da abertura da tarefa: " + tarefa.getDataAbertura().toString() + "\n\n" +
                    " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao();
        }

        if(tarefa.getStatus().equals(StatusTarefa.CONCLUIDA)){
            mensagem = "Número da tarefa: " + (tarefa.getId()) + "\n\n" +
                    "Título da Tarefa: " + tarefa.getTitulo() + " \n\n" +
                    " Descrição da tarefa: " + tarefa.getDescricao() + " \n\n" +
                    " Nome do responsavel: " + tarefa.getResponsavel().getNome() + " \n\n" +
                    " Endereço de e-mail do responsável: " + tarefa.getResponsavel().getLogin() + " \n\n" +
                    " Status da tarefa: " + tarefa.getStatus().toString() + " \n\n" +
                    " Prioridade da tarefa: " + tarefa.getPrioridade().toString() + " \n\n" +
                    " Data da abertura da tarefa: " + tarefa.getDataAbertura().toString() + "\n\n" +
                    " Data prevista para a conclusão da tarefa: " + tarefa.getDataPrevistaConclusao() + "\n\n" +
                    " Data de conclusão da tarefa: " + tarefa.getDataConclusao().toString();
        }
         return mensagem;
    }
}
