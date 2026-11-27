package com.biblioteca.view;

import com.biblioteca.dao.ObraDAO;
import com.biblioteca.model.Obra;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmObra extends JFrame {
    private JTextField txtId, txtTitulo, txtAutor, txtEditora, txtAno, txtIsbn;
    private final ObraDAO dao = new ObraDAO();
    private final FrmPrincipal principal;

    public FrmObra(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Cadastro de Obras");
        setSize(560, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Cadastro de Obras", "Catálogo geral de títulos do acervo"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        txtTitulo = EstiloUI.criarCampoTexto();
        txtAutor = EstiloUI.criarCampoTexto();
        txtEditora = EstiloUI.criarCampoTexto();
        txtAno = EstiloUI.criarCampoTexto();
        txtIsbn = EstiloUI.criarCampoTexto();

        form.add(EstiloUI.criarLabel("ID:"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Título:"));
        form.add(txtTitulo);
        form.add(EstiloUI.criarLabel("Autor:"));
        form.add(txtAutor);
        form.add(EstiloUI.criarLabel("Editora:"));
        form.add(txtEditora);
        form.add(EstiloUI.criarLabel("Ano de Publicação:"));
        form.add(txtAno);
        form.add(EstiloUI.criarLabel("ISBN:"));
        form.add(txtIsbn);

        add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        botoes.setBackground(EstiloUI.COR_FUNDO);

        JButton btnConsultar = EstiloUI.criarBotao("Consultar", EstiloUI.COR_DESTAQUE);
        JButton btnInserir   = EstiloUI.criarBotao("Inserir", EstiloUI.COR_SUCESSO);
        JButton btnAlterar   = EstiloUI.criarBotao("Alterar", EstiloUI.COR_PRIMARIA);
        JButton btnExcluir   = EstiloUI.criarBotao("Excluir", EstiloUI.COR_PERIGO);
        JButton btnLimpar    = EstiloUI.criarBotao("Limpar", EstiloUI.COR_NEUTRA);
        JButton btnListar    = EstiloUI.criarBotao("Listar", EstiloUI.COR_NEUTRA);

        btnConsultar.addActionListener(e -> consultar());
        btnInserir.addActionListener(e -> inserir());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(2)));

        botoes.add(btnConsultar);
        botoes.add(btnInserir);
        botoes.add(btnAlterar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnListar);
        add(botoes, BorderLayout.SOUTH);
    }

    private void inserir() {
        try {
            int ano = Integer.parseInt(txtAno.getText().trim());
            Obra o = new Obra(null, txtTitulo.getText(), txtAutor.getText(),
                    txtEditora.getText(), ano, txtIsbn.getText());
            if (dao.inserir(o) > 0) {
                JOptionPane.showMessageDialog(this, "Obra cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ano válido em números inteiros.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da obra para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Obra o = dao.carregar(id);
            if (o == null) {
                JOptionPane.showMessageDialog(this, "Obra não encontrada.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            txtTitulo.setText(o.getTitulo());
            txtAutor.setText(o.getAutor());
            txtEditora.setText(o.getEditora());
            txtAno.setText(String.valueOf(o.getAno()));
            txtIsbn.setText(o.getIsbn());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da obra para alterar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int ano = Integer.parseInt(txtAno.getText().trim());
            Obra o = new Obra(id, txtTitulo.getText(), txtAutor.getText(),
                    txtEditora.getText(), ano, txtIsbn.getText());
            if (dao.alterar(o) > 0) {
                JOptionPane.showMessageDialog(this, "Obra alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Obra não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos de ID e Ano.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da obra para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a obra ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Obra excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                JOptionPane.showMessageDialog(this, "Obra não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir (verifique se existem cópias ou reservas vinculadas).\nDetalhes: " + ex.getMessage(),
                    "Erro ao Excluir", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        txtId.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditora.setText("");
        txtAno.setText("");
        txtIsbn.setText("");
        txtId.requestFocus();
    }
}