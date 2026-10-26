const todoList = document.querySelector("#todo-list");
const message = document.querySelector("#message");
const todoForm = document.querySelector("#todo-form");
const todoInput = document.querySelector("#todo-title");
const addTodoButton = document.querySelector("#add-todo-button");
const todoCount = document.querySelector("#todo-count");

function renderTodos(todos) {
    todoList.textContent = "";
    
    todoCount.textContent = "Total : " + todos.length + " todos";

    for (const todo of todos) {
        const item = document.createElement("li");

        if(todo.completed === false) {
            item.textContent = todo.title;
        } else {
            item.textContent = "✅ " + todo.title;
        }

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.checked = todo.completed;
        checkbox.addEventListener("change", async function () {
        checkbox.disabled = true;
        message.textContent = "Updating status...";

        try {
            const response = await fetch(
                `http://localhost:8080/todos/${todo.id}/completed`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "text/plain;charset=UTF-8"
                    },
                    body: String(checkbox.checked)
                }
            );

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const result = await response.json();

            if (result.error) {
                throw new Error(result.error);
            }
        } catch (error) {
            checkbox.checked = todo.completed;
            message.textContent = "Cannot update status";
            console.error(error);
            return;
        } finally {
            checkbox.disabled = false;
        }

        await loadTodos();
    });

        item.append(checkbox);

        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.textContent = "Delete";
        
        deleteButton.addEventListener("click", async function () {
        deleteButton.disabled = true;
        message.textContent = "Deleting todo...";

        try {
            const response = await fetch(
                `http://localhost:8080/todos/${todo.id}`,
                { method: "DELETE" }
            );

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const result = await response.json();

            if (result.error) {
                throw new Error(result.error);
            }
        } catch (error) {
            message.textContent = "Cannot delete todo";
            console.error(error);
            return;
        } finally {
            deleteButton.disabled = false;
        }

        await loadTodos();
    });
        
        item.append(deleteButton);

        const editButton = document.createElement("button");
        editButton.type = "button";
        editButton.textContent = "Edit";

        editButton.addEventListener("click", async function () {
        const input = window.prompt("Nhập tên công việc mới:", todo.title);

        if (input === null) {
            return;
        }

        const newTitle = input.trim();

        if (newTitle === "") {
            message.textContent = "Title cannot be empty";
            return;
        }

        editButton.disabled = true;
        message.textContent = "Updating todo...";

        try {
            const response = await fetch(
                `http://localhost:8080/todos/${todo.id}`,
                {
                    method: "PUT",
                    headers: {
                        "Content-Type": "text/plain;charset=UTF-8"
                    },
                    body: newTitle
                }
            );

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }

            const result = await response.json();

            if (result.error) {
                throw new Error(result.error);
            }
        } catch (error) {
            message.textContent = "Cannot update todo";
            console.error(error);
            return;
        } finally {
            editButton.disabled = false;
        }

        await loadTodos();
});

        item.append(editButton);
        
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
    
    if (addTodoButton.disabled) {
        return;
    }

    addTodoButton.disabled = true;

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
    } finally {
        addTodoButton.disabled = false;
    }

    await loadTodos();
})