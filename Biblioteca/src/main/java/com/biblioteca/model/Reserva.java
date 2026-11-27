package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;
import java.time.LocalDate;

public class Reserva {
    private Integer id;
    private Leitor leitor; // Associação
    private Obra obra;     // Associação
    private LocalDate dataReserva;
    private boolean ativa;

    public Reserva() {
        this.ativa = true;
    }

    public Reserva(Integer id, Leitor leitor, Obra obra, LocalDate dataReserva, boolean ativa) throws ValidacaoException {
        setId(id);
        setLeitor(leitor);
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

    public Leitor getLeitor() {
        return leitor;
    }

    public void setLeitor(Leitor leitor) throws ValidacaoException {
        if (leitor == null) {
            throw new ValidacaoException("Selecione um leitor para a reserva.");
        }
        this.leitor = leitor;
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