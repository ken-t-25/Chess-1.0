package model;

import java.util.ArrayList;
import java.util.Objects;

// An abstract class for the chess pieces
public abstract class ChessPiece {

    protected int posX;
    protected int posY;
    protected boolean onBoard;
    protected String colour;
    protected boolean move;

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: an abstract constructor for chess piece
    protected ChessPiece(String colour) {
        posX = 0;
        posY = 0;
        this.colour = colour;
        onBoard = false;
        move = false;
    }

    // EFFECTS: return the possible next moves that this chess piece can take on given board
    protected abstract ArrayList<Position> possibleMoves(Game game);


    // EFFECTS: return move, which tells whether this chess piece has been moved during the game
    protected boolean hasMoved() {
        return move;
    }


    // EFFECTS: return the colour of this chess piece
    protected String getColour() {
        return colour;
    }

    // EFFECTS: return a boolean telling whether this chess piece is on board
    protected Boolean onBoard() {
        return onBoard;
    }

    // EFFECTS: return the chess piece's x position (horizontal) on the chess board
    protected int getPosX() {
        return posX;
    }

    // EFFECTS: return the chess piece's y position (vertical) on the chess board
    protected int getPosY() {
        return posY;
    }

    // REQUIRES: x must in the interval [1,8]
    // MODIFIES: this
    // EFFECTS: change the x position of this chess piece to the given value
    protected void setPosX(int x) {
        posX = x;
    }

    // REQUIRES: y must in the interval [1,8]
    // MODIFIES: this
    // EFFECTS: change the y position of this chess piece to the given value
    protected void setPosY(int y) {
        posY = y;
    }

    // MODIFIES: this
    // EFFECTS: set assign onBoard with the given boolean value
    protected void setOnBoard(Boolean b) {
        onBoard = b;
    }

    // MODIFIES: this
    // EFFECTS: set assign move with the given boolean value
    protected void setMove(Boolean m) {
        move = m;
    }

    // REQUIRES: x and y of posn must be with in the range [1, 8]
    // EFFECTS: if given position can be a next move of this chess, return a list containing only this position
    //          otherwise, return an empty list
    protected ArrayList<Position> positionTest(Game game, Position posn) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Board bd = game.getBoard();
        int posnIndex = posn.toSingleValue() - 1;
        int initialX = this.posX;
        int initialY = this.posY;
        boolean initialMove = move;
        if (Objects.isNull(bd.getOnBoard().get(posnIndex))) {
            game.move(this,posn.getPosX(),posn.getPosY());
            if (!game.check(colour)) {
                moves.add(posn);
            }
            game.move(this,initialX,initialY);
            move = initialMove;
        } else {
            if (!bd.getOnBoard().get(posnIndex).getColour().equals(colour)) {
                ChessPiece enemyAttacked = bd.getOnBoard().get(posnIndex);
                int enemyInitialX = enemyAttacked.getPosX();
                int enemyInitialY = enemyAttacked.getPosY();
                game.remove(enemyAttacked);
                game.move(this,posn.getPosX(),posn.getPosY());
                if (!game.check(colour)) {
                    moves.add(posn);
                }
                game.move(this,initialX,initialY);
                game.place(enemyAttacked,enemyInitialX,enemyInitialY);
                move = initialMove;
            }
        }
        return moves;
    }

    // REQUIRES: this chess must be one of queen, rook, and bishop
    // EFFECTS: find possible moves of this chess by lines
    protected ArrayList<Position> lineTest(Game game, int deltaX, int deltaY) {
        ArrayList<Position> moves = new ArrayList<Position>();
        int x = posX + deltaX;
        int y = posY + deltaY;
        boolean blocked = false;
        Board bd = game.getBoard();
        while (x >= 1 && x <= 8 && y >= 1 && y <= 8 && !blocked) {
            Position testPosn = new Position(x, y);
            ArrayList<Position> testedMove = positionTest(game,testPosn);
            moves.addAll(testedMove);
            if (!Objects.isNull(bd.getOnBoard().get(testPosn.toSingleValue() - 1))) {
                blocked = true;
            }
            x = x + deltaX;
            y = y + deltaY;
        }
        return moves;
    }

}
