public class SQLiteFactory extends DatabaseFactory {
    @Override
    public Database createDatabase(String url) {
        return new SQLiteDatabase(url);
    }
}
