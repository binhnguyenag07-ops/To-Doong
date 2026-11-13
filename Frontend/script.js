const todoList = document.querySelector("#todo-list");
const completedList = document.querySelector("#completed-list");
const message = document.querySelector("#message");
const todoForm = document.querySelector("#todo-form");
const todoInput = document.querySelector("#todo-title");
const addTodoButton = document.querySelector("#add-todo-button");
const todoCount = document.querySelector("#todo-count");
const completedTodoCount = document.querySelector("#completed-count");

function renderTodos(todos) {
    todoList.textContent = "";
    completedList.textContent = "";

    let completedCount = 0;

    for (const todo of todos) {
        const item = document.createElement("li");
        item.className = "todo-item";
        const title = document.createElement("span");
        title.className = "todo-text";
        title.textContent = todo.title;

        if (todo.completed) {
            item.classList.add("is-completed");
            completedCount++;
        }

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.checked = todo.completed;
        checkbox.setAttribute("aria-label", `Mark ${todo.title} as ${todo.completed ? "incomplete" : "complete"}`);
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


        const taskLabel = document.createElement("label");
        taskLabel.className = "todo-label";
        taskLabel.append(checkbox, title);
        item.append(taskLabel);

        const actions = document.createElement("div");
        actions.className = "todo-actions";

        const deleteButton = document.createElement("button");
        deleteButton.type = "button";
        deleteButton.textContent = "Delete";
        deleteButton.className = "delete-button";
        deleteButton.setAttribute("aria-label", `Delete ${todo.title}`);

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

        const editButton = document.createElement("button");
        editButton.type = "button";
        editButton.textContent = "Edit";
        editButton.setAttribute("aria-label", `Edit ${todo.title}`);

        editButton.addEventListener("click", async function () {
            const input = window.prompt("Enter the new todo title:", todo.title);

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

        actions.append(editButton, deleteButton);
        item.append(actions);

        const targetList = todo.completed ? completedList : todoList;
        targetList.append(item);
    }
    const pendingCount = todos.length - completedCount;
    todoCount.textContent = `${pendingCount} ${pendingCount === 1 ? "task" : "tasks"}`;
    completedTodoCount.textContent = `${completedCount} ${completedCount === 1 ? "task" : "tasks"}`;

    if (pendingCount === 0) {
        showEmptyState(todoList, "Nothing to do. Add a task when you're ready.");
    }
    if (completedCount === 0) {
        showEmptyState(completedList, "Completed tasks will appear here.");
    }
}

function showEmptyState(list, text) {
    const emptyState = document.createElement("li");
    emptyState.className = "empty-state";
    emptyState.textContent = text;
    list.append(emptyState);
}

async function loadTodos() {
    message.textContent = "Loading...";
    try {
        const response = await fetch("http://localhost:8080/todos");

        if (!response.ok) {
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


todoForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const title = todoInput.value.trim();
    if (title === "") {
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
