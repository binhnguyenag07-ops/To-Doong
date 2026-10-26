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

const todoForm = document.querySelector("#todo-form");
const todoInput = document.querySelector("#todo-title");
 
todoForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    const title = todoInput.value.trim() ;
    if(title === "") {
        console.log("Invalid title");
        return;
    }
    
    const response = await fetch("http://localhost:8080/todos", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain;charset=UTF-8"
        },
        body: title
    });

    if(!response.ok) {
        console.log("Adding todo was failed:" + response.status);
        return;
    }

    todoInput.value = "";
    await loadTodos();
})