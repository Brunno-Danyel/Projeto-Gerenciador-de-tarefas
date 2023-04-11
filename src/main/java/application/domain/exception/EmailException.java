package application.domain.exception;

public class EmailException extends RuntimeException{
    public EmailException(){
        super("Erro ao tentar realizar o envio de e-mail!");
    }

}
