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

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a pawn that is on the game board
    public Pawn(String colour, int x, int y) {
        super(colour, x, y);
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
            Board bd = game.getBoard();
            Position skip = new Position(posX,posY + direction);
            int skipIndex = skip.toSingleValue() - 1;
            if (Objects.isNull(bd.getOnBoard().get(skipIndex))) {
                moves.addAll(pawnPositionTest(game, posX, posY + (2 * direction)));
            }
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
            int initialX = this.posX;
            int initialY = this.posY;
            boolean initialMove = move;
            if (Objects.isNull(bd.getOnBoard().get(posnIndex))) {
                game.move(this,x,y);
                if (!game.check(colour)) {
                    moves.add(testPosition);
                }
                game.move(this,initialX,initialY);
                move = initialMove;
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
            int initialX = this.posX;
            int initialY = this.posY;
            boolean initialMove = move;
            if (!Objects.isNull(bd.getOnBoard().get(posnIndex))) {
                if (!bd.getOnBoard().get(posnIndex).getColour().equals(colour)) {
                    ChessPiece enemyAttacked = bd.getOnBoard().get(posnIndex);
                    int enemyX = enemyAttacked.getPosX();
                    int enemyY = enemyAttacked.getPosY();
                    game.remove(enemyAttacked);
                    game.move(this,x,y);
                    if (!game.check(colour)) {
                        moves.add(testPosition);
                    }
                    game.move(this,initialX,initialY);
                    game.placeFromOffBoard(enemyAttacked,enemyX,enemyY);
                    move = initialMove;
                }
            }
        }
        return moves;
    }

    // REQUIRES: posn must not be the same as the current position
    // EFFECTS: returns a boolean that tells whether this pawn can move to given position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    @Override
    public boolean checkEnemy(Game game, Position posn) {
        int x = posn.getPosX();
        int y = posn.getPosY();
        int diffX = x - posX;
        int diffY = y = posY;
        return diffY == direction && Math.abs(diffX) == 1;
    }
}
