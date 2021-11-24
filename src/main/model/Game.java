package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

public class Game implements Writable {

    private ArrayList<ChessPiece> whiteChessPiecesOnBoard;
    private ArrayList<ChessPiece> blackChessPiecesOnBoard;
    private ArrayList<ChessPiece> whiteChessPiecesOffBoard;
    private ArrayList<ChessPiece> blackChessPiecesOffBoard;
    private Board gameBoard;
    private ArrayList<Moves> history;
    private String turn;
    private boolean drawn;

    // EFFECTS: constructs and sets up a chess game
    public Game() {
        whiteChessPiecesOnBoard = buildDefaultChessOnBoard("white");
        blackChessPiecesOnBoard = buildDefaultChessOnBoard("black");
        whiteChessPiecesOffBoard = buildDefaultChessOffBoard("white", 8);
        blackChessPiecesOffBoard = buildDefaultChessOffBoard("black", 8);
        gameBoard = new Board();
        updateBoard(whiteChessPiecesOnBoard);
        updateBoard(blackChessPiecesOnBoard);
        history = new ArrayList<>();
        turn = "white";
        drawn = false;
        EventLog.getInstance().logEvent(new Event("Open a regular game."));
    }

    // REQUIRES: t must be "black" or "white"
    // EFFECTS: constructs and sets up a chess game using given chess lists, game board, and history
    public Game(ArrayList<ChessPiece> onW, ArrayList<ChessPiece> offW, ArrayList<ChessPiece> onB,
                ArrayList<ChessPiece> offB, Board gb, ArrayList<Moves> his, String t, boolean d) {
        whiteChessPiecesOnBoard = onW;
        blackChessPiecesOnBoard = onB;
        whiteChessPiecesOffBoard = offW;
        blackChessPiecesOffBoard = offB;
        gameBoard = gb;
        history = his;
        turn = t;
        drawn = d;
        EventLog.getInstance().logEvent(new Event("Open a modified game."));
    }


    // EFFECTS: returns this PlayGame as a json object
    @Override
    public JSONObject toJson() {
        EventLog.getInstance().logEvent(new Event("Save a Game."));
        JSONObject json = new JSONObject();
        json.put("wcOnBoard", gameBoard.chessListToJson(whiteChessPiecesOnBoard));
        json.put("bcOnBoard", gameBoard.chessListToJson(blackChessPiecesOnBoard));
        json.put("wcOffBoard", gameBoard.chessListToJson(whiteChessPiecesOffBoard));
        json.put("bcOffBoard", gameBoard.chessListToJson(blackChessPiecesOffBoard));
        json.put("gameBoard", gameBoard.toJson());
        json.put("history", movesListToJson(history));
        json.put("turn", turn);
        json.put("drawn", drawn);
        return json;
    }

    // EFFECTS: returns given chess array as a json array
    public JSONArray movesListToJson(ArrayList<Moves> movesArray) {
        JSONArray jsonArray = new JSONArray();
        for (Moves ms : movesArray) {
            jsonArray.put(ms.toJson());
        }
        return jsonArray;
    }

    // REQUIRES: colour must be "black" or "white"
    // EFFECTS: constructs the default chess pieces on board
    private ArrayList<ChessPiece> buildDefaultChessOnBoard(String colour) {
        ArrayList<ChessPiece> cp = new ArrayList<>();
        int firstRow;
        int secondRow;
        if (colour.equals("white")) {
            firstRow = 8;
            secondRow = 7;
        } else {
            firstRow = 1;
            secondRow = 2;
        }
        cp.add(new Rook(colour, 1, firstRow));
        cp.add(new Knight(colour, 2, firstRow));
        cp.add(new Bishop(colour, 3, firstRow));
        cp.add(new Queen(colour, 4, firstRow));
        cp.add(new King(colour, 5, firstRow));
        cp.add(new Bishop(colour, 6, firstRow));
        cp.add(new Knight(colour, 7, firstRow));
        cp.add(new Rook(colour, 8, firstRow));
        for (int i = 1; i <= 8; i++) {
            cp.add(new Pawn(colour, i, secondRow));
        }
        return cp;
    }

