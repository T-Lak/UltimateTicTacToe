board_html = """
<!DOCTYPE html>
<html lang='en'>
    <head>
        <meta charset='UTF-8'>
        <meta name='viewport' content='width=device-width, initial-scale=1.0'>
        <title>Ultimate Tic-Tac-Toe - Game</title>
        <link rel='stylesheet' href='static/styles/board.css'>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <script src='static/scripts/board.js' defer></script>
    </head>
    <body>
        <div class='content'>
            <div class='board'>
                """ + "".join(
                    [f"""
                    <div class='inner-board' data-inner-row='{i}'>
                        """ + "".join(
                            [f"<div "
                                f"class='cell' "
                                f"data-row='{i}' "
                                f"data-col='{j}' "
                                f"onclick='handleCellClicked(this, {i}, {j})'"
                             f">"
                             f"</div>"
                             for j in range(9)
                            ]
                        ) + """
                    </div>
                    """ for i in range(9)]
                ) + """
            </div>
            <div class='menu'>
                <div class="move-history-header">
                    <div class="header">#</div>
                    <div class="header">Player</div>
                    <div class="header">Board</div>
                    <div class="header">Position</div>
                </div>
                <div id="move-history-container" class='move-history-container'>
                </div>
            </div>
             <div class='button-container'>
                    <button onclick="handleQuit()">X</button>
                </div>
        </div>
    </body>
</html>
"""