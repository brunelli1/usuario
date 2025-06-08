package com.raulproject.usuario.infraestructure.exceptions;

public class ConflitException extends RuntimeException{

    public ConflitException(String mensagem){
        super(mensagem);
    }

    public ConflitException(String mensagem, Throwable throwable){
        super(mensagem);
    }

}
