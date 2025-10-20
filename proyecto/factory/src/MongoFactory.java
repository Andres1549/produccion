public class MongoFactory extends DatabaseFactory {
    @Override
    public Database createDatabase(String url) {
        return new MongoDatabaseImpl(url);
    }
}
