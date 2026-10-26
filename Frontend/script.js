const todoList = document.querySelector("#todo-list");
const message = document.querySelector("#message");
const todoForm = document.querySelector("#todo-form");
const todoInput = document.querySelector("#todo-title");

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
    message.textContent = "Loading...";
    try {
    const response = await fetch("http://localhost:8080/todos");

    if(!response.ok) {
        throw new Error(`HTTP ${response.status}`);
    }

    const todos = await response.json();

    renderTodos(todos);

    message.textContent = "";
    } catch (error) {
        message.textContent = "Cannot load the todo list";
        console.error(error);
    }

}

loadTodos();

 
todoForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const title = todoInput.value.trim() ;
    if(title === "") {
        message.textContent = "Title cannot be empty";
        return;
    }
    
    message.textContent = "Adding todo...";

    try {
        const response = await fetch("http://localhost:8080/todos", {
            method: "POST",
            headers: {
                "Content-Type": "text/plain;charset=UTF-8"
            },
            body: title
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }

        todoInput.value = "";
    } catch (error) {
        message.textContent = "Cannot add todo";
        console.error(error);
        return;
    }

    await loadTodos();
})