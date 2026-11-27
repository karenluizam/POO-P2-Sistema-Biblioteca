package com.biblioteca.dao;

import com.biblioteca.exception.ValidacaoException;
import com.biblioteca.model.Copia;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.SituacaoCopia;
import com.biblioteca.model.Pessoa;
import com.biblioteca.model.Funcionario;
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
        String tipoPessoa = (obj.getSolicitante() instanceof Funcionario) ? "FUNCIONARIO" : "LEITOR";
        int ativos = contarEmprestimosAtivos(obj.getSolicitante().getId(), tipoPessoa);

        // Polimorfismo em ação: chama getLimiteEmprestimos() de Leitor (3) ou Funcionario (5)
        if (ativos >= obj.getSolicitante().getLimiteEmprestimos()) {
            throw new ValidacaoException("O solicitante já atingiu o limite de " + obj.getSolicitante().getLimiteEmprestimos() + " empréstimos ativos.");
        }

        String sql = "INSERT INTO emprestimo(id_pessoa, tipo_pessoa, id_funcionario, id_copia, data_emprestimo, data_prevista, data_devolucao) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, obj.getSolicitante().getId());
            pstmt.setString(2, tipoPessoa);
            pstmt.setInt(3, obj.getFuncionario().getId());
            pstmt.setInt(4, obj.getCopia().getId());
            pstmt.setString(5, obj.getDataEmprestimo().toString());
            pstmt.setString(6, obj.getDataPrevistaDevolucao().toString());
            pstmt.setString(7, obj.getDataDevolucaoReal() != null ? obj.getDataDevolucaoReal().toString() : null);
            int linhas = pstmt.executeUpdate();

            if (linhas > 0) {
                Copia copia = obj.getCopia();
                copia.setSituacao(SituacaoCopia.EMPRESTADA);
                copiaDAO.alterar(copia);
            }
            return linhas;
        }
    }

    public int contarEmprestimosAtivos(int idPessoa, String tipoPessoa) throws Exception {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE id_pessoa=? AND tipo_pessoa=? AND data_devolucao IS NULL";
        try (Connection conn = ConexaoSQLite.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPessoa);
            pstmt.setString(2, tipoPessoa);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private Pessoa carregarPessoa(int idPessoa, String tipoPessoa) throws Exception {
        if ("FUNCIONARIO".equals(tipoPessoa)) {
            return funcionarioDAO.carregar(idPessoa);
        }
        return leitorDAO.carregar(idPessoa);
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
                    obj.setSolicitante(carregarPessoa(rs.getInt("id_pessoa"), rs.getString("tipo_pessoa")));
                    obj.setFuncionario(funcionarioDAO.carregar(rs.getInt("id_funcionario")));
                    obj.setCopia(copiaDAO.carregar(rs.getInt("id_copia")));
                    obj.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                    obj.setDataPrevistaDevolucao(LocalDate.parse(rs.getString("data_prevista")));
                    String dev = rs.getString("data_devolucao");
                    if (dev != null) obj.setDataDevolucaoReal(LocalDate.parse(dev));
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
                obj.setSolicitante(carregarPessoa(rs.getInt("id_pessoa"), rs.getString("tipo_pessoa")));
                obj.setFuncionario(funcionarioDAO.carregar(rs.getInt("id_funcionario")));
                obj.setCopia(copiaDAO.carregar(rs.getInt("id_copia")));
                obj.setDataEmprestimo(LocalDate.parse(rs.getString("data_emprestimo")));
                obj.setDataPrevistaDevolucao(LocalDate.parse(rs.getString("data_prevista")));
                String dev = rs.getString("data_devolucao");
                if (dev != null) obj.setDataDevolucaoReal(LocalDate.parse(dev));
                lista.add(obj);
            }
        }
        return lista;
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
}


