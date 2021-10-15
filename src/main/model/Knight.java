package model;

import java.util.ArrayList;

public class Knight extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a knight
    public Knight(String colour) {
        super(colour);
    }



    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        moves.addAll(knightPositionTest(game,posX - 1, posY - 2));
        moves.addAll(knightPositionTest(game,posX - 2, posY - 1));
        moves.addAll(knightPositionTest(game,posX - 1, posY + 2));
        moves.addAll(knightPositionTest(game,posX - 2, posY + 1));
        moves.addAll(knightPositionTest(game,posX + 1, posY - 2));
        moves.addAll(knightPositionTest(game,posX + 2, posY - 1));
        moves.addAll(knightPositionTest(game,posX + 1, posY + 2));
        moves.addAll(knightPositionTest(game,posX + 2, posY + 1));
        return moves;
    }

    // EFFECTS: find possible moves of a knight by positions
    private ArrayList<Position> knightPositionTest(Game game, int x, int y) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Position testPosition = new Position(x,y);
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            moves = positionTest(game,testPosition);
        }
        return moves;
    }
}
