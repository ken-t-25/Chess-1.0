package model;

import java.util.ArrayList;
import java.util.Objects;

public class Rook extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a rook
    public Rook(String colour) {
        super(colour);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a rook that is on the game board
    public Rook(String colour, int x, int y) {
        super(colour, x, y);
    }


    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        moves.addAll(lineTest(game,0,-1));
        moves.addAll(lineTest(game,0,1));
        moves.addAll(lineTest(game,-1,0));
        moves.addAll(lineTest(game,1,0));
        return moves;
    }

    // REQUIRES: posn must not be the same as the current position of this rook
    // EFFECTS: returns a boolean that tells whether this rook can move to given position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    @Override
    public boolean checkEnemy(Game game, Position posn) {
        Board bd = game.getBoard();
        int x = posn.getPosX();
        int y = posn.getPosY();
        if (x == posX) {
            return checkEnemyStraightPath(posX,posY,y,"y",bd);
        } else if (y == posY) {
            return checkEnemyStraightPath(posY,posX,x,"x",bd);
        } else {
            return false;
        }
    }
}
