package application.domain.controller;

import application.domain.model.EnvioDeEmail;
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

    @PostMapping("/enviar")
    @ResponseStatus(HttpStatus.OK)
    private void envioDeEmailUnitario(@RequestBody EnvioDeEmail envioDeEmail) throws MessagingException {
        emailService.envioDeEmailUnitario(envioDeEmail);
    }
}
