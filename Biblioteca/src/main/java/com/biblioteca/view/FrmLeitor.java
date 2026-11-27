package com.biblioteca.view;

import com.biblioteca.dao.LeitorDAO;
import com.biblioteca.model.Leitor;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmLeitor extends JFrame {
    private JTextField txtId, txtNome, txtCpf, txtEmail, txtTelefone, txtMatricula;
    private final LeitorDAO dao = new LeitorDAO();
    private final FrmPrincipal principal;

    public FrmLeitor(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Cadastro de Leitores");
        setSize(560, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Cadastro de Leitores", "Limite padrão de 3 empréstimos simultâneos"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        txtNome = EstiloUI.criarCampoTexto();
        txtCpf = EstiloUI.criarCampoTexto();
        txtEmail = EstiloUI.criarCampoTexto();
        txtTelefone = EstiloUI.criarCampoTexto();
        txtMatricula = EstiloUI.criarCampoTexto();

        form.add(EstiloUI.criarLabel("ID (informe para Consultar/Alterar/Excluir):"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Nome Completo:"));
        form.add(txtNome);
        form.add(EstiloUI.criarLabel("CPF (11 dígitos):"));
        form.add(txtCpf);
        form.add(EstiloUI.criarLabel("E-mail:"));
        form.add(txtEmail);
        form.add(EstiloUI.criarLabel("Telefone:"));
        form.add(txtTelefone);
        form.add(EstiloUI.criarLabel("Matrícula:"));
        form.add(txtMatricula);

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
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(principal, 0)));

        botoes.add(btnConsultar);
        botoes.add(btnInserir);
        botoes.add(btnAlterar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnListar);
        add(botoes, BorderLayout.SOUTH);
    }

    public void carregarPorId(int id) {
        txtId.setText(String.valueOf(id));
        consultar();
    }

    private void inserir() {
        try {
            Leitor l = new Leitor(null, txtNome.getText(), txtCpf.getText(),
                    txtEmail.getText(), txtTelefone.getText(), txtMatricula.getText());
            if (dao.inserir(l) > 0) {
                JOptionPane.showMessageDialog(this, "Leitor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do leitor para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Leitor l = dao.carregar(id);
            if (l == null) {
                JOptionPane.showMessageDialog(this, "Leitor não encontrado.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            txtNome.setText(l.getNome());
            txtCpf.setText(l.getCpf());
            txtEmail.setText(l.getEmail());
            txtTelefone.setText(l.getTelefone());
            txtMatricula.setText(l.getMatricula());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do leitor para alterar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Leitor l = new Leitor(id, txtNome.getText(), txtCpf.getText(),
                    txtEmail.getText(), txtTelefone.getText(), txtMatricula.getText());
            if (dao.alterar(l) > 0) {
                JOptionPane.showMessageDialog(this, "Leitor alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nenhum leitor encontrado com esse ID.", "Atenção", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Informe o ID do leitor para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o leitor ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Leitor excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                JOptionPane.showMessageDialog(this, "Leitor não encontrado para exclusão.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir (verifique se há empréstimos/reservas vinculados).\nDetalhes: " + ex.getMessage(),
                    "Erro ao Excluir", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtMatricula.setText("");
        txtId.requestFocus();
    }
}