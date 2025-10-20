
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import chucknorris.*;
public class Main {
    public static void main(String[] args) {

        // Obtener el chiste de Chuck Norris
        String jokeInEnglish = "";
        String jokeInSpanish = "";
        try {
            // Usamos ChuckNorrisAdapter para obtener el chiste en inglés
            JokeService jokeService = new ChuckNorrisAdapter();
            String joke = jokeService.getJoke();  // Obtiene el chiste en inglés y lo traduce a español

            // Separamos el chiste en inglés y en español
            String[] jokes = joke.split("\n");  // Separamos por nueva línea
            jokeInEnglish = jokes[0].replace("Inglés: ", "").trim();
            jokeInSpanish = jokes[1].replace("Español: ", "").trim();

            // Imprime el chiste en ambos idiomas
            System.out.println("Chiste en Inglés: " + jokeInEnglish);
            System.out.println("Chiste en Español: " + jokeInSpanish);

        } catch (Exception e) {
            System.out.println("Error al obtener o traducir el chiste: " + e.getMessage());
            return;  // Salir si hay error al obtener el chiste
        }

        // Escapar las comillas simples para evitar errores en SQL
        jokeInEnglish = escapeSingleQuotes(jokeInEnglish);
        jokeInSpanish = escapeSingleQuotes(jokeInSpanish);

        // Conexión a SQLite con Factory
        DatabaseFactory sqliteFactory = new SQLiteFactory();
        Database sqliteDb = sqliteFactory.createDatabase("jdbc:sqlite:baseproduccion.db");

        try (Connection conn = sqliteDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                    "AUTOID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "chiste_en_ingles TEXT, " +
                    "chiste_en_espanol TEXT)");

            // Insertar el chiste obtenido de Chuck Norris sin especificar AUTOID
            stmt.execute("INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                    "VALUES ('" + jokeInEnglish + "', '" + jokeInSpanish + "')");

            ResultSet rs = stmt.executeQuery("SELECT * FROM ChistesBasto");

            System.out.println("Datos desde SQLite:");
            while (rs.next()) {
                System.out.println(rs.getInt("AUTOID") + " - Inglés: " + rs.getString("chiste_en_ingles") +
                        " | Español: " + rs.getString("chiste_en_espanol"));
            }

        } catch (SQLException e) {
            System.out.println("Error SQLite: " + e.getMessage());
        }

        // Conexión a MySQL con Factory
        DatabaseFactory mysqlFactory = new MySQLFactory();
        Database mysqlDb = mysqlFactory.createDatabase("jdbc:mysql://172.30.16.213:3306/construccion1");

        try (Connection conn = mysqlDb.connect();
             Statement stmt = conn.createStatement()) {

            // Crear la tabla correctamente
            stmt.execute("CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                    "AUTOID INT AUTO_INCREMENT PRIMARY KEY, " +
                    "chiste_en_ingles TEXT, " +
                    "chiste_en_espanol TEXT)");

            // Insertar el chiste obtenido de Chuck Norris sin especificar AUTOID
            stmt.execute("INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                    "VALUES ('" + jokeInEnglish + "', '" + jokeInSpanish + "')");

            // Consultar y mostrar los datos
            ResultSet rs = stmt.executeQuery("SELECT * FROM ChistesBasto");

            System.out.println("Datos desde MySQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("AUTOID") + " - Inglés: " + rs.getString("chiste_en_ingles") +
                        " | Español: " + rs.getString("chiste_en_espanol"));
            }

        } catch (SQLException e) {
            System.out.println("Error MySQL: " + e.getMessage());
        }


        // Conexión a PostgreSQL con Factory
        DatabaseFactory postgresFactory = new PostgresFactory();
        Database postgresDb = postgresFactory.createDatabase("jdbc:postgresql://172.30.16.213:5432/construccion1");

        try (Connection conn = postgresDb.connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS ChistesBasto (" +
                    "AUTOID SERIAL PRIMARY KEY, " +
                    "chiste_en_ingles TEXT, " +
                    "chiste_en_espanol TEXT)");

            // Insertar el chiste obtenido de Chuck Norris
            stmt.execute("INSERT INTO ChistesBasto (chiste_en_ingles, chiste_en_espanol) " +
                    "VALUES ('" + jokeInEnglish + "', '" + jokeInSpanish + "') " +
                    "ON CONFLICT (AUTOID) DO UPDATE SET chiste_en_ingles = EXCLUDED.chiste_en_ingles, chiste_en_espanol = EXCLUDED.chiste_en_espanol");

            ResultSet rs = stmt.executeQuery("SELECT * FROM ChistesBasto");

            System.out.println("Datos desde PostgreSQL:");
            while (rs.next()) {
                System.out.println(rs.getInt("AUTOID") + " - Inglés: " + rs.getString("chiste_en_ingles") +
                        " | Español: " + rs.getString("chiste_en_espanol"));
            }

        } catch (SQLException e) {
            System.out.println("Error PostgreSQL: " + e.getMessage());
        }
    }

    // Método para escapar las comillas simples en las cadenas
    private static String escapeSingleQuotes(String str) {
        if (str != null) {
            return str.replace("'", "''");
        }
        return str;
    }
}
