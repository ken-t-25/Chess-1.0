package model;

import java.util.ArrayList;
import java.util.Objects;

public class Rook extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a rook
    public Rook(String colour) {
        super(colour);
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



}
