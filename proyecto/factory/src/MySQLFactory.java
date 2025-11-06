public class MySQLFactory extends DatabaseFactory {
    @Override
    public Database createDatabase(String url) {
        return new MySQLDatabase();
    }
}
