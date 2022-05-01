package emented.lab7.server.db;

import emented.lab7.common.exceptions.DatabaseException;
import emented.lab7.common.util.TextColoring;
import emented.lab7.server.ServerConfig;
import emented.lab7.server.interfaces.DBConnectable;
import emented.lab7.server.interfaces.SQLConsumer;
import emented.lab7.server.interfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DBLocalConnector implements DBConnectable {

    private final String dbUrl = "jdbc:postgresql://pg:5432/studs";
    private final String user = "s336189";
    private final String pass = "qez387";

    public DBLocalConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("No DB driver!"));
            System.exit(1);
        } catch (SQLException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("Error occurred during initializing tables!" + e.getMessage()));
            System.exit(1);
        }
    }

    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }

    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Error occurred during working with DB: " + Arrays.toString(e.getStackTrace()));
        }
    }


    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

        Statement statement = connection.createStatement();

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s336189musicbands_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS s336189users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE TABLE IF NOT EXISTS s336189users "
                + "("
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>''),"
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s336189users_id_seq')"
                + ");");

        statement.execute("CREATE TABLE IF NOT EXISTS s336189musicbands "
                + "("
                + "id bigint NOT NULL PRIMARY KEY DEFAULT nextval('s336189musicbands_id_seq'),"
                + "creationDate date NOT NULL,"
                + "name varchar(100) NOT NULL CHECK(name<>''),"
                + "x float NOT NULL CHECK(x <= 947),"
                + "y float NOT NULL CHECK(y <= 104),"
                + "numberOfParticipants bigint NOT NULL CHECK(numberOfParticipants > 0),"
                + "description varchar(300),"
                + "musicGenre varchar(21) CHECK(musicGenre = 'PROGRESSIVE_ROCK' "
                + "OR musicGenre = 'PSYCHEDELIC_CLOUD_RAP' "
                + "OR musicGenre = 'JAZZ' "
                + "OR musicGenre = 'BLUES' "
                + "OR musicGenre = 'BRIT_POP'),"
                + "studioAddress varchar(100),"
                + "owner_id bigint NOT NULL REFERENCES s336189users (id)"
                + ");");

        connection.close();
    }
}
