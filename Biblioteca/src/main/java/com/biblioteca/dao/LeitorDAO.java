package com.biblioteca.dao;

import com.biblioteca.model.Leitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeitorDAO {

    public int inserir(Leitor obj) throws Exception {
        String sql = "INSERT INTO leitor(nome, cpf, email, telefone, matricula) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getNome());
            pstmt.setString(2, obj.getCpf());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getTelefone());
            pstmt.setString(5, obj.getMatricula());
            return pstmt.executeUpdate();
        }
    }

    public int alterar(Leitor obj) throws Exception {
        String sql = "UPDATE leitor SET nome=?, cpf=?, email=?, telefone=?, matricula=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getNome());
            pstmt.setString(2, obj.getCpf());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getTelefone());
            pstmt.setString(5, obj.getMatricula());
            pstmt.setInt(6, obj.getId());
            return pstmt.executeUpdate();
        }
    }

    public int remover(int id) throws Exception {
        String sql = "DELETE FROM leitor WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public Leitor carregar(int id) throws Exception {
        String sql = "SELECT * FROM leitor WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Leitor obj = new Leitor();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setCpf(rs.getString("cpf"));
                    obj.setEmail(rs.getString("email"));
                    obj.setTelefone(rs.getString("telefone"));
                    obj.setMatricula(rs.getString("matricula"));
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Leitor> listar() throws Exception {
        List<Leitor> lista = new ArrayList<>();
        String sql = "SELECT * FROM leitor ORDER BY nome";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Leitor obj = new Leitor();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setCpf(rs.getString("cpf"));
                obj.setEmail(rs.getString("email"));
                obj.setTelefone(rs.getString("telefone"));
                obj.setMatricula(rs.getString("matricula"));
                lista.add(obj);
            }
        }
        return lista;
    }
}