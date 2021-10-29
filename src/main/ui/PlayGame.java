package ui;

import com.sun.corba.se.impl.ior.JIDLObjectKeyTemplate;
import model.*;
import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PlayGame {

    private static final String JSON_STORE = "./data/chessGame.json";
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private boolean exit;
    private ArrayList<String> results;

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
                startOptions();
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

    // EFFECTS: let user choose whether they wish to play a regular game or build their own game
    public void startOptions() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to build your own game? Type \"yes\" to build your own game or anything else"
                + " to play a regular game");
        String decision = scan.nextLine();
        if (decision.equals("yes")) {
            game = new Game(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new Board(),
                    new ArrayList<>(), "white", false);
            boolean modifyChess = true;
            while (modifyChess) {
                modifyChess = modifyChessAction();
            }
            String firstTeam = "";
            while (!firstTeam.equals("white") && !firstTeam.equals("black")) {
                firstTeam = setFirstTeam();
            }
            game.setWhiteChessPiecesOffBoard(setOffBoard("white"));
            game.setBlackChessPiecesOffBoard(setOffBoard("black"));
        } else {
            game = new Game();
        }
    }

    // REQUIRES: team must be black or white
    // EFFECTS: returns an array of chess pieces of given team that is not on the board
    public ArrayList<ChessPiece> setOffBoard(String team) {
        int numPawn = 0;
        ArrayList<ChessPiece> examine;
        if (team.equals("white")) {
            examine = game.getWhiteChessPiecesOnBoard();
        } else {
            examine = game.getBlackChessPiecesOnBoard();
        }
        for (ChessPiece cp: examine) {
            if (cp instanceof Pawn) {
                numPawn++;
            }
        }
        return game.buildDefaultChessOffBoard(team, numPawn);
    }

    // EFFECTS: let the user choose which team to go first, return user's input
    public String setFirstTeam() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Which team will move first? (black or white): ");
        String firstTeam = scan.nextLine();
        if (!firstTeam.equals("white") && !firstTeam.equals("black")) {
            System.out.println("Team must be \"black\" or \"white\"");
        } else if (firstTeam.equals("black")) {
            game.reverseTurn();
        }
        return firstTeam;
    }

    // EFFECTS: let the user add or remove chess pieces from game board, return true if user wants to continue
    //          modifying or false if user is done modifying
    public boolean modifyChessAction() {
        boolean modifyChess = true;
        Scanner response = new Scanner(System.in);
        System.out.println("Type \"add\" to add a chess piece on game board, \"remove\" to remove a chess "
                + "piece, or \"done\" if you are done adding");
        String add = response.nextLine();
        if (add.equals("add")) {
            addChessActon();
        } else if (add.equals("remove")) {
            removeChessActon();
        } else if (add.equals("done")) {
            if (!hasKings()) {
                System.out.println("Both teams must have a king!");
            } else if (game.hasEnded()) {
                System.out.println("There is a checkmate or stalemate in this game! Keep adding or removing "
                        + "chess pieces");
            } else {
                modifyChess = false;
            }
        }
        return modifyChess;
    }

    // EFFECTS: returns true there is a king on each side
    public boolean hasKings() {
        boolean hasWhiteKing = false;
        boolean hasBlackKing = false;
        for (ChessPiece cp: game.getBoard().getOnBoard()) {
            if (!Objects.isNull(cp) && cp instanceof King) {
                if (cp.getColour().equals("white")) {
                    hasWhiteKing = true;
                } else {
                    hasBlackKing = true;
                }
            }
        }
        return hasWhiteKing && hasBlackKing;
    }

    // EFFECTS: let the user add a chess piece to this game
    public void addChessActon() {
        Scanner strScan = new Scanner(System.in);
        System.out.println("Colour of chess Piece (white, black): ");
        String colour = strScan.nextLine();
        System.out.println("Chess type (king, queen, bishop, knight, rook, pawn): ");
        String chessType = strScan.nextLine();
        Scanner intScan = new Scanner(System.in);
        System.out.println("Column to place (type an integer from 1 to 8): ");
        int posX = intScan.nextInt();
        System.out.println("Row to place (type an integer from 1 to 8): ");
        int posY = intScan.nextInt();
        if (!colourValid(colour)) {
            System.out.println("Invalid colour! Colour must be \"black\" or \"white\"");
        }
        if (!typeValid(chessType)) {
            System.out.println("Invalid chess type! Chess type must be \"king\", \"queen\", \"bishop\", \"knight\", "
                    + "\"rook\", or \"pawn\"");
        }
        if (!(posX >= 1 && posY >= 1 && posX <= 8 && posY <= 8)) {
            System.out.println("Invalid position! Column and Row must both be integers in the range 1 to 8");
        }
        if (colourValid(colour) && typeValid(chessType) && posX >= 1 && posY >= 1 && posX <= 8 && posY <= 8) {
            proceedAdd(colour, chessType, posX, posY);
        }
    }

    // EFFECTS: returns true if user's input of chess colour is valid
    public boolean colourValid(String colour) {
        return colour.equals("white") || colour.equals("black");
    }

    // EFFECTS: returns true if user's input of chess type is valid
    public boolean typeValid(String type) {
        return type.equals("king") || type.equals("queen") || type.equals("bishop") || type.equals("knight")
                || type.equals("rook") || type.equals("pawn");
    }

    // REQUIRES: colour must black or white; type must be king, queen, bishop, knight, rook, or pawn; x and y must be
    //           integers in the range 1 to 8
    // EFFECTS: proceed with add chess action with valid colour, type, and coordinate inputs
    public void proceedAdd(String colour, String type, int x, int y) {
        ChessPiece chess = buildChess(colour, type, x, y);
        Position pos = new Position(x,y);
        int index = pos.toSingleValue() - 1;
        if (!Objects.isNull(game.getBoard().getOnBoard().get(index))) {
            System.out.println("Position is already occupied");
        } else if (type.equals("king") && kingExist(colour)) {
            System.out.println("Cannot have more than one king on the same team");
        } else if (type.equals("pawn") && ((colour.equals("white") && y == 8) || (colour.equals("black") && y == 1))) {
            System.out.println("Pawn of this colour cannot arrive to this position");
        } else {
            chessAdd(chess, colour, type, x, y);
        }
    }

    // REQUIRES: colour must black or white; type must be king, queen, bishop, knight, rook, or pawn; x and y must be
    //           integers in the range 1 to 8
    // EFFECTS: constructs and returns a chess piece based on given type and colour information
    public ChessPiece buildChess(String colour, String type, int x, int y) {
        ChessPiece cp;
        if (type.equals("king")) {
            cp = new King(colour, x, y);
        } else if (type.equals("queen")) {
            cp = new Queen(colour, x, y);
        } else if (type.equals("bishop")) {
            cp = new Bishop(colour, x, y);
        } else if (type.equals("knight")) {
            cp = new Knight(colour, x, y);
        } else if (type.equals("rook")) {
            cp = new Rook(colour, x, y);
        } else {
            cp = new Pawn(colour, x, y);
        }
        return cp;
    }

    // REQUIRES: colour must be black or white
    // EFFECTS: return true if a king already exist on given team
    public boolean kingExist(String colour) {
        boolean exist = false;
        ArrayList<ChessPiece> examined;
        if (colour.equals("white")) {
            examined = game.getWhiteChessPiecesOnBoard();
        } else {
            examined = game.getBlackChessPiecesOnBoard();
        }
        for (ChessPiece cp: examined) {
            if (cp instanceof King) {
                exist = true;
            }
        }
        return exist;
    }

    // REQUIRES: colour must black or white; type must be king, queen, bishop, knight, rook, or pawn; x and y must be
    //           integers in the range 1 to 8
    // EFFECTS: place given chess piece on given position and make adjustments to the chess piece's feature depending
    //          on the position
    public void chessAdd(ChessPiece cp, String colour, String type, int x, int y) {
        int row1;
        int row2;
        if (colour.equals("white")) {
            row1 = 8;
            row2 = 7;
        } else {
            row1 = 1;
            row2 = 2;
        }
        if (type.equals("king") && !(x == 5 && y == row1)) {
            cp.setMove(true);
        } else if (type.equals("pawn") && !(y == row2)) {
            cp.setMove(true);
        } else if (type.equals("rook") && !(y == row1 && (x == 1 || x == 8))) {
            cp.setMove(true);
        }
        game.placeNew(cp);
        System.out.println("Place success");
    }

    // EFFECTS: removes chess from game board (users can only remove a chess piece when they are constructing their own
    //          game)
    public void removeChessActon() {
        Scanner intScan = new Scanner(System.in);
        System.out.println("Enter the column of the chess piece that you wish to remove");
        int x = intScan.nextInt();
        System.out.println("Enter the row of the chess piece that you wish to remove");
        int y = intScan.nextInt();
        if (!(x >= 1 && y >= 1 && x <= 8 && y <= 8)) {
            System.out.println("Invalid inputs! Column and row must be integers in the range 1 to 8");
        } else {
            Position pos = new Position(x, y);
            int index = pos.toSingleValue() - 1;
            if (Objects.isNull(game.getBoard().getOnBoard().get(index))) {
                System.out.println("This position is empty");
            } else {
                ChessPiece cpRemove = game.getBoard().getOnBoard().get(index);
                game.remove(cpRemove);
                System.out.println("Remove success");
            }
        }
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

