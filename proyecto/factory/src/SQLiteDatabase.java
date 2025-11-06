import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase implements Database {
    private String url;

    public SQLiteDatabase(String url) {
        this.url = url;
    }

    @Override
    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("\n Conexión exitosa con SQLite. \n");
            return conn;
        } catch (SQLException e) {
            System.err.println("Error al conectar a SQLite: " + e.getMessage());
            return null;
        }
    }
}
