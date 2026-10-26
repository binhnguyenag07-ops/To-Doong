import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import Repository.TodoRepository;
import Service.TodoService;
import model.Todo;

public class ApiMain {
    public static void main(String[] args) throws Exception{
        HttpServer server = 
            HttpServer.create(new InetSocketAddress(8080), 0);
        TodoRepository repository = new TodoRepository();
        TodoService service = new TodoService(repository);
        service.createTodo("Learning Java");
        service.createTodo("Learning Ielts");
        service.createTodo("Do Gym");
        server.createContext("/todos", exchange -> {
            exchange.getResponseHeaders().set(
            "Access-Control-Allow-Origin",
            "http://127.0.0.1:5500"
        );
            exchange.getResponseHeaders().set(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS"
        );

        if (exchange.getRequestMethod().equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return;
        }
            String method = exchange.getRequestMethod();
            System.out.println("Request method: " + method);
            String response;

            if(method.equals("GET")) {
                response = Main.todosToJson(service.getAllTodos());
            } 
            
            else if(method.equals("POST")) {
                String title = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8);

                Todo newTodo = service.createTodo(title);
                response = newTodo.toJson();
            } 
            
            else if (method.equals("DELETE")) {
                String path = exchange.getRequestURI().getPath();
                String prefix = "/todos/";

                if (!path.startsWith(prefix) || path.length() <= prefix.length()) {
                    response = "{\"error\":\"Todo ID is required\"}";
                } else {
                    String id = path.substring(prefix.length());
                    boolean deleted = service.deleteTodo(id);

                    if (deleted) {
                        response = "{\"message\":\"Todo deleted\"}";
                    } else {
                        response = "{\"error\":\"Todo not found\"}";
                    }
                }
            } 
            
            else if(method.equals("PUT")) {
                String path = exchange.getRequestURI().getPath();
                String prefix = "/todos/";

                if (!path.startsWith(prefix) || path.length() <= prefix.length()) {
                    response = "{\"error\":\"Todo ID is required\"}";
                } else {
                    String target = path.substring(prefix.length());

                    String body = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                    );

                    if (target.endsWith("/completed")) {
                        String id = target.substring(
                            0, target.length() - "/completed".length()
                        );

                        String value = body.trim();

                        if (id.isEmpty()) {
                            response = "{\"error\":\"Todo ID is required\"}";
                        } else if (!value.equals("true") && !value.equals("false")) {
                            response = "{\"error\":\"Completed must be true or false\"}";
                        } else {
                            boolean completed = Boolean.parseBoolean(value);
                            boolean updated = service.setTodoCompleted(id, completed);

                            if (updated) {
                                response = "{\"message\":\"Todo status updated\"}";
                            } else {
                                response = "{\"error\":\"Todo not found\"}";
                            }
                        }
                    } else {
                        boolean updated = service.updateTodo(target, body);

                        if (updated) {
                            response = "{\"message\":\"Todo updated\"}";
                        } else {
                            response = "{\"error\":\"Todo not found\"}";
                        }
                    }
                }
            }
            else {
                response = "{\"error\":\"Method not allowed\"}";
            }
            byte[] data = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders()
                    .set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
            exchange.close();
        });
        

        server.start();
        System.out.println("Server is running at http://localhost:8080/todos");
    }
}
