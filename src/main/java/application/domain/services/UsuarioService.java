package application.domain.services;

import application.domain.dto.UsuarioDTO;
import application.domain.entities.Usuario;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioService {

     UserDetails autenticar(Usuario usuario);

     Usuario buscarUsuarioPorId(Long idUsuario);

     List<Usuario> listarUsuarios();

     void cadastrarUsuario(UsuarioDTO usuarioDTO);

     void promoverAdmin(Long idUsuario);

     String retornarNomeOrganizador();

}