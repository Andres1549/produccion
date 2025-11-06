public class PostgresFactory extends DatabaseFactory {
    @Override
    public Database createDatabase(String url) {
        return new PostgresDatabase(url, "postgres", "12345");
    }
}
