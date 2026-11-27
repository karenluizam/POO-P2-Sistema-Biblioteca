package com.biblioteca.dao;

import com.biblioteca.model.Copia;
import com.biblioteca.model.Obra;
import com.biblioteca.model.SituacaoCopia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CopiaDAO {
    private final ObraDAO obraDAO = new ObraDAO();

    public int inserir(Copia obj) throws Exception {
        String sql = "INSERT INTO copia(id_obra, codigo_tombo, situacao) VALUES(?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getObra().getId());
            pstmt.setString(2, obj.getCodigoTombo());
            pstmt.setString(3, obj.getSituacao().name());
            return pstmt.executeUpdate();
        }
    }

    public int alterar(Copia obj) throws Exception {
        String sql = "UPDATE copia SET id_obra=?, codigo_tombo=?, situacao=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getObra().getId());
            pstmt.setString(2, obj.getCodigoTombo());
            pstmt.setString(3, obj.getSituacao().name());
            pstmt.setInt(4, obj.getId());
            return pstmt.executeUpdate();
        }
    }

    public int remover(int id) throws Exception {
        String sql = "DELETE FROM copia WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public Copia carregar(int id) throws Exception {
        String sql = "SELECT * FROM copia WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Copia obj = new Copia();
                    obj.setId(rs.getInt("id"));
                    Obra obra = obraDAO.carregar(rs.getInt("id_obra"));
                    obj.setObra(obra);
                    obj.setCodigoTombo(rs.getString("codigo_tombo"));
                    obj.setSituacao(SituacaoCopia.valueOf(rs.getString("situacao")));
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Copia> listar() throws Exception {
        List<Copia> lista = new ArrayList<>();
        String sql = "SELECT c.* FROM copia c "
                + "INNER JOIN obra o ON c.id_obra = o.id "
                + "ORDER BY o.titulo ASC, c.codigo_tombo ASC";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Copia obj = new Copia();
                obj.setId(rs.getInt("id"));
                Obra obra = obraDAO.carregar(rs.getInt("id_obra"));
                obj.setObra(obra);
                obj.setCodigoTombo(rs.getString("codigo_tombo"));
                obj.setSituacao(SituacaoCopia.valueOf(rs.getString("situacao")));
                lista.add(obj);
            }
        }
        return lista;
    }
}