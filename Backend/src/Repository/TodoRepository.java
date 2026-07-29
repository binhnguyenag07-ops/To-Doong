package Repository;

import java.util.ArrayList;
import java.util.List;

import model.Todo;


public class TodoRepository {
    private List<Todo> storage = new ArrayList<>();

    
    //save a todo to List<Todo> storage
    public void save(Todo todo) {
        storage.add(todo);
    }

    //find a todo in storage by its ID
    public Todo findByID(String id) {
        for(Todo todo: storage) {
            if(todo.getID().equals(id)) {
                return todo;
            }
        }

        return null;
    }

    //find and remove a todo in storage
    public boolean deleteByID(String id) {
        Todo todo = findByID(id);
        
        if(todo == null) {
            return false;
        }

        storage.remove(todo);
        return true;
    }

    //the current storage
    public List<Todo> findAll() {
        return storage;
    }
}
