package application.domain.exception;

public class TarefaNaoEncontradaException extends RuntimeException {

    public TarefaNaoEncontradaException(String msg) {
        super(msg);
    }
}
