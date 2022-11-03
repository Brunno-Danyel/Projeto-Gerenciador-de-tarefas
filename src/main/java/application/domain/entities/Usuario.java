package application.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Digite um email de login válido")
    @NotEmpty(message = "${campo.login.obrigatorio}")
    private String login;

    @NotEmpty(message = "${campo.senha.obrigatorio}")
    @JsonIgnore
    private String senha;

    @JsonIgnore
    @OneToMany(mappedBy = "responsavel")
    private List<Tarefa> tarefa = new ArrayList<>();

    private boolean admin;
}
