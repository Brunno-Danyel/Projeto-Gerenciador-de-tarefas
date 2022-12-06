package application.domain.controller;

import application.domain.secutiry.JwtService;
import application.domain.dto.CredenciaisDTO;
import application.domain.dto.TokenDTO;
import application.domain.dto.UsuarioDTO;
import application.domain.entities.Usuario;
import application.domain.exception.SenhaInvalidaException;
import application.domain.services.UserService;
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
@RequestMapping("/usuarios")
public class UsuarioController {

    private final PasswordEncoder encoder;
    private final UserService service;
    private final JwtService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid UsuarioDTO usuario) {
        String senhaCriptografada = encoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return service.save(usuario);
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

    @GetMapping
    public ResponseEntity<List<Usuario>> listUser(){
        List<Usuario> listUser = service.listUser();
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

