package Service;


import java.util.List;

import java.util.UUID;

import model.Todo;

import java.io.File;
import java.util.ArrayList;

import Repository.TodoRepository;

public class TodoService {
    private File todoFile =;
    private List<Todo> todos = new ArrayList<>();

    public Todo CreateTodo(String title) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        Todo todo = new Todo(id, title, false);

        boolean saved = TodoRepository.store(todo,todoFile)
        return todo;
    }

    public List addTodo(Todo todo) {
        todos.add(todo);
        
        return todos;
    }
}