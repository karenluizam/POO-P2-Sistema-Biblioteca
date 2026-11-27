package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;

public class Funcionario extends Pessoa {
    private char turno;
    private String cargo;
    private double salario;

    public Funcionario() {
    }

    public Funcionario(Integer id, String nome, String cpf, String email, String telefone, char turno, String cargo, double salario) throws ValidacaoException {
        super(id, nome, cpf, email, telefone);
        setTurno(turno);
        setCargo(cargo);
        setSalario(salario);
    }

    @Override
    public int getLimiteEmprestimos() {
        return 5; // Funcionários têm limite maior (polimorfismo)
    }

    public char getTurno() {
        return turno;
    }

    public void setTurno(char turno) throws ValidacaoException {
        char turnoUpper = Character.toUpperCase(turno);
        if (turnoUpper != 'M' && turnoUpper != 'T' && turnoUpper != 'N') {
            throw new ValidacaoException("Turno inválido! Use M (Manhã), T (Tarde) ou N (Noite).");
        }
        this.turno = turnoUpper;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) throws ValidacaoException {
        if (cargo == null || cargo.trim().isEmpty()) {
            throw new ValidacaoException("O cargo do funcionário é obrigatório.");
        }
        this.cargo = cargo.trim();
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) throws ValidacaoException {
        if (salario <= 0) {
            throw new ValidacaoException("O salário deve ser maior que zero.");
        }
        this.salario = salario;
    }

    @Override
    public String toString() {
        return getNome() + " (" + cargo + ")";
    }
}