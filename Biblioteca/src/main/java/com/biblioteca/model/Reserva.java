package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;
import java.time.LocalDate;

public class Reserva {
    private Integer id;
    private Pessoa solicitante; // Associação polimósfica
    private Obra obra;     // Associação
    private LocalDate dataReserva;
    private boolean ativa;

    public Reserva() {
        this.ativa = true;
    }

    public Reserva(Integer id, Pessoa solicitante, Obra obra, LocalDate dataReserva, boolean ativa) throws ValidacaoException {
        setId(id);
        setSolicitante(solicitante);
        setObra(obra);
        setDataReserva(dataReserva);
        setAtiva(ativa);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Pessoa solicitante) throws ValidacaoException {
        if (solicitante == null) {
            throw new ValidacaoException("Selecione quem está fazendo a reserva (Leitor ou Funcionário).");
        }
        this.solicitante = solicitante;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) throws ValidacaoException {
        if (obra == null) {
            throw new ValidacaoException("Selecione uma obra para a reserva.");
        }
        this.obra = obra;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) throws ValidacaoException {
        if (dataReserva == null) {
            throw new ValidacaoException("A data da reserva é obrigatória.");
        }
        this.dataReserva = dataReserva;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}