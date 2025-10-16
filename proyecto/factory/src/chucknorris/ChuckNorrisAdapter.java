package chucknorris;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChuckNorrisAdapter implements JokeService {
    private ChuckNorrisAPI api;

    public ChuckNorrisAdapter() {
        this.api = new ChuckNorrisAPI();
    }

    @Override
    public String getJoke() {
        String json = api.fetchJoke();
        try {
            // Obtener el chiste en inglés de la respuesta JSON
            JSONObject obj = new JSONObject(json);
            String jokeInEnglish = obj.getString("value");

            // Traducir el chiste a español utilizando MyMemoryTraductor
            MyMemoryTraductor translator = new MyMemoryTraductor(jokeInEnglish, "en", "es");
            String jokeInSpanish = translator.getJoke();

            // Devolver el chiste en ambos idiomas (inglés y español)
            return String.format("Inglés: %s\nEspañol: %s", jokeInEnglish, jokeInSpanish);

        } catch (Exception e) {
            // Especificar el tipo de error (ya sea al obtener el chiste o traducir)
            return "Error al procesar el chiste o traducir: " + e.getMessage();
        }
    }
}
