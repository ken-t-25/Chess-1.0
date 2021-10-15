package model;

public class Move {

    private int beginX;
    private int beginY;
    private int endX;
    private int endY;
    private ChessPiece chess;

    // EFFECTS: constructs a move
    public Move(int beginX, int beginY, int endX, int endY, ChessPiece chess) {
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.chess = chess;
    }

    // EFFECTS: returns the beginning x position of the move
    public int getBeginX() {
        return beginX;
    }

    // EFFECTS: returns the beginning y position of the move
    public int getBeginY() {
        return beginY;
    }

    // EFFECTS: returns the ending x position of the move
    public int getEndX() {
        return endX;
    }

    // EFFECTS: returns the ending y position of the move
    public int getEndY() {
        return endY;
    }

    // EFFECTS: returns the chess that is being moved
    public ChessPiece getChessPiece() {
        return chess;
    }
}
