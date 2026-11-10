import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Todo;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/todoong";
        String username = "postgres";

        if (System.console() == null) {
            System.out.println("Hay chay bang terminal theo huong dan.");
            return;
        }

        String password = new String(
                System.console().readPassword("Mat khau PostgreSQL: "));

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            System.out.println("Ket noi thanh cong!");
            System.out.println("Database: " + connection.getCatalog());
            List<Todo> todos = findAll(connection);

            for (Todo todo : todos) {
                System.out.println(todo);
            }

        } catch (SQLException error) {
            System.out.println("Ket noi that bai: " + error.getMessage());
        }
    }

    public static List<Todo> findAll(Connection connection)
            throws SQLException {

        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT id, title, completed FROM todos";

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String id = result.getString("id");
                String title = result.getString("title");
                boolean completed = result.getBoolean("completed");

                Todo todo = new Todo(id, title, completed);
                todos.add(todo);
            }
        }

        return todos;
    }
}