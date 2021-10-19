package ui;

import model.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class PlayGame {

    Game game;
    String turn;
    Boolean drawn;

    // EFFECTS: constructs a chess game
    public PlayGame() {
        game = new Game();
        turn = "white";
        drawn = false;
    }

    // EFFECTS: starts and proceeds a new chess game
    public void play() {
        while (!game.hasEnded() && !drawn) {
            Scanner act = new Scanner(System.in);
            System.out.println("Pick your action (move, undo, draw): " + turn + "'s turn");
            String action = act.nextLine();
            if (action.equals("move")) {
                moveAction();
            } else if (action.equals("undo")) {
                undoAction();
            } else if (action.equals("draw")) {
                drawAction();
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
            if (!Objects.isNull(cp) && cp.getColour().equals(turn)) {
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
        reverseTurn();
        System.out.println("Move success");
        if (game.check(turn)) {
            System.out.println(turn + " is checked!");
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
    // EFFECTS: reverse the field "turn" and make it the opposite team's turn
    public void reverseTurn() {
        if (turn.equals("white")) {
            turn = "black";
        } else {
            turn = "white";
        }
    }

    // MODIFIES: this
    // EFFECTS: undo the most recent move
    public void undoAction() {
        if (game.getHistory().size() > 0) {
            int numMoves = game.getHistory().size();
            Moves recentMoves = game.getHistory().get(numMoves - 1);
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
            reverseTurn();
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
            drawn = true;
        }
    }

    // EFFECTS: print the result of this game
    public void printResult() {
        if (drawn || game.stalemate("white") || game.stalemate("black")) {
            System.out.println("There is a tie!!!");
        } else if (game.checkmate("white")) {
            System.out.println("BLACK WINS!!!");
        } else {
            System.out.println("WHITE WINS!!!");
        }
    }



}

