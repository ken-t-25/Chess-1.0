package model;

import org.json.JSONObject;

import java.util.ArrayList;

public class Knight extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a knight
    public Knight(String colour) {
        super(colour);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a knight that is on the game board
    public Knight(String colour, int x, int y) {
        super(colour, x, y);
    }

    // REQUIRES: colour must be one of "black" and "white", x and y must be in the range [1.8]
    // EFFECTS: constructs a knight with given information
    public Knight(String colour, int x, int y, boolean onBoard, boolean move) {
        super(colour, x, y, onBoard, move);
    }

    // EFFECTS: returns this knight as a json object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = super.toJson();
        jsonObject.put("type", "knight");
        return jsonObject;
    }

    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<>();
        moves.addAll(knightPositionTest(game, posX - 1, posY - 2));
        moves.addAll(knightPositionTest(game, posX - 2, posY - 1));
        moves.addAll(knightPositionTest(game, posX - 1, posY + 2));
        moves.addAll(knightPositionTest(game, posX - 2, posY + 1));
        moves.addAll(knightPositionTest(game, posX + 1, posY - 2));
        moves.addAll(knightPositionTest(game, posX + 2, posY - 1));
        moves.addAll(knightPositionTest(game, posX + 1, posY + 2));
        moves.addAll(knightPositionTest(game, posX + 2, posY + 1));
        return moves;
    }

    // EFFECTS: find possible moves of a knight by positions
    private ArrayList<Position> knightPositionTest(Game game, int x, int y) {
        ArrayList<Position> moves = new ArrayList<>();
        Position testPosition = new Position(x, y);
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            moves = positionTest(game, testPosition);
        }
        return moves;
    }

    // REQUIRES: posn must not be the same as the current position of this king
    // EFFECTS: returns a boolean that tells whether this knight can move to given position(enemy king's position)
    // in one step, ignoring whether the king on the same team will be checked
    @Override
    public boolean checkEnemy(Game game, Position posn) {
        int x = posn.getPosX();
        int y = posn.getPosY();
        int diffX = x - posX;
        int diffY = y - posY;
        return Math.abs(diffX) + Math.abs(diffY) == 3 && diffX != 0 && diffY != 0;
    }

    // EFFECTS: return chess type of this chess
    @Override
    public String getType() {
        return "knight";
    }
}
