import webview

from py4j.java_gateway import JavaGateway

from pages.menu import menu_html
from pages.board import board_html

class Api:
    def __init__(self):
        try:
            self.gateway = JavaGateway()
        except Exception as e:
            print(e)

    def load_game(self):
        webview.windows[0].load_html(board_html)

    def send_move(self, board_index, cell_index):
        board = self.gateway.entry_point.performHumanMove(board_index, cell_index)

        for row in board:
            print(row)

        self.update_gui(board)
        self.query_ai_move()

    def is_valid_move(self, board_index, cell_index):
        return self.gateway.entry_point.isValidHumanMove(board_index, cell_index)

    def query_ai_move(self):
        board = self.gateway.entry_point.performAIMove()
        self.update_gui(board)
        js_function = f"enableBoard()"
        webview.windows[0].evaluate_js(js_function)

    @staticmethod
    def update_gui(board):
        js_function = f"updateBoard({board})"
        webview.windows[0].evaluate_js(js_function)


if __name__ == "__main__":
    api = Api()
    webview.create_window("Ultimate Tic-Tac-Toe", html=menu_html, js_api=api)
    webview.start(gui="qt")