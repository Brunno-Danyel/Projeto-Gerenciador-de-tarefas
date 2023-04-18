package application.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{campo.login.obrigatorio}")
    @Email(message = "{campo.login.valido}")
    @Column(name = "tb_login_usuario")
    private String login;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    @Column(name = "tb_nome_usuario")
    private String nome;


    @JsonIgnore
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    @Column(name = "tb_senha_user")
    private String senha;

    @JsonIgnore
    @ManyToMany(mappedBy = "responsavel")
    private List<Tarefa> tarefa = new ArrayList<>();

    @JsonIgnore
    private boolean admin;

    public Usuario(String login, String nome, String senha) {
        this.login = login;
        this.nome = nome;
        this.senha = senha;
    }

}
