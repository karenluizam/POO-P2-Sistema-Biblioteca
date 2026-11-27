package com.biblioteca.dao;

import com.biblioteca.model.Funcionario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public int inserir(Funcionario obj) throws Exception {
        String sql = "INSERT INTO funcionario(nome, cpf, email, telefone, turno, cargo, salario) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getNome());
            pstmt.setString(2, obj.getCpf());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getTelefone());
            pstmt.setString(5, String.valueOf(obj.getTurno()));
            pstmt.setString(6, obj.getCargo());
            pstmt.setDouble(7, obj.getSalario());
            return pstmt.executeUpdate();
        }
    }

    public int alterar(Funcionario obj) throws Exception {
        String sql = "UPDATE funcionario SET nome=?, cpf=?, email=?, telefone=?, turno=?, cargo=?, salario=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, obj.getNome());
            pstmt.setString(2, obj.getCpf());
            pstmt.setString(3, obj.getEmail());
            pstmt.setString(4, obj.getTelefone());
            pstmt.setString(5, String.valueOf(obj.getTurno()));
            pstmt.setString(6, obj.getCargo());
            pstmt.setDouble(7, obj.getSalario());
            pstmt.setInt(8, obj.getId());
            return pstmt.executeUpdate();
        }
    }

    public int remover(int id) throws Exception {
        String sql = "DELETE FROM funcionario WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public Funcionario carregar(int id) throws Exception {
        String sql = "SELECT * FROM funcionario WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario obj = new Funcionario();
                    obj.setId(rs.getInt("id"));
                    obj.setNome(rs.getString("nome"));
                    obj.setCpf(rs.getString("cpf"));
                    obj.setEmail(rs.getString("email"));
                    obj.setTelefone(rs.getString("telefone"));
                    obj.setTurno(rs.getString("turno").charAt(0));
                    obj.setCargo(rs.getString("cargo"));
                    obj.setSalario(rs.getDouble("salario"));
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Funcionario> listar() throws Exception {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario ORDER BY nome";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Funcionario obj = new Funcionario();
                obj.setId(rs.getInt("id"));
                obj.setNome(rs.getString("nome"));
                obj.setCpf(rs.getString("cpf"));
                obj.setEmail(rs.getString("email"));
                obj.setTelefone(rs.getString("telefone"));
                obj.setTurno(rs.getString("turno").charAt(0));
                obj.setCargo(rs.getString("cargo"));
                obj.setSalario(rs.getDouble("salario"));
                lista.add(obj);
            }
        }
        return lista;
    }
}