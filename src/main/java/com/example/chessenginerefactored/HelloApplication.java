package com.example.chessenginerefactored;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HelloApplication extends Application {

    private final Map<String, Image> pieceImages = new HashMap<>();

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        // Create chessboard layout
        GridPane chessBoardLayout = createChessBoard();

        // Load all chess piece images
        loadChessPieces();

        // Add background and chessboard layout to root
        root.getChildren().addAll(createChessBoardImage(), chessBoardLayout);

        // Example: Initial game state (standard chess starting position)
        String initialGameState = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

        // Load the pieces based on the game state string
        loadGameFromString(initialGameState, chessBoardLayout);

        // Set scene
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Chess Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private GridPane createChessBoard() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(0);
        gridPane.setVgap(0);

        double squareSize = 75;
        for (int i = 0; i < 8; i++) {
            gridPane.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(squareSize));
            gridPane.getRowConstraints().add(new javafx.scene.layout.RowConstraints(squareSize));
        }

        return gridPane;
    }

    private void loadChessPieces() {
        String[] pieces = {"bb", "bw", "kb", "kw", "qb", "qw", "rb", "rw", "nb", "nw", "pb", "pw"};
        for (String piece : pieces) {
            String path = "/com/example/chessenginerefactored/Chess Assets/Chess Pieces/" + piece + ".png";
            pieceImages.put(piece, new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
        }
    }

    private ImageView createChessBoardImage() {
        Image chessBoardImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/chessenginerefactored/Chess Assets/ChessBoard.png")));
        ImageView imageView = new ImageView(chessBoardImage);
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        return imageView;
    }

    private void placePiece(GridPane board, String piece, int row, int col) {
        if (getPieceAt(board, row, col) != null) {
            board.getChildren().remove(getPieceAt(board, row, col));
        }

        Image pieceImage = pieceImages.get(piece);
        if (pieceImage != null) {
            ImageView pieceImageView = new ImageView(pieceImage);
            pieceImageView.setFitWidth(75);
            pieceImageView.setFitHeight(75);
            board.add(pieceImageView, col, row);
        }
    }

    private ImageView getPieceAt(GridPane board, int row, int col) {
        for (javafx.scene.Node child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == col) {
                return (ImageView) child;
            }
        }
        return null;
    }

    private void loadGameFromString(String gameState, GridPane chessBoardLayout) {
        String[] rows = gameState.split("/");

        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            String row = rows[rowIndex];
            int colIndex = 0;
            for (int i = 0; i < row.length(); i++) {
                char currentChar = row.charAt(i);

                if (Character.isDigit(currentChar)) {
                    int emptySquares = Character.getNumericValue(currentChar);
                    colIndex += emptySquares;
                } else {
                    String piece;
                    if (Character.isUpperCase(currentChar)) { // White pieces
                        piece = switch (currentChar) {
                            case 'K' -> "kw"; // White King
                            case 'Q' -> "qw"; // White Queen
                            case 'R' -> "rw"; // White Rook
                            case 'B' -> "bw"; // White Bishop
                            case 'N' -> "nw"; // White Knight
                            case 'P' -> "pw"; // White Pawn
                            default -> null;
                        };
                    } else { // Black pieces (lowercase)
                        piece = switch (currentChar) {
                            case 'k' -> "kb"; // Black King
                            case 'q' -> "qb"; // Black Queen
                            case 'r' -> "rb"; // Black Rook
                            case 'b' -> "bb"; // Black Bishop
                            case 'n' -> "nb"; // Black Knight
                            case 'p' -> "pb"; // Black Pawn
                            default -> null;
                        };
                    }

                    if (piece != null) {
                        placePiece(chessBoardLayout, piece, rowIndex, colIndex);
                        colIndex++;
                    }
                }
            }
        }
    }
}