function handleCellClicked(cell, boardIndex, cellIndex) {
    if (isDisabled(cell) || isMarked(cell)) return;

    disableBoard();

    window.pywebview.api.is_valid_move(boardIndex, cellIndex).then((isValid) => {
        if (isValid) {
            window.pywebview.api.send_move(boardIndex, cellIndex);
        } else {
            enableBoard();
        }
    });
}

function updateBoard(board) {
    let cells = document.querySelectorAll(".cell");
    let index = 0;
    for (let i = 0; i < 9; i++) {
        for (let j = 0; j < 9; j++) {
            cells[index].innerText = board[i][j];

            // if (cells[index].innerText === "O") {
            //     cells[index].style.color = "#3babd1";
            // } else if (cells[index].innerText === "X") {
            //     cells[index].style.color = "#d15486";
            // }
            index++;
        }
    }
}

function enableBoard() {
    document.querySelectorAll(".cell").forEach(cell => {
        cell.classList.remove("disabled"); // Remove class from all cells
    });
}

function disableBoard() {
    document.querySelectorAll(".cell").forEach(cell => {
        cell.classList.add("disabled"); // Add class to all cells
    });
}

function isDisabled(cell) {
    return cell.classList.contains("disabled")
}

function isMarked(cell) {
    return cell.innerText === "X" || cell.innerText === "O";
}