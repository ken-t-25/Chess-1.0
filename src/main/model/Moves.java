package model;


import java.util.ArrayList;

public class Moves {

    ArrayList<Move> moves;

    // EFFECTS: constructs a moves object that represents the moves that took place in a hand
    public Moves() {
        moves = new ArrayList<Move>();
    }

    // MODIFIES: this
    // EFFECTS: add given move to this moves
    public void addMove(Move m) {
        moves.add(m);
    }

    // EFFECTS: this turn this moves as an array list of moves
    public ArrayList<Move> getMoves() {
        return moves;
    }
}
