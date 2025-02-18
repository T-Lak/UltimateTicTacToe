function nextScreen(screenNumber) {
    document.querySelectorAll('.menu-screen').forEach(screen => screen.classList.add("hidden"));
    document.getElementById(`screen${screenNumber}`).classList.remove("hidden");
}

function startGame() {
    window.pywebview.api.load_game();
}