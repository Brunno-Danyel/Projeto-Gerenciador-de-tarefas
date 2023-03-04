package application.domain.exception;

public class UsuarioNaoEncontradoException extends RuntimeException{

    public UsuarioNaoEncontradoException(String msg) {
        super(msg);
    }
}
