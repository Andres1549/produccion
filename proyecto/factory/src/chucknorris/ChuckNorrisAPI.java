// API externa simulada con HTTP GET
// Usaremos java.net.HttpURLConnection para no depender de librerías adicionales
package chucknorris;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChuckNorrisAPI {
    public String fetchJoke() {
        try {
            URL url = new URL("https://api.chucknorris.io/jokes/random");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            return response.toString(); // JSON completo
        } catch (Exception e) {
            // Manejar error en la conexión o al obtener el chiste
            return "Error al obtener el chiste de Chuck Norris: " + e.getMessage();
        }
    }
}