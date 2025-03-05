package com.example.chessenginerefactored;

import javafx.scene.layout.GridPane;

import javax.swing.*;

// Pawn class implementing the Piece interface
class King implements Piece {
    private final String type;
    private int row, col;
    private final boolean isWhite;

    public King(String type, int row, int col) {
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
        // If the target is the current position, it's not a valid move
        if (targetRow == row && targetCol == col) {
            return false;
        }

        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetCol - col);

        // Basic king movement: one square in any direction
        boolean isOneSquareMove = (rowDiff <= 1 && colDiff <= 1) && (rowDiff > 0 || colDiff > 0);
        if (isOneSquareMove) {
            Piece targetPiece = board[targetRow][targetCol];
            // Valid if the target is empty or has an opponent's piece
            return targetPiece == null || targetPiece.isWhite() != isWhite;

        }
        return false;
    }


}
