package application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredenciaisDTO {

    @NotEmpty(message = "{campo.login.obrigatorio}")
    @Email(message = "{campo.login.valido}")
    private String login;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
