package com.biblioteca.view;

import com.biblioteca.dao.FuncionarioDAO;
import com.biblioteca.model.Funcionario;
import com.biblioteca.model.Pessoa;
import com.biblioteca.dao.LeitorDAO;
import com.biblioteca.dao.ObraDAO;
import com.biblioteca.dao.ReservaDAO;
import com.biblioteca.model.Leitor;
import com.biblioteca.model.Obra;
import com.biblioteca.model.Reserva;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmReserva extends JFrame {
    private JTextField txtId, txtDataReserva;
    private JComboBox<Pessoa> cbSolicitante;
    private final FuncionarioDAO funcDAO = new FuncionarioDAO();
    private JComboBox<Obra> cbObra;
    private JCheckBox chkAtiva;

    private final ReservaDAO dao = new ReservaDAO();
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final ObraDAO obraDAO = new ObraDAO();
    private final FrmPrincipal principal;

    public FrmReserva(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Controle de Reservas");
        setSize(580, 390);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Controle de Reservas", "Solicitação de reserva de obras pelos leitores"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(18, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        cbSolicitante = new JComboBox<>();
        cbObra = new JComboBox<>();
        txtDataReserva = EstiloUI.criarCampoTexto();
        txtDataReserva.setText(LocalDate.now().toString());
        chkAtiva = new JCheckBox("Reserva Ativa", true);
        chkAtiva.setBackground(EstiloUI.COR_FUNDO);
        chkAtiva.setFont(EstiloUI.FONTE_LABEL);

        form.add(EstiloUI.criarLabel("ID da Reserva:"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Leitor:"));
        form.add(cbSolicitante);
        form.add(EstiloUI.criarLabel("Obra Desejada:"));
        form.add(cbObra);
        form.add(EstiloUI.criarLabel("Data da Reserva (AAAA-MM-DD):"));
        form.add(txtDataReserva);
        form.add(EstiloUI.criarLabel("Status:"));
        form.add(chkAtiva);

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
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(principal, 5)));

        botoes.add(btnConsultar);
        botoes.add(btnInserir);
        botoes.add(btnAlterar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnListar);
        add(botoes, BorderLayout.SOUTH);

        carregarCombos();
    }

    public void carregarPorId(int id) {
        txtId.setText(String.valueOf(id));
        consultar();
    }

    private void carregarCombos() {
        try {
            cbSolicitante.removeAllItems();
            for (Leitor l : leitorDAO.listar()) cbSolicitante.addItem(l);
            for (Funcionario f : funcDAO.listar()) cbSolicitante.addItem(f);

            cbObra.removeAllItems();
            for (Obra o : obraDAO.listar()) cbObra.addItem(o);

            cbSolicitante.setSelectedIndex(-1);
            cbObra.setSelectedIndex(-1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar listas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inserir() {
        try {
            Pessoa p = (Pessoa) cbSolicitante.getSelectedItem();
            Obra o = (Obra) cbObra.getSelectedItem();
            LocalDate dt = LocalDate.parse(txtDataReserva.getText().trim());
            Reserva r = new Reserva(null, p, o, dt, chkAtiva.isSelected());
            if (dao.inserir(r) > 0) {
                JOptionPane.showMessageDialog(this, "Reserva registrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da reserva para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Reserva r = dao.carregar(id);
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Reserva não encontrada.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (int i = 0; i < cbSolicitante.getItemCount(); i++) {
                Pessoa p = cbSolicitante.getItemAt(i);
                if (p.getId().equals(r.getSolicitante().getId()) && p.getClass().equals(r.getSolicitante().getClass())) {
                    cbSolicitante.setSelectedIndex(i);
                }
            }
            for (int i = 0; i < cbObra.getItemCount(); i++) {
                if (cbObra.getItemAt(i).getId().equals(r.getObra().getId())) cbObra.setSelectedIndex(i);
            }
            txtDataReserva.setText(r.getDataReserva().toString());
            chkAtiva.setSelected(r.isAtiva());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da reserva para alterar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Pessoa p = (Pessoa) cbSolicitante.getSelectedItem();
            Obra o = (Obra) cbObra.getSelectedItem();
            LocalDate dt = LocalDate.parse(txtDataReserva.getText().trim());
            Reserva r = new Reserva(id, p, o, dt, chkAtiva.isSelected());
            if (dao.alterar(r) > 0) {
                JOptionPane.showMessageDialog(this, "Reserva alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Reserva não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluir() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID da reserva para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a reserva ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Reserva excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
            } else {
                JOptionPane.showMessageDialog(this, "Reserva não encontrada.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        txtId.setText("");
        txtDataReserva.setText(LocalDate.now().toString());
        chkAtiva.setSelected(true);
        txtId.requestFocus();
        cbSolicitante.setSelectedIndex(-1);
        cbObra.setSelectedIndex(-1);
    }
}