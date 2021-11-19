package model;

import org.json.JSONObject;

import java.util.ArrayList;

public class Queen extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a queen
    public Queen(String colour) {
        super(colour);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a queen that is on the game board
    public Queen(String colour, int x, int y) {
        super(colour, x, y);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a queen with given information
    public Queen(String colour, int x, int y, boolean onBoard, boolean move) {
        super(colour, x, y, onBoard, move);
    }

    // EFFECTS: returns this queen as a json object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("type", "queen");
        return jsonObject;
    }

    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<>();
        moves.addAll(lineTest(game, -1, -1));
        moves.addAll(lineTest(game, -1, 1));
        moves.addAll(lineTest(game, 1, -1));
        moves.addAll(lineTest(game, 1, 1));
        moves.addAll(lineTest(game, 0, -1));
        moves.addAll(lineTest(game, 0, 1));
        moves.addAll(lineTest(game, -1, 0));
        moves.addAll(lineTest(game, 1, 0));
        return moves;
    }

    // REQUIRES: posn must not be the same as the current position of this bishop
    // EFFECTS: returns a boolean that tells whether this queen can move to given position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    @Override
    public boolean checkEnemy(Game game, Position posn) {
        Board bd = game.getBoard();
        int x = posn.getPosX();
        int y = posn.getPosY();
        int diffX = x - posX;
        int diffY = y - posY;
        if (Math.abs(diffX) == Math.abs(diffY) && diffX != 0) {
            int diff = Math.abs(diffX);
            int deltaX = Math.abs(diffX) / diffX;
            int deltaY = Math.abs(diffY) / diffY;
            return checkEnemyDiagonalPath(deltaX, deltaY, posX, posY, diff, bd);
        } else if (x == posX) {
            return checkEnemyStraightPath(posX, posY, y, "y", bd);
        } else if (y == posY) {
            return checkEnemyStraightPath(posY, posX, x, "x", bd);
        } else {
            return false;
        }
    }
}
