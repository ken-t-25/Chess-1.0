package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Objects;

public class Board implements Writable {

    private ArrayList<ChessPiece> onBoard;

    // EFFECTS: constructs a chess board (8 times 8) with no chess pieces on it
    public Board() {
        onBoard = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                onBoard.add(null);
            }
        }
    }

    // EFFECTS: returns this board as a json object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("onBoard", chessListToJson(onBoard));
        return jsonObject;
    }

    // EFFECTS: returns given chess array as a json array
    public JSONArray chessListToJson(ArrayList<ChessPiece> chessArray) {
        JSONArray jsonArray = new JSONArray();
        for (ChessPiece cp : chessArray) {
            if (Objects.isNull(cp)) {
                ChessPiece nullChess = new King("");
                jsonArray.put(nullChess.toJson());
            } else {
                jsonArray.put(cp.toJson());
            }
        }
        return jsonArray;
    }

    // EFFECTS: constructs a chess board (8 times 8) with given onBoard list
    public Board(ArrayList<ChessPiece> onBoard) {
        this.onBoard = onBoard;
    }

    // EFFECTS: return onBoard
    public ArrayList<ChessPiece> getOnBoard() {
        return onBoard;
    }

    // REQUIRES: cp must be on the board,
    //           x and y must be within the range [1, 8] and must be different from the current x, y
    //           x and y must not be previously occupied
    // MODIFIES: this, cp
    // EFFECTS: change the position of cp on this board to (x, y)
    public void move(ChessPiece cp, int x, int y) {
        remove(cp);
        place(cp, x, y);
    }

    // REQUIRES: cp must be on the board
    // MODIFIES: this, cp
    // EFFECTS: remove cp from this board
    public void remove(ChessPiece cp) {
        int x = cp.getPosX();
        int y = cp.getPosY();
        Position posn = new Position(x, y);
        int index = posn.toSingleValue() - 1;
        onBoard.set(index, null);
    }

    // REQUIRES: cp must not be on the board, x and y must be within the range [1, 8]
    //           x and y must not be previously occupied
    // MODIFIES: this
    // EFFECTS: place cp on this board at given x, y position
    public void place(ChessPiece cp, int x, int y) {
        Position posn = new Position(x, y);
        int index = posn.toSingleValue() - 1;
        onBoard.set(index, cp);
    }
}
