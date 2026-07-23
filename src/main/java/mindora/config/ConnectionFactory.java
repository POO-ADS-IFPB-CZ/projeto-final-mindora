package mindora.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.out.println("db.properties não encontrado. Usando credenciais padrão.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o arquivo de propriedades: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        // Busca do db.properties ou assume valores de fallback
        String url = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/mindora");
        String user = props.getProperty("db.user", "postgres");
        String pass = props.getProperty("db.password", "123456");

        return DriverManager.getConnection(url, user, pass);
    }

    // Teste de conexão local
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Conexão com o PostgreSQL realizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Falha ao conectar com o banco de dados:");
            e.printStackTrace();
        }
    }
}