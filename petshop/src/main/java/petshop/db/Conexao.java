package petshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL      = "jdbc:postgresql://localhost:5432/petshop";
    private static final String USUARIO  = "postgres";
    private static final String SENHA    = "postgres";

    private static Connection instancia = null;

    private Conexao() {}

    public static Connection getConexao() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL não encontrado. Inclua o JAR no classpath.", e);
            }
            instancia = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return instancia;
    }

    public static void fechar() {
        if (instancia != null) {
            try {
                if (!instancia.isClosed()) instancia.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
