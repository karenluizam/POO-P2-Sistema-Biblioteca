package com.biblioteca.dao;

import com.biblioteca.model.Reserva;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final ObraDAO obraDAO = new ObraDAO();

    public int inserir(Reserva obj) throws Exception {
        String sql = "INSERT INTO reserva(id_leitor, id_obra, data_reserva, ativa) VALUES(?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getLeitor().getId());
            pstmt.setInt(2, obj.getObra().getId());
            pstmt.setString(3, obj.getDataReserva().toString());
            pstmt.setInt(4, obj.isAtiva() ? 1 : 0);
            return pstmt.executeUpdate();
        }
    }

    public int alterar(Reserva obj) throws Exception {
        String sql = "UPDATE reserva SET id_leitor=?, id_obra=?, data_reserva=?, ativa=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getLeitor().getId());
            pstmt.setInt(2, obj.getObra().getId());
            pstmt.setString(3, obj.getDataReserva().toString());
            pstmt.setInt(4, obj.isAtiva() ? 1 : 0);
            pstmt.setInt(5, obj.getId());
            return pstmt.executeUpdate();
        }
    }

    public int remover(int id) throws Exception {
        String sql = "DELETE FROM reserva WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }

    public Reserva carregar(int id) throws Exception {
        String sql = "SELECT * FROM reserva WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Reserva obj = new Reserva();
                    obj.setId(rs.getInt("id"));
                    obj.setLeitor(leitorDAO.carregar(rs.getInt("id_leitor")));
                    obj.setObra(obraDAO.carregar(rs.getInt("id_obra")));
                    obj.setDataReserva(LocalDate.parse(rs.getString("data_reserva")));
                    obj.setAtiva(rs.getInt("ativa") == 1);
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Reserva> listar() throws Exception {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reserva ORDER BY id DESC";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Reserva obj = new Reserva();
                obj.setId(rs.getInt("id"));
                obj.setLeitor(leitorDAO.carregar(rs.getInt("id_leitor")));
                obj.setObra(obraDAO.carregar(rs.getInt("id_obra")));
                obj.setDataReserva(LocalDate.parse(rs.getString("data_reserva")));
                obj.setAtiva(rs.getInt("ativa") == 1);
                lista.add(obj);
            }
        }
        return lista;
    }
}