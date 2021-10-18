package model;

import java.util.ArrayList;

public class Game {

    private ArrayList<ChessPiece> whiteChessPiecesOnBoard;
    private ArrayList<ChessPiece> blackChessPiecesOnBoard;
    private ArrayList<ChessPiece> whiteChessPiecesOffBoard;
    private ArrayList<ChessPiece> blackChessPiecesOffBoard;
    private Board gameBoard;
    private ArrayList<Moves> history;

    // EFFECTS: constructs and sets up a chess game
    public Game() {
        whiteChessPiecesOnBoard = buildDefaultChessOnBoard("white");
        blackChessPiecesOnBoard = buildDefaultChessOnBoard("black");
        whiteChessPiecesOffBoard = buildDefaultChessOffBoard("white");
        blackChessPiecesOffBoard = buildDefaultChessOffBoard("black");
        gameBoard = new Board();
        updateBoard(whiteChessPiecesOnBoard);
        updateBoard(blackChessPiecesOnBoard);
        history = new ArrayList<Moves>();
    }

    // REQUIRES: colour must be "black" or "white"
    // EFFECTS: constructs the default chess pieces on board
    private ArrayList<ChessPiece> buildDefaultChessOnBoard(String colour) {
        ArrayList<ChessPiece> cp = new ArrayList<ChessPiece>();
        int firstRow;
        int secondRow;
        if (colour.equals("white")) {
            firstRow = 8;
            secondRow = 7;
        } else {
            firstRow = 1;
            secondRow = 2;
        }
        cp.add(new Rook(colour,1,firstRow));
        cp.add(new Knight(colour,2,firstRow));
        cp.add(new Bishop(colour,3,firstRow));
        cp.add(new Queen(colour,4,firstRow));
        cp.add(new King(colour,5,firstRow));
        cp.add(new Bishop(colour,6,firstRow));
        cp.add(new Knight(colour,7,firstRow));
        cp.add(new Rook(colour,8,firstRow));
        for (int i = 1; i <= 8; i++) {
            cp.add(new Pawn(colour,i,secondRow));
        }
        return cp;
    }

    // REQUIRES: colour must be "black" or "white"
    // EFFECTS: constructs the default chess pieces off board
    private ArrayList<ChessPiece> buildDefaultChessOffBoard(String colour) {
        ArrayList<ChessPiece> cp = new ArrayList<ChessPiece>();
        for (int i = 0; i < 8; i++) {
            cp.add(new Queen(colour));
            cp.add(new Bishop(colour));
            cp.add(new Knight(colour));
            cp.add(new Rook(colour));
        }
        return cp;
    }

    // EFFECTS: update the chess info in given chess list to the board in this game
    private void updateBoard(ArrayList<ChessPiece> cpl) {
        for (ChessPiece cp: cpl) {
            gameBoard.place(cp,cp.getPosX(),cp.getPosY());
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
        ArrayList<ChessPiece> copyList = new ArrayList<ChessPiece>(chessList);
        for (ChessPiece cp: copyList) {
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
        boolean check = false;
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
        for (ChessPiece cp: chessList) {
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
        gameBoard.move(cp,x,y);
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

    // REQUIRES: cp must not be on the board, x and y must be within the range [1, 8]
    //           x and y must not be occupied
    // MODIFIES: this
    // EFFECTS: place cp on this board at given x, y position
    public void place(ChessPiece cp, int x, int y) {
        gameBoard.place(cp,x,y);
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
        return checkmate("white") || checkmate("black") || stalemate("white") || stalemate("black");
    }

    // REQUIRES: history must not be empty
    // EFFECTS: returns the most recent moves in history
    public Moves getMostRecentMoves() {
        int totalMoves = history.size();
        Moves recentMoves = history.get(totalMoves - 1);
        return recentMoves;
    }

    // MODIFIES: this
    // EFFECTS: update the given move on history
    public void updateHistory(Moves moves) {
        history.add(moves);
    }

    // MODIFIES: this
    // EFFECTS: remove the most recent moves from history, do nothing if history is empty
    public void removeMostRecentMoves() {
        int totalMoves = history.size();
        history.remove(totalMoves - 1);
    }
}
