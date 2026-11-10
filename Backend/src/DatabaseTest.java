import Repository.PostgresTodoRepository;
import model.Todo;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTest {
    public static void main(String[] args) {
        if (System.console() == null) {
            System.out.println("Hay chay bang terminal theo huong dan.");
            return;
        }

        String password = new String(
            System.console().readPassword("Mat khau PostgreSQL: ")
        );

        PostgresTodoRepository repository =
            new PostgresTodoRepository(password);

        try {
            List<Todo> todos = repository.findAll();

            System.out.println("Doc database thanh cong!");

            for (Todo todo : todos) {
                System.out.println(todo);
            }
        } catch (SQLException error) {
            System.out.println("Loi database: " + error.getMessage());
        }
    }
}