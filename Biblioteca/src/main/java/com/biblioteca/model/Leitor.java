package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;

public class Leitor extends Pessoa {
    private String matricula;

    public Leitor() {
    }

    public Leitor(Integer id, String nome, String cpf, String email, String telefone, String matricula) throws ValidacaoException {
        super(id, nome, cpf, email, telefone);
        setMatricula(matricula);
    }

    @Override
    public int getLimiteEmprestimos() {
        return 3; // Leitores podem pegar até 3 livros por vez
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) throws ValidacaoException {
        if (matricula == null || matricula.trim().isEmpty()) {
            throw new ValidacaoException("A matrícula do leitor é obrigatória.");
        }
        this.matricula = matricula.trim();
    }

    @Override
    public String toString() {
        return getNome() + " - Matrícula: " + matricula;
    }
}