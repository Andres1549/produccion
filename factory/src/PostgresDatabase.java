import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDatabase implements Database {
    private String url;
    private String user;
    private String password;

    public PostgresDatabase(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("\n Conexión a PostgreSQL establecida.\n");
            return conn;
        } catch (SQLException e) {
            System.err.println("Error al conectar a PostgreSQL: " + e.getMessage());
            return null;
        }
    }
}
