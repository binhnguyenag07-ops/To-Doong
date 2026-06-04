package Service;


import java.util.List;

import java.util.UUID;

import model.Todo;


import Repository.TodoRepository;

public class TodoService {
    private TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public Todo createTodo(String title) {
        if(title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("This Title can not be empty");
        }

        String id = UUID.randomUUID().toString();
        Todo todo = new Todo(id,title.trim(),false);

        repository.save(todo);
        return todo;
    }
    
    public boolean updateTodo(String id, String newTitle, boolean newCompleted) {
        if(newTitle == null || newTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("This Title can not be empty");
        }
        Todo todo = repository.findByID(id);

        if(todo == null) {
            return false;
        }

        todo.setTitle(newTitle.trim());
        todo.setCompleted(newCompleted);
        return true;
    }

    public boolean finishTodo(String id) {
        Todo todo = repository.findByID(id);

        if(todo == null) {
            return false;
        }

        todo.setCompleted(true);
        return true;
    }

    public boolean deleteTodo(String id) {
        return repository.deleteByID(id);
    }

    public List<Todo> getAllTodos() {
        return repository.findAll();
    }
}