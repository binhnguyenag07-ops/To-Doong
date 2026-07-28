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
        server.createContext("/todos", exchange -> {
            String method = exchange.getRequestMethod();
            System.out.println("Request method: " + method);
            String response;

            if(method.equals("GET")) {
                response = Main.todosToJson(service.getAllTodos());
            } else if(method.equals("POST")) {
                String title = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8);

                Todo newTodo = service.createTodo(title);
                response = newTodo.toJson();
            } else {
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
