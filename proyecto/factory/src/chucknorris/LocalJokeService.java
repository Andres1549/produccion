package chucknorris;
// Implementación local (sin API) para pruebas
public class LocalJokeService implements JokeService {
    @Override
    public String getJoke() {
        return "Este es un chiste local.";
    }
}
