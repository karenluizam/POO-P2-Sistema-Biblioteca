package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoSQLite {
    private static final String URL = "jdbc:sqlite:biblioteca.db";
    private static boolean inicializado = false;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        if (!inicializado) {
            inicializado = true;
            criarTabelas(conn);
        }
        return conn;
    }

    private static void criarTabelas(Connection conn) throws SQLException {
        String sqlLeitor = "CREATE TABLE IF NOT EXISTS leitor ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " cpf TEXT NOT NULL UNIQUE,"
                + " email TEXT NOT NULL,"
                + " telefone TEXT NOT NULL,"
                + " matricula TEXT NOT NULL UNIQUE"
                + ");";

        String sqlFuncionario = "CREATE TABLE IF NOT EXISTS funcionario ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " nome TEXT NOT NULL,"
                + " cpf TEXT NOT NULL UNIQUE,"
                + " email TEXT NOT NULL,"
                + " telefone TEXT NOT NULL,"
                + " turno TEXT NOT NULL,"
                + " cargo TEXT NOT NULL,"
                + " salario REAL NOT NULL"
                + ");";

        String sqlObra = "CREATE TABLE IF NOT EXISTS obra ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " titulo TEXT NOT NULL,"
                + " autor TEXT NOT NULL,"
                + " editora TEXT NOT NULL,"
                + " ano INTEGER NOT NULL,"
                + " isbn TEXT NOT NULL UNIQUE"
                + ");";

        String sqlCopia = "CREATE TABLE IF NOT EXISTS copia ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_obra INTEGER NOT NULL,"
                + " codigo_tombo TEXT NOT NULL UNIQUE,"
                + " situacao TEXT NOT NULL,"
                + " FOREIGN KEY (id_obra) REFERENCES obra(id)"
                + ");";

        String sqlEmprestimo = "CREATE TABLE IF NOT EXISTS emprestimo ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_leitor INTEGER NOT NULL,"
                + " id_funcionario INTEGER NOT NULL,"
                + " id_copia INTEGER NOT NULL,"
                + " data_emprestimo TEXT NOT NULL,"
                + " data_prevista TEXT NOT NULL,"
                + " data_devolucao TEXT,"
                + " FOREIGN KEY (id_leitor) REFERENCES leitor(id),"
                + " FOREIGN KEY (id_funcionario) REFERENCES funcionario(id),"
                + " FOREIGN KEY (id_copia) REFERENCES copia(id)"
                + ");";

        String sqlReserva = "CREATE TABLE IF NOT EXISTS reserva ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " id_leitor INTEGER NOT NULL,"
                + " id_obra INTEGER NOT NULL,"
                + " data_reserva TEXT NOT NULL,"
                + " ativa INTEGER NOT NULL,"
                + " FOREIGN KEY (id_leitor) REFERENCES leitor(id),"
                + " FOREIGN KEY (id_obra) REFERENCES obra(id)"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlLeitor);
            stmt.execute(sqlFuncionario);
            stmt.execute(sqlObra);
            stmt.execute(sqlCopia);
            stmt.execute(sqlEmprestimo);
            stmt.execute(sqlReserva);
        }
    }
}
