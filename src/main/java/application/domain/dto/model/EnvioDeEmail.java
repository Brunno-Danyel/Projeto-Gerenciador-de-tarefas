package application.domain.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioDeEmail {

    Long idTarefa;

    Long idUsuario;
}
