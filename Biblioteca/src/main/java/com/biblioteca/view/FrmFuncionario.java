package com.biblioteca.view;

import com.biblioteca.dao.FuncionarioDAO;
import com.biblioteca.model.Funcionario;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmFuncionario extends JFrame {
    private JTextField txtId, txtNome, txtCpf, txtEmail, txtTelefone, txtCargo, txtSalario;
    private JComboBox<String> cbTurno;
    private final FuncionarioDAO dao = new FuncionarioDAO();
    private final FrmPrincipal principal;

    public FrmFuncionario(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Cadastro de Funcionários");
        setSize(580, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Cadastro de Funcionários", "Controle de equipe e turnos da biblioteca"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 8));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(15, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        txtNome = EstiloUI.criarCampoTexto();
        txtCpf = EstiloUI.criarCampoTexto();
        txtEmail = EstiloUI.criarCampoTexto();
        txtTelefone = EstiloUI.criarCampoTexto();
        cbTurno = new JComboBox<>(new String[]{"M - Manhã", "T - Tarde", "N - Noite"});
        txtCargo = EstiloUI.criarCampoTexto();
        txtSalario = EstiloUI.criarCampoTexto();

        form.add(EstiloUI.criarLabel("ID:"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Nome:"));
        form.add(txtNome);
        form.add(EstiloUI.criarLabel("CPF (11 dígitos):"));
        form.add(txtCpf);
        form.add(EstiloUI.criarLabel("E-mail:"));
        form.add(txtEmail);
        form.add(EstiloUI.criarLabel("Telefone:"));
        form.add(txtTelefone);
        form.add(EstiloUI.criarLabel("Turno:"));
        form.add(cbTurno);
        form.add(EstiloUI.criarLabel("Cargo:"));
        form.add(txtCargo);
        form.add(EstiloUI.criarLabel("Salário (R$):"));
        form.add(txtSalario);

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
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(principal, 1)));

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

    private char getTurnoSelecionado() {
        return cbTurno.getSelectedItem().toString().charAt(0);
    }

    private void inserir() {
        try {
            double salario = Double.parseDouble(txtSalario.getText().trim().replace(",", "."));
            Funcionario f = new Funcionario(null, txtNome.getText(), txtCpf.getText(),
                    txtEmail.getText(), txtTelefone.getText(), getTurnoSelecionado(), txtCargo.getText(), salario);
            if (dao.inserir(f) > 0) {
                JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um valor numérico válido para o salário.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Funcionario f = dao.carregar(id);
            if (f == null) {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            txtNome.setText(f.getNome());
            txtCpf.setText(f.getCpf());
            txtEmail.setText(f.getEmail());
            txtTelefone.setText(f.getTelefone());
            txtCargo.setText(f.getCargo());
            txtSalario.setText(String.valueOf(f.getSalario()));
            if (f.getTurno() == 'M') cbTurno.setSelectedIndex(0);
            else if (f.getTurno() == 'T') cbTurno.setSelectedIndex(1);
            else cbTurno.setSelectedIndex(2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID para alterar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            double salario = Double.parseDouble(txtSalario.getText().trim().replace(",", "."));
            Funcionario f = new Funcionario(id, txtNome.getText(), txtCpf.getText(),
                    txtEmail.getText(), txtTelefone.getText(), getTurnoSelecionado(), txtCargo.getText(), salario);
            if (dao.alterar(f) > 0) {
                JOptionPane.showMessageDialog(this, "Funcionário alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique os valores numéricos de ID e Salário.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o funcionário ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Funcionário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                JOptionPane.showMessageDialog(this, "Funcionário não encontrado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limpar() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        cbTurno.setSelectedIndex(0);
        txtCargo.setText("");
        txtSalario.setText("");
        txtId.requestFocus();
    }
}
