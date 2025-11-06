package factory;
import java.sql.Connection;

public interface Database {
    Connection connect();
}
