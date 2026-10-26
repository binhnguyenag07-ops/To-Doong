const todoList = document.querySelector("#todo-list");


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

async function loadTodos() {
    const response = await fetch("http://localhost:8080/todos");
    const todos = await response.json();

    renderTodos(todos);
}

loadTodos();
