package model;

import org.json.JSONObject;
import persistence.Writable;

public class Move implements Writable {

    private final int beginX;
    private final int beginY;
    private final int endX;
    private final int endY;
    private final ChessPiece chess;
    private final boolean moveStatus;

    // EFFECTS: constructs a move
    public Move(int beginX, int beginY, int endX, int endY, ChessPiece chess, boolean moveStatus) {
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.chess = chess;
        this.moveStatus = moveStatus;
    }

    // EFFECTS: returns this move to a json object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("beginX", beginX);
        jsonObject.put("beginY", beginY);
        jsonObject.put("endX", endX);
        jsonObject.put("endY", endY);
        jsonObject.put("chess", chess.toJson());
        jsonObject.put("moveStatus", moveStatus);
        return jsonObject;
    }

    // EFFECTS: returns the beginning x position of the move
    public int getBeginX() {
        return beginX;
    }

    // EFFECTS: returns the beginning y position of the move
    public int getBeginY() {
        return beginY;
    }

    // EFFECTS: returns the ending x position of the move
    public int getEndX() {
        return endX;
    }

    // EFFECTS: returns the ending y position of the move
    public int getEndY() {
        return endY;
    }

    // EFFECTS: returns the chess that is being moved
    public ChessPiece getChessPiece() {
        return chess;
    }

    // EFFECTS: returns the move status of the moved chess before this move
    public boolean getMoveStatus() {
        return moveStatus;
    }
}
