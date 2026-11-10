import Repository.PostgresTodoRepository;
import model.Todo;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseTest {
    public static void main(String[] args) {
        if (System.console() == null) {
            System.out.println("Hay chay bang terminal theo huong dan.");
            return;
        }

        String password = new String(
                System.console().readPassword("Mat khau PostgreSQL: "));

        PostgresTodoRepository repository = new PostgresTodoRepository(password);

        try {
            Todo newTodo = new Todo(
                    UUID.randomUUID().toString(),
                    "Add Todo from Java",
                    false);

            repository.save(newTodo);

            Todo found = repository.findByID(newTodo.getID());
            System.out.println("Tim theo ID: " + found);

            Todo missing = repository.findByID("id-khong-ton-tai");
            System.out.println("ID khong ton tai: " + missing);

            System.out.println("Da luu Todo vao PostgreSQL!");
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