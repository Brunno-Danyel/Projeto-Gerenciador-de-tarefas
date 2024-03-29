package application.domain.controller;

import application.domain.dto.CredenciaisDTO;
import application.domain.dto.TokenDTO;
import application.domain.dto.UsuarioDTO;
import application.domain.entities.Usuario;
import application.domain.exception.SenhaInvalidaException;
import application.domain.secutiry.JwtService;
import application.domain.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private final PasswordEncoder encoder;
    private final UsuarioService service;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Valid UsuarioDTO usuario) {
        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        service.cadastrarUsuario(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO dto) {
        try {
            Usuario usuario = Usuario.builder().login(dto.getLogin()).senha(dto.getSenha()).build();
            UserDetails usuarioAutenticado = service.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/admin/{idUsuario}")
    @ResponseStatus(HttpStatus.OK)
    public void promoverAdmin(@PathVariable Long idUsuario){
        service.promoverAdmin(idUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listUser(){
        List<Usuario> listUser = service.listarUsuarios();
        return ResponseEntity.ok().body(listUser);
    }

	/*@GetMapping("/{usuarioId}")
	public ResponseEntity<Usuario> findByNumber(@PathVariable Long usuarioId) {
		Usuario user = service.findById(usuarioId);
		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/nome/{nomeUsuario}")
	public ResponseEntity<List<Usuario>> findByName(@PathVariable String nomeUsuario){
		List<Usuario> user = service.searchName(nomeUsuario);
		return ResponseEntity.ok().body(user);
	}*/
    }

