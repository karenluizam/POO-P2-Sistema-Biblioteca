package com.biblioteca.dao;

import com.biblioteca.exception.ValidacaoException;
import com.biblioteca.model.Copia;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.SituacaoCopia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final CopiaDAO copiaDAO = new CopiaDAO();

    public int inserir(Emprestimo obj) throws Exception {
        if (!obj.getCopia().isDisponivel()) {
            throw new ValidacaoException("Esta cópia não está disponível para empréstimo (" + obj.getCopia().getSituacao() + ").");
        }
        int ativos = contarEmprestimosAtivosDoLeitor(obj.getLeitor().getId());
        if (ativos >= obj.getLeitor().getLimiteEmprestimos()) {
            throw new ValidacaoException("O leitor já atingiu o limite de " + obj.getLeitor().getLimiteEmprestimos() + " empréstimos ativos.");
        }

        String sql = "INSERT INTO emprestimo(id_leitor, id_funcionario, id_copia, data_emprestimo, data_prevista, data_devolucao) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getLeitor().getId());
            pstmt.setInt(2, obj.getFuncionario().getId());
            pstmt.setInt(3, obj.getCopia().getId());
            pstmt.setString(4, obj.getDataEmprestimo().toString());
            pstmt.setString(5, obj.getDataPrevistaDevolucao().toString());
            pstmt.setString(6, obj.getDataDevolucaoReal() != null ? obj.getDataDevolucaoReal().toString() : null);
            int linhas = pstmt.executeUpdate();

            if (linhas > 0) {
                Copia copia = obj.getCopia();
                copia.setSituacao(SituacaoCopia.EMPRESTADA);
                copiaDAO.alterar(copia);
            }
            return linhas;
        }
    }

    public int registrarDevolucao(int idEmprestimo, LocalDate dataDevolucao) throws Exception {
        Emprestimo emp = carregar(idEmprestimo);
        if (emp == null) {
            throw new ValidacaoException("Empréstimo não encontrado.");
        }
        if (emp.isDevolvido()) {
            throw new ValidacaoException("Este empréstimo já foi devolvido.");
        }
        emp.setDataDevolucaoReal(dataDevolucao);

        String sql = "UPDATE emprestimo SET data_devolucao=? WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.getDataDevolucaoReal().toString());
            pstmt.setInt(2, idEmprestimo);
            int linhas = pstmt.executeUpdate();

            if (linhas > 0) {
                Copia copia = emp.getCopia();
                copia.setSituacao(SituacaoCopia.DISPONIVEL);
                copiaDAO.alterar(copia);
            }
            return linhas;
        }
    }

    public int remover(int id) throws Exception {
        Emprestimo emp = carregar(id);
        String sql = "DELETE FROM emprestimo WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0 && emp != null && !emp.isDevolvido()) {
                Copia copia = emp.getCopia();
                copia.setSituacao(SituacaoCopia.DISPONIVEL);
                copiaDAO.alterar(copia);
            }
            return linhas;
        }
    }

    public int contarEmprestimosAtivosDoLeitor(int idLeitor) throws Exception {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE id_leitor=? AND data_devolucao IS NULL";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idLeitor);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public Emprestimo carregar(int id) throws Exception {
        String sql = "SELECT * FROM emprestimo WHERE id=?";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Emprestimo obj = new Emprestimo();
                    obj.setId(rs.getInt("id"));
                    obj.setLeitor(leitorDAO.carregar(rs.getInt("id_leitor")));
                    obj.setFuncionario(funcionarioDAO.carregar(rs.getInt("id_funcionario")));
                    obj.setCopia(copiaDAO.carregar(rs.getInt("id_copia")));
                    obj.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                    obj.setDataPrevistaDevolucao(LocalDate.parse(rs.getString("data_prevista")));
                    String dev = rs.getString("data_devolucao");
                    if (dev != null) {
                        obj.setDataDevolucaoReal(LocalDate.parse(dev));
                    }
                    return obj;
                }
            }
        }
        return null;
    }

    public List<Emprestimo> listar() throws Exception {
        List<Emprestimo> lista = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo ORDER BY id DESC";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Emprestimo obj = new Emprestimo();
                obj.setId(rs.getInt("id"));
                obj.setLeitor(leitorDAO.carregar(rs.getInt("id_leitor")));
                obj.setFuncionario(funcionarioDAO.carregar(rs.getInt("id_funcionario")));
                obj.setCopia(copiaDAO.carregar(rs.getInt("id_copia")));
                obj.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                obj.setDataPrevistaDevolucao(LocalDate.parse(rs.getString("data_prevista")));
                String dev = rs.getString("data_devolucao");
                if (dev != null) {
                    obj.setDataDevolucaoReal(LocalDate.parse(dev));
                }
                lista.add(obj);
            }
        }
        return lista;
    }
}