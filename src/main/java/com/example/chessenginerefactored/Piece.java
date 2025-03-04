package com.example.chessenginerefactored;

import javafx.scene.layout.GridPane;
// Interface for all chess pieces
interface Piece {
    String getType(); // e.g., "pw" for white pawn
    int getRow();
    int getCol();
    boolean isWhite();
    void updatePosition(int newRow, int newCol);
    boolean isValidMove(int targetRow, int targetCol, Piece[][] board);
}
