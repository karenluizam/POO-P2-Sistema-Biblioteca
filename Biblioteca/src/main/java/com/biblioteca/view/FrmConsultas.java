package com.biblioteca.view;

import com.biblioteca.dao.*;
import com.biblioteca.model.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmConsultas extends JFrame {
    private JTabbedPane abas;
    private DefaultTableModel modLeitores, modFuncionarios, modObras, modCopias, modEmprestimos, modReservas;

    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final FuncionarioDAO funcDAO = new FuncionarioDAO();
    private final ObraDAO obraDAO = new ObraDAO();
    private final CopiaDAO copiaDAO = new CopiaDAO();
    private final EmprestimoDAO empDAO = new EmprestimoDAO();
    private final ReservaDAO resDAO = new ReservaDAO();
    private final FrmPrincipal principal;

    public FrmConsultas(FrmPrincipal principal) {
        this(principal, 0);
    }

    public FrmConsultas(FrmPrincipal principal, int abaInicial) {
        this.principal = principal;
        setTitle("Consultas Gerais do Sistema");
        setSize(960, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(EstiloUI.COR_FUNDO);

        add(EstiloUI.criarCabecalho("Consultas Gerais", "Dê um duplo clique em qualquer linha para abrir e editar o registro"), BorderLayout.NORTH);

        abas = new JTabbedPane();
        abas.setFont(EstiloUI.FONTE_LABEL);

        modLeitores     = criarModelo(new String[]{"ID", "Nome", "CPF", "E-mail", "Telefone", "Matrícula"});
        modFuncionarios = criarModelo(new String[]{"ID", "Nome", "CPF", "Turno", "Cargo", "Salário (R$)"});
        modObras        = criarModelo(new String[]{"ID", "Título", "Autor", "Editora", "Ano", "ISBN"});
        modCopias       = criarModelo(new String[]{"ID", "Obra", "Código Tombo", "Situação"});
        modEmprestimos  = criarModelo(new String[]{"ID", "Solicitante", "Funcionário", "Cópia (Tombo)", "Empréstimo", "Prevista", "Devolução"});
        modReservas     = criarModelo(new String[]{"ID", "Solicitante", "Obra", "Data Reserva", "Status"});

        abas.addTab("Leitores", criarPainelTabela(modLeitores, 0));
        abas.addTab("Funcionários", criarPainelTabela(modFuncionarios, 1));
        abas.addTab("Obras", criarPainelTabela(modObras, 2));
        abas.addTab("Cópias", criarPainelTabela(modCopias, 3));
        abas.addTab("Empréstimos", criarPainelTabela(modEmprestimos, 4));
        abas.addTab("Reservas", criarPainelTabela(modReservas, 5));

        add(abas, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        rodape.setBackground(EstiloUI.COR_FUNDO);
        JButton btnAtualizar = EstiloUI.criarBotao("Atualizar Todas as Tabelas", EstiloUI.COR_DESTAQUE);
        btnAtualizar.addActionListener(e -> carregarTudo());
        rodape.add(btnAtualizar);
        add(rodape, BorderLayout.SOUTH);

        carregarTudo();
        if (abaInicial >= 0 && abaInicial < abas.getTabCount()) {
            abas.setSelectedIndex(abaInicial);
        }
    }

    private DefaultTableModel criarModelo(String[] colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta nas células da tabela
            }
        };
    }

    private JScrollPane criarPainelTabela(DefaultTableModel modelo, int tipoTabela) {
        JTable tabela = new JTable(modelo);
        EstiloUI.estilizarTabela(tabela);

        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                    int linha = tabela.getSelectedRow();
                    int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());
                    abrirTelaEdicao(tipoTabela, id);
                }
            }
        });
        return new JScrollPane(tabela);
    }

    private void abrirTelaEdicao(int tipoTabela, int id) {
        switch (tipoTabela) {
            case 0 -> principal.abrirTelaUnica(() -> {
                FrmLeitor frm = new FrmLeitor(principal);
                frm.carregarPorId(id);
                return frm;
            });
            case 1 -> principal.abrirTelaUnica(() -> {
                FrmFuncionario frm = new FrmFuncionario(principal);
                frm.carregarPorId(id);
                return frm;
            });
            case 2 -> principal.abrirTelaUnica(() -> {
                FrmObra frm = new FrmObra(principal);
                frm.carregarPorId(id);
                return frm;
            });
            case 3 -> principal.abrirTelaUnica(() -> {
                FrmCopia frm = new FrmCopia(principal);
                frm.carregarPorId(id);
                return frm;
            });
            case 4 -> principal.abrirTelaUnica(() -> {
                FrmEmprestimo frm = new FrmEmprestimo(principal);
                frm.carregarPorId(id);
                return frm;
            });
            case 5 -> principal.abrirTelaUnica(() -> {
                FrmReserva frm = new FrmReserva(principal);
                frm.carregarPorId(id);
                return frm;
            });
        }
    }

    private void carregarTudo() {
        try {
            modLeitores.setRowCount(0);
            for (Leitor l : leitorDAO.listar()) {
                modLeitores.addRow(new Object[]{l.getId(), l.getNome(), l.getCpf(), l.getEmail(), l.getTelefone(), l.getMatricula()});
            }

            modFuncionarios.setRowCount(0);
            for (Funcionario f : funcDAO.listar()) {
                modFuncionarios.addRow(new Object[]{f.getId(), f.getNome(), f.getCpf(), f.getTurno(), f.getCargo(), String.format("%.2f", f.getSalario())});
            }

            modObras.setRowCount(0);
            for (Obra o : obraDAO.listar()) {
                modObras.addRow(new Object[]{o.getId(), o.getTitulo(), o.getAutor(), o.getEditora(), o.getAno(), o.getIsbn()});
            }

            modCopias.setRowCount(0);
            for (Copia c : copiaDAO.listar()) {
                modCopias.addRow(new Object[]{c.getId(), c.getObra().getTitulo(), c.getCodigoTombo(), c.getSituacao()});
            }

            modEmprestimos.setRowCount(0);
            for (Emprestimo emp : empDAO.listar()) {
                String dev = (emp.getDataDevolucaoReal() != null) ? emp.getDataDevolucaoReal().toString() : "Em aberto";
                modEmprestimos.addRow(new Object[]{
                        emp.getId(),
                        emp.getSolicitante().getNome() + (emp.getSolicitante() instanceof Funcionario ? " [Func.]" : " [Solicitante]"),
                        emp.getFuncionario().getNome(),
                        emp.getCopia().getCodigoTombo() + " (" + emp.getCopia().getObra().getTitulo() + ")",
                        emp.getDataEmprestimo(),
                        emp.getDataPrevistaDevolucao(),
                        dev
                });
            }

            modReservas.setRowCount(0);
            for (Reserva r : resDAO.listar()) {
                modReservas.addRow(new Object[]{
                        r.getId(),
                        r.getSolicitante().getNome() + (r.getSolicitante() instanceof Funcionario ? " [Func.]" : " [Leitor]"),
                        r.getObra().getTitulo(),
                        r.getDataReserva(),
                        r.isAtiva() ? "Ativa" : "Concluída/Cancelada"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar consultas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}