package com.biblioteca.model;
import com.biblioteca.exception.ValidacaoException;

public abstract class Pessoa {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    public Pessoa() {
    }

    public Pessoa(Integer id, String nome, String cpf, String email, String telefone) throws ValidacaoException {
        setId(id);
        setNome(nome);
        setCpf(cpf);
        setEmail(email);
        setTelefone(telefone);
    }

    // Metodo polimórfico que cada subclasse implementará com sua regra
    public abstract int getLimiteEmprestimos();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("O nome é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws ValidacaoException {
        if (cpf == null || cpf.replaceAll("[^0-9]", "").length() != 11) {
            throw new ValidacaoException("CPF inválido! Deve conter 11 dígitos.");
        }
        this.cpf = cpf.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws ValidacaoException {
        if (email == null || !email.contains("@") || !email.contains(".") || email.trim().length() < 6) {
            throw new ValidacaoException("E-mail inválido! Verifique o formato.");
        }
        this.email = email.trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws ValidacaoException {
        if (telefone == null || telefone.replaceAll("[^0-9]", "").length() < 10) {
            throw new ValidacaoException("Telefone inválido! Informe DDD + número.");
        }
        this.telefone = telefone.trim();
    }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}