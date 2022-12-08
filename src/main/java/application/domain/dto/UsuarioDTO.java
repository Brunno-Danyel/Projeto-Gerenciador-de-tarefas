package application.domain.dto;

import application.domain.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotEmpty(message = "{campo.login.obrigatorio}")
    @Email(message = "{campo.login.valido}")
    private String login;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;


    public Usuario fromDto(UsuarioDTO usuario){
        return new Usuario(login, nome, senha);
    }
}
