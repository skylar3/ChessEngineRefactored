package com.example.chessenginerefactored;

import javafx.scene.layout.GridPane;

// Pawn class implementing the Piece interface
class Pawn implements Piece {
    private final String type;
    private int row, col;
    private final boolean isWhite;

    public Pawn(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.isWhite = type.endsWith("w");
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public void updatePosition(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol, Piece[][] board) {
        int direction = isWhite ? -1 : 1; // White moves up (decreasing row), black moves down (increasing row)
        int startRow = isWhite ? 6 : 1; // Starting row for pawns (6 for white, 1 for black)

        // Move forward one square
        if (targetCol == col && targetRow == row + direction && board[targetRow][targetCol] == null) {
            return true;
        }
        // Move forward two squares from starting position
        if (row == startRow && targetCol == col && targetRow == row + 2 * direction
                && board[targetRow][targetCol] == null && board[row + direction][col] == null) {
            return true;
        }
        // Capture diagonally
        if (Math.abs(targetCol - col) == 1 && targetRow == row + direction
                && board[targetRow][targetCol] != null && board[targetRow][targetCol].isWhite() != isWhite) {
            return true;
        }
        return false;
    }
}
