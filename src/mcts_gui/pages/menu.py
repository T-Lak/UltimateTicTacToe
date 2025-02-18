menu_html = """
<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Ultimate Tic-Tac-Toe - Menu</title>
    <style>
        body {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #282c36;
            color: white;
            font-family: Arial, sans-serif;
        }

        .menu-screen {
            text-align: center;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .hidden {
            display: none;
        }

        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .container > button {
            height: 50px;
            width: 100px;
            font-size: 18px;
        }

        button {
            height: 70px;
            width: 230px;
            font-size: 20px;
            margin: 10px;
            border: none;
            border-radius: 5px;
            background-color: #007BFF;
            color: white;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div id="screen1" class="menu-screen">
        <button onclick="startGame()">Start Game</button>
        <button onclick="nextScreen(2)">Settings</button>
        <button onclick="nextScreen(3)">Instructions</button>
    </div>
    <div id="screen2" class="menu-screen hidden">
        <div class="container">
            <button onclick="nextScreen(1)">Back</button>
        </div>
    </div>
    <div id="screen3" class="menu-screen hidden">
        <div class="container">
            <button onclick="nextScreen(1)">Back</button>
        </div>
    </div>

    <script>
        function nextScreen(screenNumber) {
            document.querySelectorAll('.menu-screen').forEach(screen => screen.classList.add("hidden"));
            document.getElementById('screen' + screenNumber).classList.remove("hidden");
        }

        function startGame() {
            window.pywebview.api.load_game();
        }
    </script>
</body>
</html>
"""