    // REQUIRES: colour must be "black" or "white"
    // EFFECTS: constructs the default chess pieces off board
    public ArrayList<ChessPiece> buildDefaultChessOffBoard(String colour, int pawnNum) {
        ArrayList<ChessPiece> cp = new ArrayList<>();
        for (int i = 0; i < pawnNum; i++) {
            cp.add(new Queen(colour));
            cp.add(new Bishop(colour));
            cp.add(new Knight(colour));
            cp.add(new Rook(colour));
        }
        return cp;
    }

    // EFFECTS: update the chess info in given chess list to the board in this game
    private void updateBoard(ArrayList<ChessPiece> cpl) {
        for (ChessPiece cp : cpl) {
            gameBoard.place(cp, cp.getPosX(), cp.getPosY());
        }
    }

    // MODIFIES: this
    // EFFECTS: replaces current gameBoard with given board
    public void setGameBoard(Board bd) {
        gameBoard = bd;
    }

    // MODIFIES: this
    // EFFECTS: replaces current gameBoard with given board
    public void setHistory(ArrayList<Moves> movesList) {
        history = movesList;
    }

    // MODIFIES: this
    // EFFECTS: replaces current whiteChessPieceOnBoard list with given list
    public void setWhiteChessPiecesOnBoard(ArrayList<ChessPiece> cp) {
        whiteChessPiecesOnBoard = cp;
    }

    // MODIFIES: this
    // EFFECTS: replaces current blackChessPieceOnBoard list with given list
    public void setBlackChessPiecesOnBoard(ArrayList<ChessPiece> cp) {
        blackChessPiecesOnBoard = cp;
    }

    // MODIFIES: this
    // EFFECTS: replaces current whiteChessPieceOffBoard list with given list
    public void setWhiteChessPiecesOffBoard(ArrayList<ChessPiece> cp) {
        whiteChessPiecesOffBoard = cp;
    }

    // MODIFIES: this
    // EFFECTS: replaces current blackChessPieceOffBoard list with given list
    public void setBlackChessPiecesOffBoard(ArrayList<ChessPiece> cp) {
        blackChessPiecesOffBoard = cp;
    }

    // EFFECTS: returns history of moves of this game
    public ArrayList<Moves> getHistory() {
        return history;
    }

    // EFFECTS: returns the game board of this game
    public Board getBoard() {
        return gameBoard;
    }

    // EFFECTS: returns the list of white chess pieces on board in this game
    public ArrayList<ChessPiece> getWhiteChessPiecesOnBoard() {
        return whiteChessPiecesOnBoard;
    }

    // EFFECTS: returns the list of black chess pieces on board in this game
    public ArrayList<ChessPiece> getBlackChessPiecesOnBoard() {
        return blackChessPiecesOnBoard;
    }

    // EFFECTS: returns the list of white chess pieces off board in this game
    public ArrayList<ChessPiece> getWhiteChessPiecesOffBoard() {
        return whiteChessPiecesOffBoard;
    }

    // EFFECTS: returns the list of black chess pieces off board in this game
    public ArrayList<ChessPiece> getBlackChessPiecesOffBoard() {
        return whiteChessPiecesOffBoard;
    }

    // REQUIRES: side must be one of "black" and "white"
    // EFFECTS: tells whether a stalemate has occurred on the given side (i.e. no more legal moves)
    public boolean stalemate(String side) {
        ArrayList<ChessPiece> examinedList;
        if (side.equals("white")) {
            examinedList = whiteChessPiecesOnBoard;
        } else {
            examinedList = blackChessPiecesOnBoard;
        }
        return !haveLegalMove(examinedList) && !check(side);
    }

