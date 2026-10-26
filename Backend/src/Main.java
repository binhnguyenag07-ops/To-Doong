import java.util.List;
import java.util.Scanner;

import Repository.TodoRepository;
import Service.TodoService;
import model.Todo;

public class Main {
    public static void main(String[] args) {
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while(running) {
            System.out.println("==== TODOONG ====");
            System.out.println("1. See Todo");
            System.out.println("2. Add Todo");
            System.out.println("3. Delete Todo");
            System.out.println("4. Finish Todo");
            System.out.println("5. Update Todo");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            if( choice.equals("1")) {
                System.out.println(todosToJson(service.getAllTodos()));
            }

            else if (choice.equals("2")) {
                System.out.println("Enter Title: ");
                String title = scanner.nextLine();

                service.createTodo(title);
                System.out.println("Todo Added");
            }

            else if(choice.equals("3")) {
                System.out.println("Enter ID: ");
                String id = scanner.nextLine();

                boolean deleted = service.deleteTodo(id);

                if(deleted) {
                    System.out.println("Todo was deleted");
                } else {
                    System.out.println("Cannot find Todo");
                }
            }

            else if(choice.equals("4")) {
                System.out.println("Enter ID: ");
                String id = scanner.nextLine();

                boolean finished = service.setTodoCompleted(id, true);
                if(finished) {
                    System.out.println("Todo finished");
                } else {
                    System.out.println("Cannot find Todo");
                }
            }

            else if(choice.equals("5")) {
                System.out.println("Enter ID: ");
                String id = scanner.nextLine();

                System.out.println("New Title: ");
                String title = scanner.nextLine();

                Todo todo = repository.findByID(id);

                if(todo == null) {
                    System.out.println("Cannot find Todo");
                } else {
                    boolean updated = service.updateTodo(id, title);

                    if(updated) {
                        System.out.println("Todo was updated");
                    }
                }

            }
            else if(choice.equals("0")) {
                running = false;
            }

            else {
                System.out.println("Invalid! Please enter again!");
            }
        }
        scanner.close();

    }
    public static String todosToJson(List<Todo> todos) {
            if (todos.isEmpty()) {
                return "[]";
            }
            String json = "[";
            for(int i = 0; i < todos.size() - 1; i++) {
                json = json + todos.get(i).toJson();
                json = json + ",";
            }
            int n = todos.size() - 1;
            json = json + todos.get(n).toJson();
            json = json + "]";
            return json;
        }
}