package Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import Repository.TodoStore;
import model.Todo;

public class TodoService {
    private final TodoStore repository;

    public TodoService(TodoStore repository) {
        this.repository = repository;
    }

    public Todo createTodo(String title) throws SQLException {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("This Title can not be empty");
        }

        Todo todo = new Todo(
            UUID.randomUUID().toString(),
            title.trim(),
            false
        );

        repository.save(todo);
        return todo;
    }

    public boolean updateTodo(String id, String newTitle)
            throws SQLException {

        if (newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("This Title can not be empty");
        }

        return repository.updateTitle(id, newTitle.trim());
    }

    public boolean setTodoCompleted(String id, boolean completed)
            throws SQLException {

        return repository.updateCompleted(id, completed);
    }

    public boolean deleteTodo(String id) throws SQLException {
        return repository.deleteByID(id);
    }

    public List<Todo> getAllTodos() throws SQLException {
        return repository.findAll();
    }
}