package com.biblioteca.view;

import java.awt.*;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FrmPrincipal extends JFrame {
    //Controla para que apenas uma janela filha fique aberta por vez
    // Ao clicar em outro menu ou botão, ele fecha a janela anterior antes de abrir a nova

    private JFrame janelaAtual; // Guarda a única tela aberta no momento

    public FrmPrincipal() {
        setTitle("Sistema de Gestão - Biblioteca da Mari");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        montarMenu();
        montarPainelCentral();
    }

    // Garante que nunca existam duas janelas abertas ao mesmo tempo
    public void abrirTelaUnica(Supplier<JFrame> construtorTela) {
        if (janelaAtual != null && janelaAtual.isDisplayable()) {
            janelaAtual.dispose();
        }
        janelaAtual = construtorTela.get();
        janelaAtual.setVisible(true);
        janelaAtual.toFront();
    }

    private void montarMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Cadastros
        JMenu menuCadastros = new JMenu("Cadastros");
        JMenuItem miLeitor = new JMenuItem("Leitores");
        JMenuItem miFuncionario = new JMenuItem("Funcionários");
        JMenuItem miObra = new JMenuItem("Obras");
        JMenuItem miCopia = new JMenuItem("Cópias (Exemplares)");

        miLeitor.addActionListener(e -> abrirTelaUnica(() -> new FrmLeitor(this)));
        miFuncionario.addActionListener(e -> abrirTelaUnica(() -> new FrmFuncionario(this)));
        miObra.addActionListener(e -> abrirTelaUnica(() -> new FrmObra(this)));
        miCopia.addActionListener(e -> abrirTelaUnica(() -> new FrmCopia(this)));

        menuCadastros.add(miLeitor);
        menuCadastros.add(miFuncionario);
        menuCadastros.addSeparator();
        menuCadastros.add(miObra);
        menuCadastros.add(miCopia);

        // Menu Movimentações
        JMenu menuMov = new JMenu("Movimentações");
        JMenuItem miEmprestimo = new JMenuItem("Empréstimos e Devoluções");
        JMenuItem miReserva = new JMenuItem("Reservas");

        miEmprestimo.addActionListener(e -> abrirTelaUnica(() -> new FrmEmprestimo(this)));
        miReserva.addActionListener(e -> abrirTelaUnica(() -> new FrmReserva(this)));

        menuMov.add(miEmprestimo);
        menuMov.add(miReserva);

        // Menu Consultas
        JMenu menuConsultas = new JMenu("Consultas");
        JMenuItem miConsultaGeral = new JMenuItem("Consultar Tabelas do Acervo Pessoal e Cadastros");
        miConsultaGeral.addActionListener(e -> abrirTelaUnica(() -> new FrmConsultas(this)));
        menuConsultas.add(miConsultaGeral);

        // Menu Sistema
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem miSair = new JMenuItem("Sair");
        miSair.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(this, "Deseja realmente sair do sistema?",
                    "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuSistema.add(miSair);

        menuBar.add(menuCadastros);
        menuBar.add(menuMov);
        menuBar.add(menuConsultas);
        menuBar.add(menuSistema);
        setJMenuBar(menuBar);
    }

    private void montarPainelCentral() {
        JPanel fundo = new JPanel(new BorderLayout());
        fundo.setBackground(EstiloUI.COR_FUNDO);

        fundo.add(EstiloUI.criarCabecalho(
                "Controle da Biblioteca da Mari",
                "Gerenciamento integrado de Leitores, Funcionários, Acervo, Empréstimos e Reservas"
        ), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(EstiloUI.COR_FUNDO);

        JPanel card = new JPanel(new GridLayout(4, 2, 16, 14));
        card.setBackground(EstiloUI.COR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(30, 35, 30, 35)
        ));

        JButton btnLeitores = EstiloUI.criarBotao("Gerenciar Leitores", EstiloUI.COR_DESTAQUE);
        JButton btnFuncionarios = EstiloUI.criarBotao("Gerenciar Funcionários", EstiloUI.COR_DESTAQUE);
        JButton btnObras = EstiloUI.criarBotao("Gerenciar Obras", EstiloUI.COR_PRIMARIA);
        JButton btnCopias = EstiloUI.criarBotao("Gerenciar Cópias", EstiloUI.COR_PRIMARIA);
        JButton btnEmprestimos = EstiloUI.criarBotao("Empréstimos / Devoluções", EstiloUI.COR_SUCESSO);
        JButton btnReservas = EstiloUI.criarBotao("Gerenciar Reservas", EstiloUI.COR_SUCESSO);
        JButton btnConsultas = EstiloUI.criarBotao("Consultar Tabelas", EstiloUI.COR_NEUTRA);

        btnReservas.addActionListener(e -> abrirTelaUnica(() -> new FrmReserva(this)));
        btnLeitores.addActionListener(e -> abrirTelaUnica(() -> new FrmLeitor(this)));
        btnFuncionarios.addActionListener(e -> abrirTelaUnica(() -> new FrmFuncionario(this)));
        btnObras.addActionListener(e -> abrirTelaUnica(() -> new FrmObra(this)));
        btnCopias.addActionListener(e -> abrirTelaUnica(() -> new FrmCopia(this)));
        btnEmprestimos.addActionListener(e -> abrirTelaUnica(() -> new FrmEmprestimo(this)));
        btnConsultas.addActionListener(e -> abrirTelaUnica(() -> new FrmConsultas(this)));

        card.add(btnLeitores);
        card.add(btnFuncionarios);
        card.add(btnObras);
        card.add(btnCopias);
        card.add(btnEmprestimos);
        card.add(btnConsultas);
        card.add(btnReservas);

        centro.add(card);
        fundo.add(centro, BorderLayout.CENTER);
        add(fundo);
    }
}