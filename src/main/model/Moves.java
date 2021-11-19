package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.nio.file.Watchable;
import java.util.ArrayList;

public class Moves implements Writable {

    ArrayList<Move> moves;

    // EFFECTS: constructs a moves object with given list of moves
    public Moves(ArrayList<Move> moves) {
        this.moves = moves;
    }

    // EFFECTS: return this moves as a json object
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Move m : moves) {
            jsonArray.put(m.toJson());
        }
        jsonObject.put("moves", jsonArray);
        return jsonObject;
    }

    // EFFECTS: constructs a moves object that represents the moves that took place in a hand
    public Moves() {
        moves = new ArrayList<>();
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
