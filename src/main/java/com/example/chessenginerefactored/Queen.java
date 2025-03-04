package com.example.chessenginerefactored;

public class Queen implements Piece{
    private final String type;
    private int row, col;
    private final boolean isWhite;
    public Queen(String type, int row, int col) {
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

        int rowDiff = targetRow - row;
        int colDiff = targetCol - col;

        // Check if the move is horizontal, vertical, or diagonal
        boolean isHorizontal = rowDiff == 0 && colDiff != 0;
        boolean isVertical = colDiff == 0 && rowDiff != 0;
        boolean isDiagonal = Math.abs(rowDiff) == Math.abs(colDiff) && rowDiff != 0;

        if (!(isHorizontal || isVertical || isDiagonal)) {
            return false; // Not a valid queen move
        }

        // Determine the step direction for checking the path
        int stepRow = rowDiff == 0 ? 0 : (rowDiff > 0 ? 1 : -1);
        int stepCol = colDiff == 0 ? 0 : (colDiff > 0 ? 1 : -1);
        int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));

        // Check the path for obstacles
        for (int i = 1; i < steps; i++) {
            int checkRow = row + i * stepRow;
            int checkCol = col + i * stepCol;
            if (board[checkRow][checkCol] != null) {
                return false; // Path is blocked
            }
        }

        // Check the target square: empty or contains an opponent's piece
        Piece targetPiece = board[targetRow][targetCol];
        return targetPiece == null || targetPiece.isWhite() != isWhite;
    }
}
