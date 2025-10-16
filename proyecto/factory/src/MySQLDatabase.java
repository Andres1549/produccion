import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabase implements Database {
    private static final String URL = "jdbc:mysql://172.30.16.44:3306/construccion1?useSSL=false&serverTimezone=UTC";
    private static final String USER = "u67001549";
    private static final String PASSWORD = "12345";

    @Override
    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("\n Conexión a MySQL establecida.\n");
        } catch (SQLException e) {
            System.out.println("Error al conectar con MySQL: " + e.getMessage());
        }
        return connection;
    }
}
