package com.biblioteca.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EstiloUI {
    // Paleta personalizada: dois tons de Rosa + Cinza e Verde
    public static final Color COR_PRIMARIA = new Color(158, 63, 102);    // Rosa escuro / cereja fechado (cabeçalhos)
    public static final Color COR_DESTAQUE = new Color(214, 112, 150);   // Rosa médio / malva (botões de ação)
    public static final Color COR_SUCESSO  = new Color(46, 125, 91);     // Verde-esmeralda discreto (mantido)
    public static final Color COR_PERIGO   = new Color(176, 65, 62);     // Vermelho fosco (excluir)
    public static final Color COR_NEUTRA   = new Color(108, 122, 137);   // Cinza-azulado (mantido)
    public static final Color COR_FUNDO    = new Color(252, 246, 249);   // Off-white com leve toque rosado
    public static final Color COR_CARD     = Color.WHITE;
    public static final Color COR_TEXTO    = new Color(45, 35, 40);

    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONTE_LABEL  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONTE_CAMPO  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_BOTAO  = new Font("Segoe UI", Font.BOLD, 12);

    public static JPanel criarCabecalho(String titulo, String subtitulo) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_PRIMARIA);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(248, 224, 234));

        header.add(lblTitulo, BorderLayout.NORTH);
        header.add(lblSub, BorderLayout.SOUTH);
        return header;
    }

    public static JButton criarBotao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        return btn;
    }

    public static JLabel criarLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONTE_LABEL);
        lbl.setForeground(COR_TEXTO);
        return lbl;
    }

    public static JTextField criarCampoTexto() {
        JTextField txt = new JTextField();
        txt.setFont(FONTE_CAMPO);
        return txt;
    }

    public static void estilizarTabela(JTable tabela) {
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.setSelectionBackground(new Color(248, 218, 230)); // Rosa bem claro ao selecionar a linha
        tabela.setSelectionForeground(COR_TEXTO);
        tabela.getTableHeader().setFont(FONTE_LABEL);
        tabela.getTableHeader().setBackground(COR_PRIMARIA);
        tabela.getTableHeader().setForeground(COR_PRIMARIA);
    }
}