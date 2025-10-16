package chucknorris;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyMemoryTraductor implements JokeService {

    private String textToTranslate;
    private String sourceLang;
    private String targetLang;

    public MyMemoryTraductor(String textToTranslate, String sourceLang, String targetLang) {
        this.textToTranslate = textToTranslate;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
    }

    @Override
    public String getJoke() {
        try {
            String apiUrl = "https://api.mymemory.translated.net/get?q="
                    + java.net.URLEncoder.encode(textToTranslate, "UTF-8")
                    + "&langpair=" + sourceLang + "|" + targetLang;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getJSONObject("responseData").getString("translatedText");

        } catch (Exception e) {
            return "Error al traducir con MyMemory: " + e.getMessage();
        }
    }
}
