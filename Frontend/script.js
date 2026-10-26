const todoList = document.querySelector("#todo-list");
const todos = [
    { title: "Learn JavaScript", completed: true },
    { title: "Do Gym", completed: true },
    { title: "Learn IELTS", completed: false }
];

function renderTodos(todos) {
    todoList.textContent = "";

    for (const todo of todos) {
        const item = document.createElement("li");
        if(todo.completed === false) {
            item.textContent = todo.title;
        } else {
            item.textContent = "✅ " + todo.title;
        }
        todoList.append(item);
    }
}

renderTodos(todos);

