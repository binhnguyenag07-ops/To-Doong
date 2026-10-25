const todoList = document.querySelector("#todo-list");
const titles = ["Learn JavaScript", "Do Gym", "Learn IELTS"];

function renderTodos(titles) {
    for (const title of titles) {
        const item = document.createElement("li");
        item.textContent = title;
        todoList.append(item);
    }
}

renderTodos(titles);