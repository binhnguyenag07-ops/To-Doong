import Repository.PostgresTodoRepository;
import model.Todo;
import Service.TodoService;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DatabaseTest {
    public static void main(String[] args) {
        if (System.console() == null) {
            System.out.println("Please run this program from a terminal.");
            return;
        }

        String password = new String(
                System.console().readPassword("PostgreSQL password: "));

        PostgresTodoRepository repository = new PostgresTodoRepository(password);
        TodoService service = new TodoService(repository);

        try {
            Todo newTodo = service.createTodo("Add Todo From Java");

            boolean updated = service.updateTodo(
                    newTodo.getID(),
                    "Updated title through Service");

            System.out.println("Update successful: " + updated);

            Todo afterUpdate = repository.findByID(newTodo.getID());
            System.out.println("Todo after update: " + afterUpdate);

            boolean missingUpdated = repository.updateTitle(
                    "nonexistent-id",
                    "New title");

            System.out.println("Update nonexistent ID: " + missingUpdated);

            Todo found = repository.findByID(newTodo.getID());
            System.out.println("Find by ID: " + found);

            Todo missing = repository.findByID("nonexistent-id");
            System.out.println("Nonexistent ID: " + missing);

            System.out.println("Todo saved to PostgreSQL!");

            System.out.println("Database read successful!");

            boolean completedUpdated = repository.updateCompleted(newTodo.getID(), true);

            System.out.println("Mark as completed: " + completedUpdated);
            System.out.println(repository.findByID(newTodo.getID()));

            boolean reopened = repository.updateCompleted(newTodo.getID(), false);

            System.out.println("Mark as incomplete: " + reopened);
            System.out.println(repository.findByID(newTodo.getID()));

            boolean missingStatusUpdated = repository.updateCompleted("nonexistent-id", true);

            System.out.println("Nonexistent ID: " + missingStatusUpdated);

            boolean deleted = repository.deleteByID(newTodo.getID());
            System.out.println("Delete todo: " + deleted);

            Todo afterDelete = repository.findByID(newTodo.getID());
            System.out.println("Find after deletion: " + afterDelete);

            boolean deletedAgain = repository.deleteByID(newTodo.getID());
            System.out.println("Delete again: " + deletedAgain);

           
        } catch (SQLException error) {
            System.out.println("Database error: " + error.getMessage());
        }
    }
}