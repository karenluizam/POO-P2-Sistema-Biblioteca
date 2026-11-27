package com.biblioteca.exception;

public class ValidacaoException extends Exception{

    //construtor para as classes do model
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
    //construtor para a DAO
    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
