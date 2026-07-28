import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import Repository.TodoRepository;
import Service.TodoService;

public class ApiMain {
    public static void main(String[] args) throws Exception{
        HttpServer server = 
            HttpServer.create(new InetSocketAddress(8080), 0);
            TodoRepository repository = new TodoRepository();
            TodoService service = new TodoService(repository);
        server.createContext("/todos", exchange -> {
            String method = exchange.getRequestMethod();
            System.out.println("Request method: " + method);
            String response = Main.todosToJson(service.getAllTodos());
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
