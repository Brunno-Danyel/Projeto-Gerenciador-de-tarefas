package application.domain.services;

import application.domain.dto.UsuarioDTO;
import application.domain.entities.Usuario;
import application.domain.exception.SenhaInvalidaException;
import application.domain.exception.UsuarioException;
import application.domain.exception.UsuarioNaoEncontradoException;
import application.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails autenticar(Usuario usuario) {
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhasBatem = passwordEncoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasBatem) {
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado na base de dados"));

        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User
                .builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles).build();
    }

    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return repository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }

    @Transactional
    public Usuario cadastrarUsuario(UsuarioDTO usuario) {
        Usuario user = usuario.fromDto(usuario);
        boolean loginEmUso = repository.findByLogin(usuario.getLogin()).stream()
                .anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));

        if (loginEmUso ) {
            throw new UsuarioException("Já existe um cliente cadastrado com esse e-mail");
        }
        return repository.save(user);
    }

    public List<Usuario> listarUsuarios() {
        List users = repository.findAll();
        return users;
    }

}
