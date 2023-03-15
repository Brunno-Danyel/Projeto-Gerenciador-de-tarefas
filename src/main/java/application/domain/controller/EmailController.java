package application.domain.controller;

import application.domain.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/tarefa/{idTarefa}/usuario/{idUsuario}")
    @ResponseStatus(HttpStatus.OK)
    private void envioDeEmailUnitario(@PathVariable Long idTarefa, @PathVariable Long idUsuario) throws MessagingException {
        emailService.envioDeEmailUnitario(idTarefa, idUsuario);
    }
}
