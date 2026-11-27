package com.biblioteca.view;

import com.biblioteca.dao.CopiaDAO;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.FuncionarioDAO;
import com.biblioteca.dao.LeitorDAO;
import com.biblioteca.model.Copia;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Funcionario;
import com.biblioteca.model.Leitor;
import com.biblioteca.model.Pessoa;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmEmprestimo extends JFrame {
    private JTextField txtId, txtDataEmp, txtDataPrevista, txtDataDevolucao;
    private JComboBox<Pessoa> cbSolicitante;
    private JComboBox<Funcionario> cbFuncionario;
    private JComboBox<Copia> cbCopia;

    private final EmprestimoDAO dao = new EmprestimoDAO();
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final FuncionarioDAO funcDAO = new FuncionarioDAO();
    private final CopiaDAO copiaDAO = new CopiaDAO();
    private final FrmPrincipal principal;

    public FrmEmprestimo(FrmPrincipal principal) {
        this.principal = principal;
        setTitle("Controle de Empréstimos e Devoluções");
        setSize(620, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Empréstimos e Devoluções", "Datas no formato AAAA-MM-DD (ex.: 2026-11-25)"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 8));
        form.setBackground(EstiloUI.COR_FUNDO);
        form.setBorder(new EmptyBorder(15, 22, 10, 22));

        txtId = EstiloUI.criarCampoTexto();
        cbSolicitante = new JComboBox<>();
        cbFuncionario = new JComboBox<>();
        cbCopia = new JComboBox<>();
        txtDataEmp = EstiloUI.criarCampoTexto();
        txtDataPrevista = EstiloUI.criarCampoTexto();
        txtDataDevolucao = EstiloUI.criarCampoTexto();
        txtDataDevolucao.setEditable(false);

        form.add(EstiloUI.criarLabel("ID do Empréstimo:"));
        form.add(txtId);
        form.add(EstiloUI.criarLabel("Solicitante:"));
        form.add(cbSolicitante);
        form.add(EstiloUI.criarLabel("Funcionário Responsável:"));
        form.add(cbFuncionario);
        form.add(EstiloUI.criarLabel("Cópia (Exemplar):"));
        form.add(cbCopia);
        form.add(EstiloUI.criarLabel("Data do Empréstimo:"));
        form.add(txtDataEmp);
        form.add(EstiloUI.criarLabel("Data Prevista Devolução:"));
        form.add(txtDataPrevista);
        form.add(EstiloUI.criarLabel("Data Devolução Real:"));
        form.add(txtDataDevolucao);

        add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
        botoes.setBackground(EstiloUI.COR_FUNDO);

        JButton btnConsultar = EstiloUI.criarBotao("Consultar", EstiloUI.COR_DESTAQUE);
        JButton btnEmprestar = EstiloUI.criarBotao("Registrar Empréstimo", EstiloUI.COR_SUCESSO);
        JButton btnDevolver  = EstiloUI.criarBotao("Devolver", EstiloUI.COR_PRIMARIA);
        JButton btnExcluir   = EstiloUI.criarBotao("Excluir", EstiloUI.COR_PERIGO);
        JButton btnLimpar    = EstiloUI.criarBotao("Limpar", EstiloUI.COR_NEUTRA);
        JButton btnListar    = EstiloUI.criarBotao("Listar", EstiloUI.COR_NEUTRA);

        btnConsultar.addActionListener(e -> consultar());
        btnEmprestar.addActionListener(e -> inserir());
        btnDevolver.addActionListener(e -> devolver());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnListar.addActionListener(e -> principal.abrirTelaUnica(() -> new FrmConsultas(principal, 4)));

        botoes.add(btnConsultar);
        botoes.add(btnEmprestar);
        botoes.add(btnDevolver);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        botoes.add(btnListar);
        add(botoes, BorderLayout.SOUTH);

        carregarCombos();
        preencherDatasPadrao();
    }

    public void carregarPorId(int id) {
        txtId.setText(String.valueOf(id));
        consultar();
    }

    private void preencherDatasPadrao() {
        LocalDate hoje = LocalDate.now();
        txtDataEmp.setText(hoje.toString());
        //preenche automaticamente a data de hoje e o prazo de +7 dias,
        // além de permitir registrar a devolução e atualizar a cópia para DISPONIVEL).
        txtDataPrevista.setText(hoje.plusDays(7).toString());
    }

    private void carregarCombos() {
        try {
            cbSolicitante.removeAllItems();
            for (Leitor l : leitorDAO.listar()) cbSolicitante.addItem(l);
            for (Funcionario f : funcDAO.listar()) cbSolicitante.addItem(f);

            cbFuncionario.removeAllItems();
            for (Funcionario f : funcDAO.listar()) cbFuncionario.addItem(f);

            cbCopia.removeAllItems();
            for (Copia c : copiaDAO.listar()) cbCopia.addItem(c);

            limparSelecoes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar listas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparSelecoes() {
        cbSolicitante.setSelectedIndex(-1);
        cbFuncionario.setSelectedIndex(-1);
        cbCopia.setSelectedIndex(-1);
    }

    private void inserir() {
        try {
            Pessoa solicitante = (Pessoa) cbSolicitante.getSelectedItem();
            Funcionario func = (Funcionario) cbFuncionario.getSelectedItem();
            Copia copia = (Copia) cbCopia.getSelectedItem();
            LocalDate dtEmp = LocalDate.parse(txtDataEmp.getText().trim());
            LocalDate dtPrev = LocalDate.parse(txtDataPrevista.getText().trim());

            Emprestimo emp = new Emprestimo(null, solicitante, func, copia, dtEmp, dtPrev);
            if (dao.inserir(emp) > 0) {
                int continuar = JOptionPane.showConfirmDialog(this,
                        "Empréstimo registrado com sucesso!\nDeseja registrar outro livro para " + solicitante.getNome() + "?",
                        "Sucesso", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                // Recarrega os combos para atualizar o status da cópia recém-emprestada
                carregarCombos();

                if (continuar == JOptionPane.YES_OPTION) {
                    // Restaura Solicitante, Funcionário e Datas, deixando apenas a Cópia em branco
                    for (int i = 0; i < cbSolicitante.getItemCount(); i++) {
                        Pessoa p = cbSolicitante.getItemAt(i);
                        if (p.getId().equals(solicitante.getId()) && p.getClass().equals(solicitante.getClass())) {
                            cbSolicitante.setSelectedIndex(i);
                            break;
                        }
                    }
                    for (int i = 0; i < cbFuncionario.getItemCount(); i++) {
                        if (cbFuncionario.getItemAt(i).getId().equals(func.getId())) {
                            cbFuncionario.setSelectedIndex(i);
                            break;
                        }
                    }
                    txtDataEmp.setText(dtEmp.toString());
                    txtDataPrevista.setText(dtPrev.toString());
                    cbCopia.setSelectedIndex(-1);
                    cbCopia.requestFocus();
                } else {
                    limpar();
                }
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato AAAA-MM-DD (ex.: 2026-11-25).", "Erro de Data", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultar() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o ID do empréstimo para consultar.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            Emprestimo emp = dao.carregar(id);
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "Empréstimo não encontrado.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (int i = 0; i < cbSolicitante.getItemCount(); i++) {
                Pessoa p = cbSolicitante.getItemAt(i);
                if (p.getId().equals(emp.getSolicitante().getId()) && p.getClass().equals(emp.getSolicitante().getClass())) {
                    cbSolicitante.setSelectedIndex(i);
                }
            }
            for (int i = 0; i < cbFuncionario.getItemCount(); i++) {
                if (cbFuncionario.getItemAt(i).getId().equals(emp.getFuncionario().getId())) cbFuncionario.setSelectedIndex(i);
            }
            for (int i = 0; i < cbCopia.getItemCount(); i++) {
                if (cbCopia.getItemAt(i).getId().equals(emp.getCopia().getId())) cbCopia.setSelectedIndex(i);
            }
            txtDataEmp.setText(emp.getDataEmprestimo().toString());
            txtDataPrevista.setText(emp.getDataPrevistaDevolucao().toString());
            txtDataDevolucao.setText(emp.getDataDevolucaoReal() != null ? emp.getDataDevolucaoReal().toString() : "Em aberto");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolver() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Consulte ou informe o ID do empréstimo para devolver.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Confirmar a devolução do empréstimo ID " + id + " na data de hoje?",
                    "Registrar Devolução", JOptionPane.YES_NO_OPTION);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.registrarDevolucao(id, LocalDate.now()) > 0) {
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso! A cópia está disponível novamente.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                consultar();
                carregarCombos();
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
                JOptionPane.showMessageDialog(this, "Informe o ID do empréstimo para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = Integer.parseInt(txtId.getText().trim());
            int confirma = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o empréstimo ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirma != JOptionPane.YES_OPTION) return;

            if (dao.remover(id) > 0) {
                JOptionPane.showMessageDialog(this, "Empréstimo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                carregarCombos();
            } else {
                JOptionPane.showMessageDialog(this, "Empréstimo não encontrado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O ID deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void limpar() {
        txtId.setText("");
        txtDataDevolucao.setText("");
        limparSelecoes();
        preencherDatasPadrao();
        txtId.requestFocus();
    }
}