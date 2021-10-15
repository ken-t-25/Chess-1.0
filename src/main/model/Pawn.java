package model;

import java.util.ArrayList;
import java.util.Objects;

public class Pawn extends ChessPiece {

    int direction;

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a pawn
    public Pawn(String colour) {
        super(colour);
        if (colour.equals("white")) {
            direction = -1;
        } else {
            direction = 1;
        }
    }



    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        if (!move) {
            moves.addAll(pawnPositionTest(game,posX,posY + (2 * direction)));
        }
        moves.addAll(pawnPositionTest(game,posX, posY + direction));
        moves.addAll(attackTest(game,posX - 1, posY + direction));
        moves.addAll(attackTest(game,posX + 1, posY + direction));
        return moves;
    }

    // EFFECTS: find possible moves of a pawn by positions (excluding attack moves)
    private ArrayList<Position> pawnPositionTest(Game game, int x, int y) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Position testPosition = new Position(x,y);
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            Board bd = game.getBoard();
            int posnIndex = testPosition.toSingleValue() - 1;
            Game testGame = new Game(game);
            if (Objects.isNull(bd.getOnBoard().get(posnIndex))) {
                testGame.move(this,x,y);
                if (!testGame.check(colour)) {
                    moves.add(testPosition);
                }
            }
        }
        return moves;
    }

    // EFFECTS: find possible attack moves of a pawn by positions
    private ArrayList<Position> attackTest(Game game, int x, int y) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Position testPosition = new Position(x,y);
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            Board bd = game.getBoard();
            int posnIndex = testPosition.toSingleValue() - 1;
            Game testGame = new Game(game);
            if (!Objects.isNull(bd.getOnBoard().get(posnIndex))) {
                if (!bd.getOnBoard().get(posnIndex).getColour().equals(colour)) {
                    ChessPiece enemyAttacked = bd.getOnBoard().get(posnIndex);
                    testGame.remove(enemyAttacked);
                    testGame.move(this,x,y);
                    if (!testGame.check(colour)) {
                        moves.add(testPosition);
                    }
                }
            }
        }
        return moves;
    }
}
