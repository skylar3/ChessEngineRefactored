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
    private Piece[][] boardState = new Piece[8][8]; // Updated to use Piece interface
    int roundNumber = 0;

    @Override
    public void start(Stage stage) {
        Group root = new Group();

        GridPane chessBoardLayout = createChessBoard();
        loadChessPieces();
        root.getChildren().addAll(createChessBoardImage(), chessBoardLayout);

        String gameState = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        loadGameFromString(gameState, chessBoardLayout);

        enableBoardDragAndDrop(chessBoardLayout);

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

    private void placePiece(GridPane board, Piece piece, int row, int col) {
        if (getPieceAt(board, row, col) != null) {
            board.getChildren().remove(getPieceAt(board, row, col));
        }

        Image pieceImage = pieceImages.get(piece.getType());
        if (pieceImage != null) {
            ImageView pieceImageView = new ImageView(pieceImage);
            pieceImageView.setFitWidth(75);
            pieceImageView.setFitHeight(75);
            pieceImageView.setUserData(piece); // Store the Piece object
            board.add(pieceImageView, col, row);
            enablePieceDrag(pieceImageView, board);
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = null;
            }
        }

        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            String row = rows[rowIndex];
            int colIndex = 0;
            for (int i = 0; i < row.length(); i++) {
                char currentChar = row.charAt(i);

                if (Character.isDigit(currentChar)) {
                    colIndex += Character.getNumericValue(currentChar);
                } else {
                    String pieceType = mapCharToPiece(currentChar);
                    if (pieceType != null) {
                        Piece piece = createPiece(pieceType, rowIndex, colIndex);
                        boardState[rowIndex][colIndex] = piece;
                        placePiece(chessBoardLayout, piece, rowIndex, colIndex);
                        colIndex++;
                    }
                }
            }
        }
    }

    private String mapCharToPiece(char c) {
        return switch (Character.toLowerCase(c)) {
            case 'k' -> Character.isUpperCase(c) ? "kw" : "kb";
            case 'q' -> Character.isUpperCase(c) ? "qw" : "qb";
            case 'r' -> Character.isUpperCase(c) ? "rw" : "rb";
            case 'b' -> Character.isUpperCase(c) ? "bw" : "bb";
            case 'n' -> Character.isUpperCase(c) ? "nw" : "nb";
            case 'p' -> Character.isUpperCase(c) ? "pw" : "pb";
            default -> null;
        };
    }

    private Piece createPiece(String type, int row, int col) {
        // For now, only Pawn is implemented; others return null or a placeholder
        return switch (type.charAt(0)) {
            case 'p' -> new Pawn(type, row, col);
            case 'n' -> new Knight(type, row, col);
            case 'q' -> new Queen(type, row, col);
            case 'r' -> new Rook(type, row, col);
            case 'b' -> new Bishop(type, row, col);
            default -> new Pawn(type, row, col); // Temporary placeholder for other pieces
        };
    }

    private void enablePieceDrag(ImageView pieceImageView, GridPane board) {
        pieceImageView.setOnDragDetected(event -> {
            Dragboard db = pieceImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(((Piece) pieceImageView.getUserData()).getType());
            db.setContent(content);
            db.setDragView(pieceImageView.getImage(), 37.5, 37.5);
            event.consume();
        });
    }

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
                Piece piece = (Piece) draggedPiece.getUserData();
                int startRow = GridPane.getRowIndex(draggedPiece);
                int startCol = GridPane.getColumnIndex(draggedPiece);

                double squareSize = 75;
                int targetRow = (int) (event.getY() / squareSize);
                int targetCol = (int) (event.getX() / squareSize);
                if ((piece.isWhite() && roundNumber %2 == 0) || (!piece.isWhite() && roundNumber %2 == 1))
                {
                if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                    // Validate move using piece logic
                    if (piece.isValidMove(targetRow, targetCol, boardState)) {
                        chessBoardLayout.getChildren().remove(draggedPiece);
                        ImageView targetPiece = getPieceAt(chessBoardLayout, targetRow, targetCol);
                        if (targetPiece != null) {
                            chessBoardLayout.getChildren().remove(targetPiece);
                        }

                        chessBoardLayout.add(draggedPiece, targetCol, targetRow);

                        // Update board state
                        boardState[startRow][startCol] = null;
                        boardState[targetRow][targetCol] = piece;
                        piece.updatePosition(targetRow, targetCol);
                        System.out.println("Updated game state: " + getGameState());
                       String roundNumberMessage =  (roundNumber % 2 == 0 ) ? "Blacks turn" :"Whites turn";
                       System.out.println(roundNumberMessage);
                        success = true;
                        roundNumber = roundNumber + 1;
                    }
                }
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        chessBoardLayout.setOnDragDone(event -> {
            if (!event.isDropCompleted()) {
                ImageView draggedPiece = (ImageView) event.getGestureSource();
                int startRow = GridPane.getRowIndex(draggedPiece);
                int startCol = GridPane.getColumnIndex(draggedPiece);
                chessBoardLayout.getChildren().remove(draggedPiece);
                chessBoardLayout.add(draggedPiece, startCol, startRow);
            }
            event.consume();
        });
    }

    private String getGameState() {
        StringBuilder gameState = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 8; col++) {
                Piece piece = boardState[row][col];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        gameState.append(emptyCount);
                        emptyCount = 0;
                    }
                    gameState.append(mapPieceToChar(piece.getType()));
                }
            }
            if (emptyCount > 0) {
                gameState.append(emptyCount);
            }
            if (row < 7) {
                gameState.append("/");
            }
        }
        return gameState.toString();
    }

    private char mapPieceToChar(String piece) {
        return switch (piece) {
            case "kw" -> 'K';
            case "qw" -> 'Q';
            case "rw" -> 'R';
            case "bw" -> 'B';
            case "nw" -> 'N';
            case "pw" -> 'P';
            case "kb" -> 'k';
            case "qb" -> 'q';
            case "rb" -> 'r';
            case "bb" -> 'b';
            case "nb" -> 'n';
            case "pb" -> 'p';
            default -> throw new IllegalArgumentException("Unknown piece: " + piece);
        };
    }
}