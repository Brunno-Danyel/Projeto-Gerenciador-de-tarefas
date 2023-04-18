package application.domain.services;

import application.domain.dto.UsuarioDTO;
import application.domain.entities.Tarefa;
import application.domain.entities.Usuario;
import application.domain.enumeration.StatusTarefa;
import application.domain.exception.SenhaInvalidaException;
import application.domain.exception.UsuarioException;
import application.domain.exception.UsuarioNaoEncontradoException;
import application.domain.repositories.TarefaRepository;
import application.domain.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    public static final int QUANTIDADE_TAREFA = 3;
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TarefaRepository tarefaRepository;

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
    public void cadastrarUsuario(@Valid UsuarioDTO usuario) {
        Usuario user = usuario.fromDto(usuario);
        boolean loginEmUso = repository.findByLogin(usuario.getLogin()).stream()
                .anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));

        if (loginEmUso) {
            throw new UsuarioException("Já existe um usuário cadastrado com esse e-mail");
        }
        repository.save(user);
    }

    public void promoverAdmin(Long idUsuario) {
        Usuario usuario = repository.findById(idUsuario).map(user -> {
            if (user.isAdmin() == true) {
                throw new UsuarioException("Usuario " + idUsuario + " já é admin!");
            }

            user.setAdmin(true);
            repository.save(user);
            return user;
        }).orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        List users = repository.findAll();
        return users;
    }

    public void verificaQuantidadeDeTarefasParaUsuario(Usuario usuario) {
        List<Tarefa> listaDeTarefasEmAndamento = usuario.getTarefa().stream()
                .filter(tarefa -> tarefa.getStatus().equals(StatusTarefa.EM_ANDAMENTO) || tarefa.getStatus().equals(StatusTarefa.ATRASADA)).collect(Collectors.toList());
        if (listaDeTarefasEmAndamento.size() > QUANTIDADE_TAREFA) {
            throw new UsuarioException("Usuário " + usuario.getNome() + " já tem o número máximo de tarefas em andamento!");
        }
    }

}
