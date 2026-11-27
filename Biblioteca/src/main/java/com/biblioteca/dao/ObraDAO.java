package com.biblioteca.dao;

import com.biblioteca.model.Obra;
import com.biblioteca.model.Copia;
import com.biblioteca.model.SituacaoCopia;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {

    public int inserir(Obra obj) throws Exception {
        String sqlObra = "INSERT INTO obra(titulo, autor, editora, ano, isbn) VALUES(?, ?, ?, ?, ?)";
        String sqlCopia = "INSERT INTO copia(id_obra, codigo_tombo, situacao) VALUES(?, ?, ?)";

        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlObra, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, obj.getTitulo());
            pstmt.setString(2, obj.getAutor());
            pstmt.setString(3, obj.getEditora());
            pstmt.setInt(4, obj.getAno());
            pstmt.setString(5, obj.getIsbn());
            int linhas = pstmt.executeUpdate();

            if (linhas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idObra = rs.getInt(1);
                        obj.setId(idObra);

                        // Cria automaticamente o 1º exemplar (Cópia #1) da obra
                        String tomboPadrao = "TOMBO-" + idObra + "-1";
                        Copia primeiraCopia = new Copia(null, obj, tomboPadrao, SituacaoCopia.DISPONIVEL);
                        obj.adicionarCopia(primeiraCopia);

                        try (PreparedStatement pstmtCopia = conn.prepareStatement(sqlCopia)) {
                            pstmtCopia.setInt(1, idObra);
                            pstmtCopia.setString(2, primeiraCopia.getCodigoTombo());
                            pstmtCopia.setString(3, primeiraCopia.getSituacao().name());
                            pstmtCopia.executeUpdate();
                        }
                    }
                }
            }
            return linhas;
        }
    }

    public int alterar(Obra obj) throws Exception {
        String sql = "UPDATE obra SET titulo=?, autor=?, editora=?, ano=?, isbn=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getTitulo());
            pstmt.setString(2, obj.getAutor());
            pstmt.setString(3, obj.getEditora());
            pstmt.setInt(4, obj.getAno());
            pstmt.setString(5, obj.getIsbn());
            pstmt.setInt(6, obj.getId());
            return pstmt.executeUpdate();
        }
    }

    public int remover(int id) throws Exception {
        String sql = "DELETE FROM obra WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public Obra carregar(int id) throws Exception {
        String sql = "SELECT * FROM obra WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Obra obj = new Obra();
                    obj.setId(rs.getInt("id"));
                    obj.setTitulo(rs.getString("titulo"));
                    obj.setAutor(rs.getString("autor"));
                    obj.setEditora(rs.getString("editora"));
                    obj.setAno(rs.getInt("ano"));
                    obj.setIsbn(rs.getString("isbn"));
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Obra> listar() throws Exception {
        List<Obra> lista = new ArrayList<>();
        String sql = "SELECT * FROM obra ORDER BY titulo";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Obra obj = new Obra();
                obj.setId(rs.getInt("id"));
                obj.setTitulo(rs.getString("titulo"));
                obj.setAutor(rs.getString("autor"));
                obj.setEditora(rs.getString("editora"));
                obj.setAno(rs.getInt("ano"));
                obj.setIsbn(rs.getString("isbn"));
                lista.add(obj);
            }
        }
        return lista;
    }
}