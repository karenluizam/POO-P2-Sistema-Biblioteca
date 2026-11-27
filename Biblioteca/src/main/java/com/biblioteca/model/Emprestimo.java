package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;
import java.time.LocalDate;

public class Emprestimo {
    private Integer id;
    private Pessoa solicitante; // Associação polimórfica (aceita Leitor ou Funcionario)
    private Funcionario funcionario; // Associação
    private Copia copia;             // Associação
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal;

    public Emprestimo() {
    }

    public Emprestimo(Integer id, Pessoa solicitante, Funcionario funcionario, Copia copia,
                      LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) throws ValidacaoException {
        setId(id);
        setSolicitante(solicitante);
        setFuncionario(funcionario);
        setCopia(copia);
        setDataEmprestimo(dataEmprestimo);
        setDataPrevistaDevolucao(dataPrevistaDevolucao);
    }

    public Pessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Pessoa solicitante) throws ValidacaoException {
        if (solicitante == null) {
            throw new ValidacaoException("Selecione quem está pegando o livro (Leitor ou Funcionário).");
        }
        this.solicitante = solicitante;
    }

    public boolean isDevolvido() {
        return dataDevolucaoReal != null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) throws ValidacaoException {
        if (funcionario == null) {
            throw new ValidacaoException("Selecione o funcionário responsável pelo empréstimo.");
        }
        this.funcionario = funcionario;
    }

    public Copia getCopia() {
        return copia;
    }

    public void setCopia(Copia copia) throws ValidacaoException {
        if (copia == null) {
            throw new ValidacaoException("Selecione uma cópia para o empréstimo.");
        }
        this.copia = copia;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) throws ValidacaoException {
        if (dataEmprestimo == null) {
            throw new ValidacaoException("A data do empréstimo é obrigatória.");
        }
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) throws ValidacaoException {
        if (dataPrevistaDevolucao == null) {
            throw new ValidacaoException("A data prevista de devolução é obrigatória.");
        }
        if (this.dataEmprestimo != null && dataPrevistaDevolucao.isBefore(this.dataEmprestimo)) {
            throw new ValidacaoException("A data prevista não pode ser anterior à data do empréstimo.");
        }
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) throws ValidacaoException {
        if (dataDevolucaoReal != null && this.dataEmprestimo != null && dataDevolucaoReal.isBefore(this.dataEmprestimo)) {
            throw new ValidacaoException("A data de devolução não pode ser anterior ao empréstimo.");
        }
        this.dataDevolucaoReal = dataDevolucaoReal;
    }
}