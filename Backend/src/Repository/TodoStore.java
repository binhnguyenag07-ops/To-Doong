package Repository;

import java.sql.SQLException;
import java.util.List;
import model.Todo;

public interface TodoStore {
    void save(Todo todo) throws SQLException;

    Todo findByID(String id) throws SQLException;

    List<Todo> findAll() throws SQLException;

    boolean updateTitle(String id, String title) throws SQLException;

    boolean updateCompleted(String id, boolean completed)
        throws SQLException;

    boolean deleteByID(String id) throws SQLException;
}