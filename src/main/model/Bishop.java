package model;

import java.util.ArrayList;

public class Bishop extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a bishop
    public Bishop(String colour) {
        super(colour);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a bishop that is on the game board
    public Bishop(String colour, int x, int y) {
        super(colour, x, y);
    }

    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<>();
        moves.addAll(lineTest(game,-1,-1));
        moves.addAll(lineTest(game,-1,1));
        moves.addAll(lineTest(game,1,-1));
        moves.addAll(lineTest(game,1,1));
        return moves;
    }

    // REQUIRES: posn must not be the same as the current position of this bishop
    // EFFECTS: returns a boolean that tells whether this bishop can move to given position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    @Override
    public boolean checkEnemy(Game game, Position posn) {
        Board bd = game.getBoard();
        int x = posn.getPosX();
        int y = posn.getPosY();
        int diffX = x - posX;
        int diffY = y - posY;
        if (Math.abs(diffX) == Math.abs(diffY) && diffX != 0) {
            int deltaX = Math.abs(diffX) / diffX;
            int deltaY = Math.abs(diffY) / diffY;
            int diff = Math.abs(diffX);
            return checkEnemyDiagonalPath(deltaX,deltaY,posX,posY,diff,bd);
        } else {
            return false;
        }
    }
}
