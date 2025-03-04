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
    public boolean isValidMove(int targetRow, int targetCol, Piece[][] board)
    {
        if ((targetCol == col - 1 && (targetRow == (row +2) || targetRow == (row - 2)) ||
                targetCol == col + 1 && (targetRow == (row+2) || targetRow == (row - 2)) ||
                ((targetCol == col + 2 || targetCol == col - 2) && (targetRow ==(row + 1) || targetRow == row - 1)) &&
                        (board[targetRow][targetCol] == null)) )
        {

            return true;
        }
        else {
            return false;
        }

    }

}
