package Repository;

import java.io.*;
import model.Todo;

public class TodoRepository {

    private void createParentFolderIfNeeded(File file) {
        File parentFile = file.getParentFile();

        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    public boolean createFile(String filePath) {
        File file = new File(filePath);

        try {
            createParentFolderIfNeeded(file);
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println("Cannot create file: " + e.getMessage());
            return false;
        }
    }

    public boolean store(Todo todo, File filePath) {
        try {
            createParentFolderIfNeeded(filePath);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));

            writer.write(todo.getID() + "," + todo.getTitle() + "," + todo.getCompleted());
            writer.newLine();

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Cannot store todo: " + e.getMessage());
            return false;
        }
    }

    public Todo findById(String id, File filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);

                String todoId = parts[0];
                String title = parts[1];
                boolean completed = Boolean.parseBoolean(parts[2]);

                if (todoId.equals(id)) {
                    reader.close();
                    return new Todo(todoId, title, completed);
                }
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Cannot find todo: " + e.getMessage());
        }

        return null;
    }
}