    // EFFECTS: tells whether given team has legal move
    private boolean haveLegalMove(ArrayList<ChessPiece> chessList) {
        ArrayList<ChessPiece> copyList = new ArrayList<>(chessList);
        for (ChessPiece cp : copyList) {
            if (!cp.possibleMoves(this).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: side must be one of "black" and "white"
    // EFFECTS: tells whether the given side's king is checked
    public boolean check(String side) {
        ArrayList<ChessPiece> examinedList;
        ArrayList<ChessPiece> enemySide;
        boolean check;
        if (side.equals("white")) {
            examinedList = whiteChessPiecesOnBoard;
            enemySide = blackChessPiecesOnBoard;
        } else {
            examinedList = blackChessPiecesOnBoard;
            enemySide = whiteChessPiecesOnBoard;
        }
        ChessPiece examinedKing = findKing(examinedList);
        Position kingPosn = new Position(examinedKing.getPosX(), examinedKing.getPosY());
        check = kingPosn.attacked(enemySide, this);
        return check;
    }

    // EFFECTS: find king within given chessList
    private ChessPiece findKing(ArrayList<ChessPiece> chessList) {
        ChessPiece king = null;
        for (ChessPiece cp : chessList) {
            if (cp instanceof King) {
                king = cp;
            }
        }
        return king;
    }

    // REQUIRES: cp must be on the board,
    //           x and y must be within the range [1, 8] and must be different from the current x, y
    //           x and y must not be occupied
    // MODIFIES: this, cp
    // EFFECTS: change the position of cp in this game to (x, y)
    public void move(ChessPiece cp, int x, int y) {
        gameBoard.move(cp, x, y);
        String colour = cp.getColour();
        ArrayList<ChessPiece> listModified;
        if (colour.equals("white")) {
            listModified = whiteChessPiecesOnBoard;
        } else {
            listModified = blackChessPiecesOnBoard;
        }
        listModified.remove(cp);
        cp.setPosX(x);
        cp.setPosY(y);
        if (!cp.hasMoved()) {
            cp.setMove(true);
        }
        listModified.add(cp);
    }

    // REQUIRES: cp must be on the board
    // MODIFIES: this, cp
    // EFFECTS: remove cp from this game
    public void remove(ChessPiece cp) {
        gameBoard.remove(cp);
        String colour = cp.getColour();
        ArrayList<ChessPiece> listRemoveFrom;
        ArrayList<ChessPiece> listAddTo;
        if (colour.equals("white")) {
            listRemoveFrom = whiteChessPiecesOnBoard;
            listAddTo = whiteChessPiecesOffBoard;
        } else {
            listRemoveFrom = blackChessPiecesOnBoard;
            listAddTo = blackChessPiecesOffBoard;
        }
        listRemoveFrom.remove(cp);
        cp.setPosX(0);
        cp.setPosY(0);
        cp.setOnBoard(false);
        listAddTo.add(cp);
    }

    // REQUIRES: cp must be in off board list, x and y must be within the range [1, 8]
    //           x and y must not be occupied
    // MODIFIES: this
    // EFFECTS: take cp from off board list and place cp on this board at given x, y position
    public void placeFromOffBoard(ChessPiece cp, int x, int y) {
        gameBoard.place(cp, x, y);
        String colour = cp.getColour();
        ArrayList<ChessPiece> listTakeFrom;
        ArrayList<ChessPiece> listAddTo;
        if (colour.equals("white")) {
            listTakeFrom = whiteChessPiecesOffBoard;
            listAddTo = whiteChessPiecesOnBoard;
        } else {
            listTakeFrom = blackChessPiecesOffBoard;
            listAddTo = blackChessPiecesOnBoard;
        }
        listTakeFrom.remove(cp);
        cp.setPosX(x);
        cp.setPosY(y);
        cp.setOnBoard(true);
        listAddTo.add(cp);
    }

    // REQUIRES: cp must not be on the board nor in off board list, cp must have valid x, y position (within the range
    //           [1.8], x and y must not be occupied
    // MODIFIES: this
    // EFFECTS: place newly created cp on this board at given x, y position
    public void placeNew(ChessPiece cp) {
        int x = cp.getPosX();
        int y = cp.getPosY();
        gameBoard.place(cp, x, y);
        String colour = cp.getColour();
        ArrayList<ChessPiece> listAddTo;
        if (colour.equals("white")) {
            listAddTo = whiteChessPiecesOnBoard;
        } else {
            listAddTo = blackChessPiecesOnBoard;
        }
        listAddTo.add(cp);
    }

    // REQUIRES: side must be one of "black" and "white"
    // EFFECTS: tells whether the given side's king is checkmated
    public boolean checkmate(String side) {
        ArrayList<ChessPiece> examinedList;
        if (side.equals("white")) {
            examinedList = whiteChessPiecesOnBoard;
        } else {
            examinedList = blackChessPiecesOnBoard;
        }
        return !haveLegalMove(examinedList) && check(side);
    }

    // EFFECTS: tells whether the game has ended by checkmate or stalemate
    public boolean hasEnded() {
        return checkmate("white") || checkmate("black") || stalemate(turn) || drawn;
    }

    // REQUIRES: history must not be empty
    // EFFECTS: returns the most recent moves in history
    public Moves getMostRecentMoves() {
        int totalMoves = history.size();
        return history.get(totalMoves - 1);
    }

    // REQUIRES: moves must consist of 1 to 3 move variables
    // MODIFIES: this
    // EFFECTS: update the given move on history, update EventLog
    public void updateHistory(Moves moves) {
        String event = "Move: ";
        ArrayList<Move> moveList = moves.getMoves();
        if (moveList.size() == 3) {
            event = event + eventMessageThreeMoves(moveList);
        } else if (moveList.size() == 2) {
            event = event + eventMessageTwoMoves(moveList);
        } else if (moveList.size() == 1) {
            event = event + eventMessageOneMove(moveList);
        }
        EventLog.getInstance().logEvent(new Event(event));
        history.add(moves);
        if (hasEnded()) {
            eventLogUpdateResult();
        }
    }

    // REQUIRES: moveList has exactly three moves
    // EFFECTS: constructs a message for event log for cases where there are three moves in moveList
    public String eventMessageThreeMoves(ArrayList<Move> moveList) {
        Move m1 = moveList.get(1);
        Move m2 = moveList.get(1);
        Move m3 = moveList.get(2);
        return m2.getChessPiece().getColour() + " pawn at " + posName(m2.getBeginX(), m2.getBeginY())
                + " attacks " + m1.getChessPiece().getColour() + " " + m1.getChessPiece().getType() + " at "
                + posName(m1.getBeginX(), m1.getBeginY()) + ". Promote the moved pawn to a "
                + m3.getChessPiece().getType() + ". ";
    }

    // REQUIRES: moveList has exactly two moves
    // EFFECTS: constructs a message for event log for cases where there are two moves in moveList
    public String eventMessageTwoMoves(ArrayList<Move> moveList) {
        Move m1 = moveList.get(0);
        Move m2 = moveList.get(1);
        String message;
        if (m1.getChessPiece().getColour().equals(m2.getChessPiece().getColour())) {
            if (m1.getBeginX() == m2.getEndX()) {
                message = "move " + m1.getChessPiece().getColour() + " pawn at " + posName(m1.getBeginX(),
                        m1.getBeginY()) + " to " + posName(m2.getEndX(), m2.getEndY()) + ". Promote the moved pawn"
                        + " to a " + m2.getChessPiece().getType() + ". ";
            } else {
                message = "castling between " + m1.getChessPiece().getColour() + " "
                        + m1.getChessPiece().getType() + " at " + posName(m1.getBeginX(), m1.getBeginY()) + " and "
                        + m2.getChessPiece().getColour() + " " + m2.getChessPiece().getType() + " at "
                        + posName(m2.getBeginX(), m2.getBeginY()) + ". ";
            }
        } else {
            message = m2.getChessPiece().getColour() + " " + m2.getChessPiece().getType() + " at "
                    + posName(m2.getBeginX(), m2.getBeginY()) + " attacks " + m1.getChessPiece().getColour() + " "
                    + m1.getChessPiece().getType() + " at " + posName(m1.getBeginX(), m1.getBeginY()) + ". ";
        }
        return message;
    }

    // REQUIRES: moveList has exactly one move
    // EFFECTS: constructs a message for event log for cases where there is one move in moveList
    public String eventMessageOneMove(ArrayList<Move> moveList) {
        Move m1 = moveList.get(0);
        return "move " + m1.getChessPiece().getColour() + " " + m1.getChessPiece().getType() + " at "
                + posName(m1.getBeginX(), m1.getBeginY()) + " to " + posName(m1.getEndX(), m1.getEndY()) + ". ";
    }

    // MODIFIES: EventLog
    // REQUIRES: this game has ended
    // EFFECTS: update EventLog with result
    public void eventLogUpdateResult() {
        if (checkmate("white")) {
            EventLog.getInstance().logEvent(new Event("Black wins. Game ends in a checkmate."));
        } else if (check("black")) {
            EventLog.getInstance().logEvent(new Event("White wins. Game ends in a checkmate."));
        } else if (stalemate(turn)) {
            EventLog.getInstance().logEvent(new Event("There is a tie. Game ends in a stalemate."));
        } else {
            EventLog.getInstance().logEvent(new Event("There is a tie. Game ends in a draw."));
        }
    }

    // REQUIRES: history must not be empty
    // MODIFIES: this
    // EFFECTS: remove the most recent moves from history, do nothing if history is empty, update EventLog
    public void removeMostRecentMoves() {
        String eventMessage = "Undo move: ";
        int totalMoves = history.size();
        Moves recent = getMostRecentMoves();
        ArrayList<Move> moveList = recent.getMoves();
        for (int i = moveList.size() - 1; i >= 0; i--) {
            Move move = moveList.get(i);
            ChessPiece movedChess = move.getChessPiece();
            if (move.getBeginX() == 0 && move.getBeginY() == 0) {
                eventMessage = eventMessage + "Remove " + movedChess.getColour() + " " + movedChess.getType() + " at "
                        + posName(move.getEndX(), move.getEndY()) + ". ";
            } else if (move.getEndX() == 0 && move.getEndY() == 0) {
                eventMessage = eventMessage + "Place " + movedChess.getColour() + " " + movedChess.getType() + " at "
                        + posName(move.getBeginX(), move.getBeginY()) + ". ";
            } else {
                eventMessage = eventMessage + "Move " + movedChess.getColour() + " " + movedChess.getType() + " from "
                        + posName(move.getEndX(), move.getEndY()) + " back to "
                        + posName(move.getBeginX(), move.getBeginY()) + ". ";
            }
        }
        EventLog.getInstance().logEvent(new Event(eventMessage));
        history.remove(totalMoves - 1);
    }

    // MODIFIES: this
    // EFFECTS: set draw to given boolean value
    public void setDrawn(boolean d) {
        drawn = d;
    }

    // MODIFIES: this
    // EFFECTS: reverse the field "turn" and make it the opposite team's turn
    public void reverseTurn() {
        if (turn.equals("white")) {
            turn = "black";
        } else {
            turn = "white";
        }
    }

    // EFFECTS: return turn
    public String getTurn() {
        return turn;
    }

    // EFFECTS: return drawn
    public boolean getDrawn() {
        return drawn;
    }

    // REQUIRES: column must be in the range [1, 8]
    // EFFECTS: returns the formal name for a column in chess (a - g)
    public String columnName(int column) {
        if (column == 1) {
            return "a";
        } else if (column == 2) {
            return "b";
        } else if (column == 3) {
            return "c";
        } else if (column == 4) {
            return "d";
        } else if (column == 5) {
            return "e";
        } else if (column == 6) {
            return "f";
        } else if (column == 7) {
            return "g";
        } else {
            return "h";
        }
    }

    // REQUIRES: row must be in the range [1, 8]
    // EFFECTS: returns the formal name for a row in chess (1 - 8)
    public String rowName(int row) {
        int rowName = 8 - row + 1;
        return Integer.toString(rowName);
    }

    // REQUIRES: row must be in the range [1, 8], column must be in the range [1, 8]
    // EFFECTS: returns the formal name for a position on a chess board
    public String posName(int column, int row) {
        return columnName(column) + rowName(row);
    }
}
