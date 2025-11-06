package chucknorris;
public class JokeTranslator {

    // API de Chuck Norris
    public static String getChuckNorrisJoke() throws Exception {
        String joke = new ChuckNorrisAPI().fetchJoke(); // Usa fetchJoke() en lugar de getChuckNorrisJoke()
        // Aquí suponemos que la respuesta es algo como: {"value": "Chuck Norris can divide by zero."}
        return joke.split("\"value\":\"")[1].split("\"")[0];
    }

    // API de MyMemory para traducir
    public static String translateJoke(String joke, String sourceLang, String targetLang) throws Exception {
        return new MyMemoryTraductor(joke, sourceLang, targetLang).getJoke();
    }

    public static void main(String[] args) {
        try {
            // Obtener chiste de Chuck Norris
            String chuckNorrisJoke = getChuckNorrisJoke();
            System.out.println("Original Joke: " + chuckNorrisJoke);

            // Traducir chiste de inglés a español
            String translatedJoke = translateJoke(chuckNorrisJoke, "en", "es");

            System.out.println("Translated Joke: " + translatedJoke);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
