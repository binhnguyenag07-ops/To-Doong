package Repository;

import config.DatabaseConnection;
import model.Todo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresTodoRepository {
    private final String password;

    public PostgresTodoRepository(String password) {
        this.password = password;
    }

    public List<Todo> findAll() throws SQLException {
        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT id, title, completed FROM todos";

        try (Connection connection = DatabaseConnection.open(password);
                PreparedStatement statement = connection.prepareStatement(sql);
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

    public void save(Todo todo) throws SQLException {
        String sql = """
                INSERT INTO todos (id, title, completed)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.open(password);
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, todo.getID());
            statement.setString(2, todo.getTitle());
            statement.setBoolean(3, todo.getCompleted());

            statement.executeUpdate();
        }
    }

    public Todo findByID(String id) throws SQLException {
        String sql = "SELECT id, title, completed FROM todos WHERE id = ?";

        try (Connection connection = DatabaseConnection.open(password);
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Todo(
                            result.getString("id"),
                            result.getString("title"),
                            result.getBoolean("completed"));
                }

                return null;
            }
        }
    }
}