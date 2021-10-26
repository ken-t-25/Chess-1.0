package ui;

import model.*;
import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PlayGame {

    private static final String JSON_STORE = "./data/chessgame.json";
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private boolean exit;

    // EFFECTS: constructs a chess game
    public PlayGame() {
        game = new Game();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        exit = false;
    }

    // EFFECTS: starts the application
    public void start() {
        while (!exit) {
            Scanner scan = new Scanner(System.in);
            printStart();
            String response = scan.nextLine();
            if (response.equals("load")) {
                try {
                    game = jsonReader.readGame();
                    System.out.println("You have load your most recently saved game from file.");
                    play();
                } catch (IOException e) {
                    System.out.println("Unable to read from file: " + JSON_STORE);
                }
            } else if (response.equals("start")) {
                System.out.println("You have started a new game.");
                play();
            } else if (response.equals("exit")) {
                String confirmation = exitConfirmation();
                if (confirmation.equals("yes")) {
                    System.out.println("Good bye:)");
                    exit = true;
                }
            }
        }
    }

    // EFFECTS: prints statements when application is started
    public void printStart() {
        System.out.println("Enter:");
        System.out.println("\"load\" to load game from file");
        System.out.println("\"start\" to start a new game");
        System.out.println("\"exit\" to exit game");
    }

    // EFFECTS: confirms whether user wants to exit the game
    public String exitConfirmation() {
        Scanner confirm = new Scanner(System.in);
        System.out.println("Are you sure to exit? Type \"yes\" to exit or anything else to keep playing.");
        return confirm.nextLine();
    }

    // EFFECTS: starts and proceeds a new chess game
    public void play() {
        while (!game.hasEnded()) {
            Scanner act = new Scanner(System.in);
            System.out.println("Pick your action (move, undo, draw, save, leave): " + game.getTurn() + "'s turn");
            String action = act.nextLine();
            if (action.equals("move")) {
                moveAction();
            } else if (action.equals("undo")) {
                undoAction();
            } else if (action.equals("draw")) {
                drawAction();
            } else if (action.equals("save")) {
                saveGame();
            } else if (action.equals("leave")) {
                Scanner leaveConfirm = new Scanner(System.in);
                System.out.println("Are you sure to leave this game? Type \"yes\" to leave or anything else to keep"
                        + "playing (Note: remember to save your game for later).");
                String confirm = leaveConfirm.nextLine();
                if (confirm.equals("yes")) {
                    break;
                }
            }
        }
        printResult();
    }

    // MODIFIES: this
    // EFFECTS: asks user to input the position of the chess piece they wish to move as well as the position they
    //          wish to move to, then determines whether this information is valid and proceed move with this
    //          information
    public void moveAction() {
        Scanner beginX = new Scanner(System.in);
        System.out.println("Enter column of the chess piece you wish to move");
        int bx = beginX.nextInt();
        Scanner beginY = new Scanner(System.in);
        System.out.println("Enter row of the chess piece you wish to move");
        int by = beginY.nextInt();
        Scanner endX = new Scanner(System.in);
        System.out.println("Enter column of the position you wish to move to");
        int ex = endX.nextInt();
        Scanner endY = new Scanner(System.in);
        System.out.println("Enter row of the position you wish to move to");
        int ey = endY.nextInt();
        handleMoveInfo(bx, by, ex, ey);
    }

    // MODIFIES: this
    // EFFECTS: determines whether the given move information is valid, then apply move
    public void handleMoveInfo(int bx, int by, int ex, int ey) {
        if (bx >= 0 && bx <= 8 && by >= 0 && by <= 8 && ex >= 0 && ex <= 8 && ey >= 0 && ey <= 8) {
            Position initialPosn = new Position(bx, by);
            int initialIndex = initialPosn.toSingleValue() - 1;
            ChessPiece cp = game.getBoard().getOnBoard().get(initialIndex);
            if (!Objects.isNull(cp) && cp.getColour().equals(game.getTurn())) {
                for (Position posn : cp.possibleMoves(game)) {
                    if (posn.getPosX() == ex && posn.getPosY() == ey) {
                        applyMove(cp, bx, by, ex, ey);
                    }
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: apply move with given move information by finding the chess piece being moved, checking whether this
    //          move is a special move, and update the game
    public void applyMove(ChessPiece cp, int bx, int by, int ex, int ey) {
        if (cp instanceof King && Math.abs(bx - ex) == 2) {
            castlingMove(cp, bx, by, ex, ey);
        } else if (cp instanceof Pawn && (ey == 1 || ey == 8)) {
            promotionMove(cp, bx, by, ex, ey);
        } else {
            simpleMove(cp, bx, by, ex, ey);
        }
        game.reverseTurn();
        System.out.println("Move success");
        if (game.check(game.getTurn())) {
            System.out.println(game.getTurn() + " is checked!");
        }
    }

    // MODIFIES: this
    // EFFECTS: perform a castling move with given information
    public void castlingMove(ChessPiece cp, int bx, int by, int ex, int ey) {
        int rookBeginX;
        int rookBeginY;
        int direction = (ex - bx) / Math.abs(ex - bx);
        if (direction > 0) {
            rookBeginX = 8;
        } else {
            rookBeginX = 1;
        }
        if (cp.getColour().equals("white")) {
            rookBeginY = 8;
        } else {
            rookBeginY = 1;
        }
        Position rookPosn = new Position(rookBeginX, rookBeginY);
        ChessPiece rook = game.getBoard().getOnBoard().get(rookPosn.toSingleValue() - 1);
        game.move(cp, ex, ey);
        game.move(rook, ex - direction, ey);
        Moves moves = new Moves();
        moves.addMove(new Move(bx, by, ex, ey, cp, false));
        moves.addMove(new Move(rookBeginX, rookBeginY, ex - direction, ey, rook, false));
        game.updateHistory(moves);
    }

    // MODIFIES: this
    // EFFECTS: perform a pawn promotion move with given information
    public void promotionMove(ChessPiece cp, int bx, int by, int ex, int ey) {
        Moves moves = new Moves();
        Position endPosn = new Position(ex, ey);
        ChessPiece attackedEnemy = game.getBoard().getOnBoard().get(endPosn.toSingleValue() - 1);
        if (!Objects.isNull(attackedEnemy)) {
            boolean enemyMoveStatus = attackedEnemy.hasMoved();
            game.remove(attackedEnemy);
            moves.addMove(new Move(ex, ey, 0, 0, attackedEnemy, enemyMoveStatus));
        }
        game.move(cp, ex, ey);
        Scanner userChoice = new Scanner(System.in);
        System.out.println("PROMOTE YOUR PAWN!!! Type \"queen\", \"bishop\", \"knight\", \"rook\", or any other "
                + "response if you don't wish to promote your pawn");
        String promotion = userChoice.nextLine();
        for (Move m: handlePromotionInput(cp,bx,by,ex,ey,promotion).getMoves()) {
            moves.addMove(m);
        }
        game.updateHistory(moves);
    }

    // MODIFIES: this
    // EFFECTS: handle promotion with user's input
    public Moves handlePromotionInput(ChessPiece cp, int bx, int by, int ex, int ey, String pro) {
        Moves moves = new Moves();
        String colour = cp.getColour();
        boolean pawnMoveStatus = cp.hasMoved();
        if (!(pro.equals("queen") || pro.equals("bishop") || pro.equals("knight") || pro.equals("rook"))) {
            game.move(cp, ex, ey);
            moves.addMove(new Move(bx, by, ex, ey, cp, pawnMoveStatus));
        } else {
            ChessPiece addChess;
            if (pro.equals("queen")) {
                addChess = new Queen(colour, ex, ey);
            } else if (pro.equals("bishop")) {
                addChess = new Bishop(colour, ex, ey);
            } else if (pro.equals("knight")) {
                addChess = new Knight(colour, ex, ey);
            } else {
                addChess = new Rook(colour, ex, ey);
            }
            game.remove(cp);
            game.placeNew(addChess);
            moves.addMove(new Move(bx,by,0,0,cp,pawnMoveStatus));
            moves.addMove(new Move(0,0,ex,ey,addChess,false));
        }
        return moves;
    }

    // MODIFIES: this
    // EFFECTS: perform a simple move with given information
    public void simpleMove(ChessPiece cp, int bx, int by, int ex, int ey) {
        Moves moves = new Moves();
        Position endPosn = new Position(ex, ey);
        ChessPiece attackedEnemy = game.getBoard().getOnBoard().get(endPosn.toSingleValue() - 1);
        if (!Objects.isNull(attackedEnemy)) {
            boolean enemyMoveStatus = attackedEnemy.hasMoved();
            game.remove(attackedEnemy);
            moves.addMove(new Move(ex, ey, 0, 0, attackedEnemy, enemyMoveStatus));
        }
        boolean cpMoveStatus = cp.hasMoved();
        game.move(cp, ex, ey);
        moves.addMove(new Move(bx, by, ex, ey, cp, cpMoveStatus));
        game.updateHistory(moves);
    }

    // MODIFIES: this
    // EFFECTS: undo the most recent move
    public void undoAction() {
        if (game.getHistory().size() > 0) {
            Moves recentMoves = game.getMostRecentMoves();
            ArrayList<Move> moveList = recentMoves.getMoves();
            for (int i = moveList.size() - 1; i >= 0; i--) {
                Move move = moveList.get(i);
                ChessPiece movedChess = move.getChessPiece();
                if (move.getBeginX() == 0 && move.getBeginY() == 0) {
                    game.remove(movedChess);
                } else if (move.getEndX() == 0 && move.getEndY() == 0) {
                    game.placeFromOffBoard(movedChess, move.getBeginX(), move.getBeginY());
                } else {
                    game.move(movedChess, move.getBeginX(), move.getBeginY());
                }
                movedChess.setMove(move.getMoveStatus());
            }
            game.removeMostRecentMoves();
            game.reverseTurn();
        }
    }

    // MODIFIES: this
    // EFFECTS: ask user again if they want to draw the game, if "yes", make the field "drawn" to true,
    //          otherwise, do nothing
    public void drawAction() {
        Scanner makeSureToDraw = new Scanner(System.in);
        System.out.println("Are you sure that you wish to draw the game? If yes, type \"yes\". If no, "
                        + "type anything else");
        String response = makeSureToDraw.nextLine();
        if (response.equals("yes")) {
            game.setDrawn(true);
        }
    }

    // EFFECTS: print the result of this game
    public void printResult() {
        if (game.getDrawn() || game.stalemate("white") || game.stalemate("black")) {
            System.out.println("There is a tie!!!");
        } else if (game.checkmate("white")) {
            System.out.println("BLACK WINS!!!");
        } else if (game.checkmate("black")) {
            System.out.println("WHITE WINS!!!");
        } else {
            System.out.println("Players have left the game.");
        }
    }

    // EFFECTS: saves game to file
    private void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.writeGame(game);
            jsonWriter.close();
            System.out.println("Game has been saved " + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}

