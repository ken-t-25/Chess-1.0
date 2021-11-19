package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Objects;

// An abstract class for the chess pieces
public abstract class ChessPiece implements Writable {

    protected int posX;
    protected int posY;
    protected boolean onBoard;
    protected String colour;
    protected boolean move;

    // REQUIRES: colour must be one of "black", "white", or an empty string (stands for a null chess);
    // EFFECTS: an abstract constructor for chess piece
    protected ChessPiece(String colour) {
        posX = 0;
        posY = 0;
        this.colour = colour;
        onBoard = false;
        move = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return posX == that.posX && posY == that.posY && onBoard == that.onBoard && move == that.move
                && Objects.equals(colour, that.colour);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: an abstract constructor for chess piece that is on the game board
    protected ChessPiece(String colour, int x, int y) {
        posX = x;
        posY = y;
        this.colour = colour;
        onBoard = true;
        move = false;
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: an abstract constructor for chess piece with given information
    protected ChessPiece(String colour, int x, int y, boolean onBoard, boolean move) {
        posX = x;
        posY = y;
        this.colour = colour;
        this.onBoard = onBoard;
        this.move = move;
    }

    // EFFECTS: returns this chess piece as an array
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("posX", posX);
        json.put("posY", posY);
        json.put("onBoard", onBoard);
        json.put("colour", colour);
        json.put("move", move);
        return json;
    }


    // EFFECTS: return the possible next moves that this chess piece can take on given board
    public abstract ArrayList<Position> possibleMoves(Game game);

    // REQUIRES: posn must not be the same as the current position of this chess piece
    // EFFECTS: returns a boolean that tells whether this chess piece can move to this position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    public abstract boolean checkEnemy(Game game, Position posn);

    // EFFECTS: return move, which tells whether this chess piece has been moved during the game
    public boolean hasMoved() {
        return move;
    }

    // EFFECTS: return the colour of this chess piece
    public String getColour() {
        return colour;
    }

    // EFFECTS: return a boolean telling whether this chess piece is on board
    public Boolean getOnBoard() {
        return onBoard;
    }

    // EFFECTS: return the chess piece's x position (horizontal) on the chess board
    public int getPosX() {
        return posX;
    }

    // EFFECTS: return the chess piece's y position (vertical) on the chess board
    public int getPosY() {
        return posY;
    }

    // REQUIRES: x must in the interval [1,8]
    // MODIFIES: this
    // EFFECTS: change the x position of this chess piece to the given value
    public void setPosX(int x) {
        posX = x;
    }

    // REQUIRES: y must in the interval [1,8]
    // MODIFIES: this
    // EFFECTS: change the y position of this chess piece to the given value
    public void setPosY(int y) {
        posY = y;
    }

    // MODIFIES: this
    // EFFECTS: set assign onBoard with the given boolean value
    public void setOnBoard(Boolean b) {
        onBoard = b;
    }

    // MODIFIES: this
    // EFFECTS: set assign move with the given boolean value
    public void setMove(Boolean m) {
        move = m;
    }

    // REQUIRES: x and y of posn must be with in the range [1, 8]
    // EFFECTS: if given position can be a next move of this chess, return a list containing only this position
    //          otherwise, return an empty list
    protected ArrayList<Position> positionTest(Game game, Position posn) {
        ArrayList<Position> moves = new ArrayList<>();
        Board bd = game.getBoard();
        int posnIndex = posn.toSingleValue() - 1;
        int initialX = this.posX;
        int initialY = this.posY;
        boolean initialMove = move;
        if (Objects.isNull(bd.getOnBoard().get(posnIndex))) {
            positionEmpty(game, posn, moves, initialX, initialY, initialMove);
        } else {
            if (!bd.getOnBoard().get(posnIndex).getColour().equals(colour)) {
                hasEnemy(game, posn, moves, initialX, initialY, initialMove, bd, posnIndex);
            }
        }
        return moves;
    }

    // MODIFIES: moves
    // EFFECTS: do position test on a position that is empty
    //          ix = initialX, iy = initialY, im = initialMove
    protected void positionEmpty(Game game, Position posn, ArrayList<Position> moves, int ix, int iy, boolean im) {
        game.move(this, posn.getPosX(), posn.getPosY());
        if (!game.check(colour)) {
            moves.add(posn);
        }
        game.move(this, ix, iy);
        this.move = im;
    }

    // MODIFIES: ms (moves)
    // EFFECTS: do position test on a position that is occupied by an enemy
    //          g = game, p = position, ms = moves, tp = testPosition, ix = initialX, iy = initialY,
    //          im = initialMove, b = board, pi = positionIndex
    protected void hasEnemy(Game g, Position p, ArrayList<Position> ms, int ix, int iy, boolean im, Board b, int pi) {
        ChessPiece enemyAttacked = b.getOnBoard().get(pi);
        int enemyInitialX = enemyAttacked.getPosX();
        int enemyInitialY = enemyAttacked.getPosY();
        g.remove(enemyAttacked);
        g.move(this, p.getPosX(), p.getPosY());
        if (!g.check(colour)) {
            ms.add(p);
        }
        g.move(this, ix, iy);
        g.placeFromOffBoard(enemyAttacked, enemyInitialX, enemyInitialY);
        move = im;
    }

    // REQUIRES: this chess must be one of queen, rook, and bishop
    // EFFECTS: find possible moves of this chess by lines
    protected ArrayList<Position> lineTest(Game game, int deltaX, int deltaY) {
        ArrayList<Position> moves = new ArrayList<>();
        int x = posX + deltaX;
        int y = posY + deltaY;
        boolean blocked = false;
        Board bd = game.getBoard();
        while (x >= 1 && x <= 8 && y >= 1 && y <= 8 && !blocked) {
            Position testPosn = new Position(x, y);
            ArrayList<Position> testedMove = positionTest(game, testPosn);
            moves.addAll(testedMove);
            if (!Objects.isNull(bd.getOnBoard().get(testPosn.toSingleValue() - 1))) {
                blocked = true;
            }
            x = x + deltaX;
            y = y + deltaY;
        }
        return moves;
    }

    // EFFECTS: returns true if there are no blocks on the given diagonal path
    protected boolean checkEnemyDiagonalPath(int deltaX, int deltaY, int x, int y, int diff, Board board) {
        boolean notBlock = true;
        for (int i = 1; i < diff; i++) {
            int testX = x + (deltaX * i);
            int testY = y + (deltaY * i);
            Position testPosn = new Position(testX, testY);
            int testIndex = testPosn.toSingleValue() - 1;
            if (!Objects.isNull(board.getOnBoard().get(testIndex))) {
                notBlock = false;
            }
        }
        return notBlock;
    }

    // EFFECTS: returns true if there are no blocks on the given horizontal or vertical path
    protected boolean checkEnemyStraightPath(int constance, int change, int changeTo, String direction, Board board) {
        boolean notBlock = true;
        int diff = changeTo - change;
        int absDiff = Math.abs(diff);
        int delta = diff / absDiff;
        for (int i = 1; i < absDiff; i++) {
            Position testPosn;
            if (direction.equals("x")) {
                testPosn = new Position(change + (delta * i), constance);
            } else {
                testPosn = new Position(constance, change + (delta * i));
            }
            int testIndex = testPosn.toSingleValue() - 1;
            if (!Objects.isNull(board.getOnBoard().get(testIndex))) {
                notBlock = false;
            }
        }
        return notBlock;
    }
}
