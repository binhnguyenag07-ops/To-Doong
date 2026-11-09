import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/todoong";
        String username = "postgres";

        if (System.console() == null) {
            System.out.println("Hay chay bang terminal theo huong dan.");
            return;
        }

        String password = new String(
            System.console().readPassword("Mat khau PostgreSQL: ")
        );

        try (Connection connection =
                DriverManager.getConnection(url, username, password)) {

            System.out.println("Ket noi thanh cong!");
            System.out.println("Database: " + connection.getCatalog());

        } catch (SQLException error) {
            System.out.println("Ket noi that bai: " + error.getMessage());
        }
    }
}