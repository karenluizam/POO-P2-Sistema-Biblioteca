package com.biblioteca.view;

import com.biblioteca.dao.CopiaDAO;
import com.biblioteca.dao.ObraDAO;
import com.biblioteca.model.Copia;
import com.biblioteca.model.Obra;
import com.biblioteca.model.SituacaoCopia;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmCopia extends JFrame {
    private JTextField txtId, txtTombo;
    private JComboBox<Obra> cbObra;
    private JComboBox<SituacaoCopia> cbSituacao;
    private final CopiaDAO dao = new CopiaDAO();
    private final ObraDAO obraDAO = new ObraDAO();
    private final FrmPrincipal principal;

    public FrmCopia(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Cadastro de Cópias (Exemplares)");
        setSize(560, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Cadastro de Cópias", "Exemplares físicos vinculados às obras do acervo"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 12));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        cbObra = new JComboBox<>();
        txtTombo = EstiloUI.criarCampoTexto();
        cbSituacao = new JComboBox<>(SituacaoCopia.values());

        form.add(EstiloUI.criarLabel("ID da Cópia:"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Obra Vinculada:"));
        form.add(cbObra);
        form.add(EstiloUI.criarLabel("Código de Tombo:"));
        form.add(txtTombo);
        form.add(EstiloUI.criarLabel("Situação:"));
        form.add(cbSituacao);

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
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(3)));

        botoes.add(btnConsultar);
        botoes.add(btnInserir);
        botoes.add(btnAlterar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnListar);
        add(botoes, BorderLayout.SOUTH);

        carregarObras();
    }

    private void carregarObras() {
        try {
            cbObra.removeAllItems();
            for (Obra o : obraDAO.listar()) {
                cbObra.addItem(o);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar obras: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selecionarObraNoCombo(int idObra) {
        for (int i = 0; i < cbObra.getItemCount(); i++) {
            if (cbObra.getItemAt(i).getId() == idObra) {
                cbObra.setSelectedIndex(i);
                return;
            }
        }
    }

    private void inserir() {
        try {
            Obra obraSelecionada = (Obra) cbObra.getSelectedItem();
            SituacaoCopia sit = (SituacaoCopia) cbSituacao.getSelectedItem();
            Copia c = new Copia(null, obraSelecionada, txtTombo.getText(), sit);
            if (dao.inserir(c) > 0) {
                JOptionPane.showMessageDialog(this, "Cópia cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da cópia para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Copia c = dao.carregar(id);
            if (c == null) {
                JOptionPane.showMessageDialog(this, "Cópia não encontrada.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            selecionarObraNoCombo(c.getObra().getId());
            txtTombo.setText(c.getCodigoTombo());
            cbSituacao.setSelectedItem(c.getSituacao());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da cópia para alterar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Obra obraSelecionada = (Obra) cbObra.getSelectedItem();
            SituacaoCopia sit = (SituacaoCopia) cbSituacao.getSelectedItem();
            Copia c = new Copia(id, obraSelecionada, txtTombo.getText(), sit);
            if (dao.alterar(c) > 0) {
                JOptionPane.showMessageDialog(this, "Cópia alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cópia não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da cópia para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a cópia ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Cópia excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                JOptionPane.showMessageDialog(this, "Cópia não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        txtId.setText("");
        txtTombo.setText("");
        cbSituacao.setSelectedIndex(0);
        if (cbObra.getItemCount() > 0) cbObra.setSelectedIndex(0);
        txtId.requestFocus();
    }
}