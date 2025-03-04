package com.example.chessenginerefactored;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
        String gameState = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"; // Corrected to standard position

        // Load the pieces based on the game state string
        loadGameFromString(gameState, chessBoardLayout);

        // Enable drag-and-drop on the entire board
        enableBoardDragAndDrop(chessBoardLayout);

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
            pieceImageView.setUserData(piece); // Store piece type
            board.add(pieceImageView, col, row);
            enablePieceDrag(pieceImageView, board); // Enable dragging for this piece
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
                            case 'K' -> "kw";
                            case 'Q' -> "qw";
                            case 'R' -> "rw";
                            case 'B' -> "bw";
                            case 'N' -> "nw";
                            case 'P' -> "pw";
                            default -> null;
                        };
                    } else { // Black pieces (lowercase)
                        piece = switch (currentChar) {
                            case 'k' -> "kb";
                            case 'q' -> "qb";
                            case 'r' -> "rb";
                            case 'b' -> "bb";
                            case 'n' -> "nb";
                            case 'p' -> "pb";
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

    // Enable dragging for a specific piece
    private void enablePieceDrag(ImageView pieceImageView, GridPane board) {
        pieceImageView.setOnDragDetected(event -> {
            Dragboard db = pieceImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString((String) pieceImageView.getUserData()); // Piece type
            db.setContent(content);
            db.setDragView(pieceImageView.getImage(), 37.5, 37.5); // Center the drag image
            event.consume();
        });
    }

    // Enable drag-and-drop on the entire board
    private void enableBoardDragAndDrop(GridPane chessBoardLayout) {
        chessBoardLayout.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof ImageView && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        chessBoardLayout.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ImageView draggedPiece = (ImageView) event.getGestureSource();
                int startRow = GridPane.getRowIndex(draggedPiece);
                int startCol = GridPane.getColumnIndex(draggedPiece);

                // Calculate target position based on drop coordinates
                double squareSize = 75;
                int targetRow = (int) (event.getY() / squareSize);
                int targetCol = (int) (event.getX() / squareSize);

                // Ensure target is within bounds
                if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                    // Remove piece from old position
                    chessBoardLayout.getChildren().remove(draggedPiece);

                    // If target square is occupied, remove the piece there
                    ImageView targetPiece = getPieceAt(chessBoardLayout, targetRow, targetCol);
                    if (targetPiece != null) {
                        chessBoardLayout.getChildren().remove(targetPiece);
                    }

                    // Move the dragged piece to the new position
                    chessBoardLayout.add(draggedPiece, targetCol, targetRow);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Optional: Reset piece position if drag fails
        chessBoardLayout.setOnDragDone(event -> {
            if (!event.isDropCompleted()) {
                ImageView draggedPiece = (ImageView) event.getGestureSource();
                int startRow = GridPane.getRowIndex(draggedPiece);
                int startCol = GridPane.getColumnIndex(draggedPiece);
                chessBoardLayout.getChildren().remove(draggedPiece);
                chessBoardLayout.add(draggedPiece, startCol, startRow); // Return to original position
            }
            event.consume();
        });
    }
}