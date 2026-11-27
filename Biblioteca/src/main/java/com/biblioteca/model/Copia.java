package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;

public class Copia {
    private Integer id;
    private Obra obra; // Associação com Obra
    private String codigoTombo;
    private SituacaoCopia situacao;

    public Copia() {
        this.situacao = SituacaoCopia.DISPONIVEL;
    }

    public Copia(Integer id, Obra obra, String codigoTombo, SituacaoCopia situacao) throws ValidacaoException {
        setId(id);
        setObra(obra);
        setCodigoTombo(codigoTombo);
        setSituacao(situacao != null ? situacao : SituacaoCopia.DISPONIVEL);
    }

    public boolean isDisponivel() {
        return this.situacao == SituacaoCopia.DISPONIVEL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) throws ValidacaoException {
        if (obra == null) {
            throw new ValidacaoException("Toda cópia deve estar vinculada a uma obra.");
        }
        this.obra = obra;
    }

    public String getCodigoTombo() {
        return codigoTombo;
    }

    public void setCodigoTombo(String codigoTombo) throws ValidacaoException {
        if (codigoTombo == null || codigoTombo.trim().isEmpty()) {
            throw new ValidacaoException("O código de tombo da cópia é obrigatório.");
        }
        this.codigoTombo = codigoTombo.trim();
    }

    public SituacaoCopia getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoCopia situacao) throws ValidacaoException {
        if (situacao == null) {
            throw new ValidacaoException("A situação da cópia é obrigatória.");
        }
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        String nomeObra = (obra != null) ? obra.getTitulo() : "Sem obra";
        return nomeObra + " [Tombo: " + codigoTombo + " - " + situacao + "]";
    }
}