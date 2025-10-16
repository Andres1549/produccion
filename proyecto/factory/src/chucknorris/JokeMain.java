package chucknorris;
public class JokeMain {
    public static void main(String[] args) {
        // Usamos el adaptador para obtener el chiste traducido
        JokeService jokeService = new ChuckNorrisAdapter();
        String joke = jokeService.getJoke();

        // Imprimir el chiste traducido
        System.out.println("Joke translated: \n" + joke);
    }
}
