package application.domain.dto;

import application.domain.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String login;
    private String nome;
    private String senha;
    private boolean admin;

    public static Usuario fromDto(UsuarioDTO dto){
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setLogin(dto.getLogin());
        usuario.setNome(dto.getNome());
        usuario.setSenha(dto.getSenha());
        usuario.setAdmin(dto.isAdmin());

        return usuario;
    }
}
