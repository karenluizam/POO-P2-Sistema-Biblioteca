package com.biblioteca.model;

import com.biblioteca.exception.ValidacaoException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class Obra {
    private Integer id;
    private String titulo;
    private String autor;
    private String editora;
    private int ano;
    private String isbn;

    // Agregação: uma Obra contém uma coleção de exemplares físicos (Copia)
    private List<Copia> copias = new ArrayList<>();

    public Obra() {
    }

    public Obra(Integer id, String titulo, String autor, String editora, int ano, String isbn) throws ValidacaoException {
        setId(id);
        setTitulo(titulo);
        setAutor(autor);
        setEditora(editora);
        setAno(ano);
        setIsbn(isbn);
    }

    public void adicionarCopia(Copia copia) throws ValidacaoException {
        if (copia == null) {
            throw new ValidacaoException("A cópia não pode ser nula.");
        }
        copia.setObra(this);
        this.copias.add(copia);
    }

    public int getQuantidadeCopiasDisponiveis() {
        int disponiveis = 0;
        for (Copia c : copias) {
            if (c.isDisponivel()) {
                disponiveis++;
            }
        }
        return disponiveis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) throws ValidacaoException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacaoException("O título da obra é obrigatório.");
        }
        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) throws ValidacaoException {
        if (autor == null || autor.trim().isEmpty()) {
            throw new ValidacaoException("O autor da obra é obrigatório.");
        }
        this.autor = autor.trim();
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) throws ValidacaoException {
        if (editora == null || editora.trim().isEmpty()) {
            throw new ValidacaoException("A editora da obra é obrigatória.");
        }
        this.editora = editora.trim();
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) throws ValidacaoException {
        int anoAtual = Year.now().getValue();
        if (ano < 1000 || ano > anoAtual) {
            throw new ValidacaoException("Ano de publicação inválido! Informe entre 1000 e " + anoAtual + ".");
        }
        this.ano = ano;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) throws ValidacaoException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidacaoException("O ISBN da obra é obrigatório.");
        }
        this.isbn = isbn.trim();
    }

    public List<Copia> getCopias() {
        return copias;
    }

    public void setCopias(List<Copia> copias) {
        this.copias = (copias != null) ? copias : new ArrayList<>();
    }

    @Override
    public String toString() {
        return titulo + " (" + autor + ")";
    }
}
