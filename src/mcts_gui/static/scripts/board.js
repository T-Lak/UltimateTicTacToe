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

function processData(data) {
    const board = data.board;
    const lastMove = data.lastMove;
    const decidedBoards = data.decidedBoards;

    updateBoard(board, decidedBoards, lastMove);
    updateMoveHistory(lastMove);
}

function updateMoveHistory(move) {
    const container = document.getElementById("move-history-container");

    if (!container) {
        return;
    }

    const index = move.index;
    const player = move.player;
    const boardIndex = move.board;
    const position = move.position;

    const moveIndex = document.createElement("div");
    moveIndex.classList.add("move");
    moveIndex.textContent = index;

    const playerCell = document.createElement("div");
    playerCell.classList.add("move");
    playerCell.textContent = player;

    const boardCell = document.createElement("div");
    boardCell.classList.add("move");
    boardCell.textContent = boardIndex;

    const positionCell = document.createElement("div");
    positionCell.classList.add("move");
    positionCell.textContent = position;

    container.appendChild(moveIndex);
    container.appendChild(playerCell);
    container.appendChild(boardCell);
    container.appendChild(positionCell);

    container.scrollTop = container.scrollHeight;
}

function updateBoard(board, decidedBoards, move) {
    let cells = document.querySelectorAll(".cell");

    let index = 0;
    for (let i = 0; i < 9; i++) {
        for (let j = 0; j < 9; j++) {
            cells[index].innerText = board[i][j];

            if (move.board === i && move.position === j) {
                cells[index].classList.add("last-move");
            } else {
                cells[index].classList.remove("last-move");

                if (decidedBoards[i] !== undefined) {
                    if (decidedBoards[i] === "") {
                        cells[index].classList.add("decided-draw"); // Draw
                    } else if (decidedBoards[i] === "X") {
                        cells[index].classList.add("decided-human"); // Human win
                    } else if (decidedBoards[i] === "O") {
                        cells[index].classList.add("decided-ai"); // AI win
                    }
                }
            }
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

function handleQuit() {
    window.pywebview.api.load_menu();
}