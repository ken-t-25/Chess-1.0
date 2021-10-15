package model;

import java.util.ArrayList;
import java.util.Objects;

public class Bishop extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a bishop
    public Bishop(String colour) {
        super(colour);
    }



    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        ArrayList<Position> topLeftDiagonal = lineTest(game,-1,-1);
        ArrayList<Position> bottomLeftDiagonal = lineTest(game,-1,1);
        ArrayList<Position> topRightDiagonal = lineTest(game,1,-1);
        ArrayList<Position> bottomRightDiagonal = lineTest(game,1,1);
        append(moves, topLeftDiagonal);
        append(moves, bottomLeftDiagonal);
        append(moves, topRightDiagonal);
        append(moves, bottomRightDiagonal);
        return moves;
    }

    protected void append(ArrayList<Position> allMoves, ArrayList<Position> moves) {
        for (Position posn: moves) {
            allMoves.add(posn);
        }
    }
}
