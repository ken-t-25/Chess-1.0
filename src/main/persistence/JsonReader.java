package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.json.*;
import ui.PlayGame;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads game from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Game readGame() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses Game from JSON object and returns it
    private Game parseGame(JSONObject jsonObject) {
        ArrayList<ChessPiece> wcOnBoard = parseChessList(jsonObject.getJSONArray("wcOnBoard"));
        ArrayList<ChessPiece> bcOnBoard = parseChessList(jsonObject.getJSONArray("bcOnBoard"));
        ArrayList<ChessPiece> wcOffBoard = parseChessList(jsonObject.getJSONArray("wcOffBoard"));
        ArrayList<ChessPiece> bcOffBoard = parseChessList(jsonObject.getJSONArray("bcOffBoard"));
        ArrayList<ChessPiece> allChess = new ArrayList<>();
        allChess.addAll(wcOnBoard);
        allChess.addAll(bcOnBoard);
        Board gameBoard = parseGameBoard(jsonObject.getJSONObject("gameBoard"), wcOnBoard, bcOnBoard, allChess);
        allChess.addAll(wcOffBoard);
        allChess.addAll(bcOffBoard);
        ArrayList<Moves> history = parseHistory(jsonObject.getJSONArray("history"), wcOnBoard, wcOffBoard,
                bcOnBoard, bcOffBoard, allChess);
        String turn = jsonObject.getString("turn");
        boolean drawn = jsonObject.getBoolean("drawn");
        return new Game(wcOnBoard, wcOffBoard, bcOnBoard, bcOffBoard, gameBoard, history, turn, drawn);
    }

    // EFFECTS: parses a list of chess pieces from JSON array and returns it
    private ArrayList<ChessPiece> parseChessList(JSONArray jsonArray) {
        ArrayList<ChessPiece> cp = new ArrayList<>();
        for (Object jo : jsonArray) {
            JSONObject json = (JSONObject) jo;
            ChessPiece nextCP = parseChessPiece(json);
            cp.add(nextCP);
        }
        return cp;
    }

    // MODIFIES: allChessOnBoard
    // EFFECTS: parses Board from JSON object and returns it
    private Board parseGameBoard(JSONObject jsonObject, ArrayList<ChessPiece> wc, ArrayList<ChessPiece> bc,
                                 ArrayList<ChessPiece> allChessOnBoard) {
        ArrayList<ChessPiece> onBoard = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("onBoard");
        for (Object jo : jsonArray) {
            JSONObject json = (JSONObject) jo;
            ChessPiece nextCP = parseChessPiece(json);
            if (nextCP == null) {
                onBoard.add(null);
            } else {
                for (ChessPiece cp : allChessOnBoard) {
                    if (cp.equals(nextCP)) {
                        onBoard.add(cp);
                        allChessOnBoard.remove(cp);
                        break;
                    }
                }
            }
        }
        return new Board(onBoard);
    }

    // MODIFIES: allChess
    // EFFECTS: parses a list of moves from JSON array and returns it
    private ArrayList<Moves> parseHistory(JSONArray jsonArray, ArrayList<ChessPiece> wcOn, ArrayList<ChessPiece> wcOff,
                                          ArrayList<ChessPiece> bcOn, ArrayList<ChessPiece> bcOff,
                                          ArrayList<ChessPiece> allChess) {
        ArrayList<Moves> history = new ArrayList<>();
        for (Object jo : jsonArray) {
            JSONObject json = (JSONObject) jo;
            Moves moves = parseMoves(json, wcOn, wcOff, bcOn, bcOff, allChess);
            history.add(moves);
        }
        return history;
    }

    // MODIFIES: allChess
    // EFFECTS: parses Moves from JSON object and returns it
    private Moves parseMoves(JSONObject jsonObject, ArrayList<ChessPiece> wcOn, ArrayList<ChessPiece> wcOff,
                             ArrayList<ChessPiece> bcOn, ArrayList<ChessPiece> bcOff, ArrayList<ChessPiece> allChess) {
        ArrayList<Move> moves = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("moves");
        for (Object jo : jsonArray) {
            JSONObject json = (JSONObject) jo;
            Move move = parseMove(json, wcOn, wcOff, bcOn, bcOff, allChess);
            moves.add(move);
        }
        return new Moves(moves);
    }

    // MODIFIES: allChess
    // EFFECTS: parses Move from JSON object and returns it
    private Move parseMove(JSONObject jsonObject, ArrayList<ChessPiece> wcOn, ArrayList<ChessPiece> wcOff,
                           ArrayList<ChessPiece> bcOn, ArrayList<ChessPiece> bcOff, ArrayList<ChessPiece> allChess) {
        int bx = jsonObject.getInt("beginX");
        int by = jsonObject.getInt("beginY");
        int ex = jsonObject.getInt("endX");
        int ey = jsonObject.getInt("endY");
        ChessPiece chess = parseChessPiece(jsonObject.getJSONObject("chess"));
        for (ChessPiece cp: allChess) {
            if (cp.equals(chess)) {
                chess = cp;
                allChess.remove(cp);
                break;
            }
        }
        boolean moveStatus = jsonObject.getBoolean("moveStatus");
        return new Move(bx, by, ex, ey, chess, moveStatus);
    }

    // EFFECTS: parses ChessPiece from JSON object and returns it
    private ChessPiece parseChessPiece(JSONObject jsonObject) {
        if (jsonObject.getString("colour").equals((""))) {
            return null;
        }
        int x = jsonObject.getInt("posX");
        int y = jsonObject.getInt("posY");
        boolean onBoard = jsonObject.getBoolean("onBoard");
        String colour = jsonObject.getString("colour");
        boolean move = jsonObject.getBoolean("move");
        String type = jsonObject.getString("type");
        if (type.equals("pawn")) {
            return new Pawn(colour, x, y, onBoard, move);
        } else if (type.equals("rook")) {
            return new Rook(colour, x, y, onBoard, move);
        } else if (type.equals("knight")) {
            return new Knight(colour, x, y, onBoard, move);
        } else if (type.equals("bishop")) {
            return new Bishop(colour, x, y, onBoard, move);
        } else if (type.equals("queen")) {
            return new Queen(colour, x, y, onBoard, move);
        } else {
            return new King(colour, x, y, onBoard, move);
        }
    }
}
