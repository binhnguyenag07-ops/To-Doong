package model;

public class Todo {
    private String id;
    private String title;
    private boolean isCompleted;

    public Todo(String todoId, String title, boolean isCompleted) {
        this.id = todoId;
        this.title = title;
        this.isCompleted = isCompleted;
    }

    public String getTitle() {
        return title;
    }
    
    public boolean getCompleted() {
        return isCompleted;
    }

    public String getID() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", completed=" + isCompleted +
                '}';
    }

    public String toJson() {
    return "{"
            + "\"id\":\"" + id + "\","
            + "\"title\":\"" + title + "\","
            + "\"completed\":" + isCompleted
            + "}";
    }
}
