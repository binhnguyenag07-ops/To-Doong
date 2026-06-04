import Repository.TodoRepository;
import Service.TodoService;
import model.Todo;

public class Main {
    public static void main(String[] args) {
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);

        Todo learnJava = service.createTodo("Learn Java");
        Todo gym = service.createTodo("Go to gym");

        System.out.println(service.getAllTodos());

        service.finishTodo(learnJava.getID());
        service.updateTodo(gym.getID(), "Go to gym at 6PM", false);

        System.out.println(service.getAllTodos());

        boolean deleted = service.deleteTodo(learnJava.getID());
        System.out.println("Deleted: " + deleted);

        boolean deletedFake = service.deleteTodo("wrong-id");
        System.out.println("Deleted fake id: " + deletedFake);

        System.out.println(service.getAllTodos());

    }
}