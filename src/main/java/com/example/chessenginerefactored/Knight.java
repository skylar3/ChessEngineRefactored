package com.example.chessenginerefactored;

public class Knight implements Piece {
    private final String type;
    private int row, col;
    private final boolean isWhite;
    public Knight(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.isWhite = type.endsWith("w");

    }
    @Override
    public String getType()
    {
        return type;
    }
    @Override
    public int getRow()
    {
        return row;
    }

    @Override
    public int getCol()
    {
        return col;
    }
    @Override
    public void updatePosition(int newRow, int newCol)
    {
        this.row = newRow;
        this.col = newCol;
    }
    @Override
    public boolean isWhite(){
        return isWhite;
    }
    @Override
    public boolean isValidMove(int targetRow, int targetCol, Piece[][] board) {
        // If the target is the current position, it's not a valid move
        if (targetRow == row && targetCol == col) {
            return false;
        }

        // Calculate differences in row and column
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetCol - col);

        // Knight moves in an L-shape: (2,1) or (1,2)
        boolean isLShape = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

        if (!isLShape) {
            return false; // Not a valid knight move
        }

        // Check the target square: empty or contains an opponent's piece
        Piece targetPiece = board[targetRow][targetCol];
        return targetPiece == null || targetPiece.isWhite() != isWhite;
    }

}

