package model;

import java.util.ArrayList;

public class Position {

    private final int posX;
    private final int posY;

    // REQUIRES: x and y must be in the range [1, 8]
    // EFFECTS: constructs a position on a chess board
    public Position(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    // EFFECTS: returns posX of this position
    public int getPosX() {
        return posX;
    }

    // EFFECTS: returns posY of this position
    public int getPosY() {
        return posY;
    }

    // EFFECTS: returns a boolean representing whether this position is attacked by the given list of chess pieces
    public Boolean attacked(ArrayList<ChessPiece> chessPieces, Game game) {
        boolean attacked = false;
        for (ChessPiece cp: chessPieces) {
            if (cp.checkEnemy(game,this)) {
                attacked = true;
            }
        }
        return attacked;
    }

    // EFFECTS: returns the single value representation of this position (8(y-1)+x)
    public int toSingleValue() {
        return 8 * (posY - 1) + posX;
    }

    // EFFECTS: returns a true if this position has the same x,y coordinates as the given position
    public boolean positionEquals(Position posn) {
        return posn.getPosX() == posX && posn.getPosY() == posY;
    }
}
