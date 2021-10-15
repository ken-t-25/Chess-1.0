package model;

import java.util.ArrayList;
import java.util.Objects;

public class King extends ChessPiece {

    // REQUIRES: colour must be one of "black" and "white"
    // EFFECTS: constructs a king
    public King(String colour) {
        super(colour);
    }



    // EFFECTS: consumes a game board and returns a list of positions that represents
    //          the possible moves of this bishop
    @Override
    public ArrayList<Position> possibleMoves(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        moves.addAll(kingPositionTest(game, posX - 1, posY - 1));
        moves.addAll(kingPositionTest(game, posX - 1, posY));
        moves.addAll(kingPositionTest(game, posX - 1, posY + 1));
        moves.addAll(kingPositionTest(game, posX, posY - 1));
        moves.addAll(kingPositionTest(game, posX, posY + 1));
        moves.addAll(kingPositionTest(game, posX + 1, posY - 1));
        moves.addAll(kingPositionTest(game, posX + 1, posY));
        moves.addAll(kingPositionTest(game, posX + 1, posY + 1));
        moves.addAll(castling(game));
        return moves;
    }

    // EFFECTS: find possible moves of a king by positions (excluding castling)
    private ArrayList<Position> kingPositionTest(Game game, int x, int y) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Position testPosition = new Position(x,y);
        if (x >= 1 && x <= 8 && y >= 1 && y <= 8) {
            moves = positionTest(game,testPosition);
        }
        return moves;
    }

    // EFFECTS: find possible castling moves
    private ArrayList<Position> castling(Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        if (!move && !game.check(colour)) {
            ArrayList<ChessPiece> rooks = new ArrayList<ChessPiece>();
            ArrayList<ChessPiece> examinedList = new ArrayList<ChessPiece>();
            if (colour.equals("white")) {
                examinedList = game.getWhiteChessPiecesOnBoard();
            } else {
                examinedList = game.getBlackChessPiecesOnBoard();
            }
            for (ChessPiece cp: examinedList) {
                if (cp instanceof Rook) {
                    rooks.add(cp);
                }
            }
            for (ChessPiece rook: rooks) {
                moves.addAll(testRook(rook, game));
            }
        }
        return moves;
    }

    // EFFECTS: test whether given bishop can perform castling move with this king,
    //          if can, return a list that contains the corresponding move of this king
    //          otherwise, return an empty list
    private ArrayList<Position> testRook(ChessPiece rook, Game game) {
        ArrayList<Position> moves = new ArrayList<Position>();
        if (!rook.hasMoved()) {
            int rookX = rook.getPosX();
            int difference = rookX - posX;
            int absDiff = Math.abs(difference);
            int direction = difference / absDiff;
            Position testPosn1 = new Position(posX + direction, posY);
            Position testPosn2 = new Position(posX + (2 * direction), posY);
            boolean test3 = true;
            if (absDiff == 4) {
                Position testPosn3 = new Position(posX + (3 * direction), posY);
                int singlePosn3 = testPosn3.toSingleValue();
                test3 = Objects.isNull(game.getBoard().getOnBoard().get(singlePosn3 - 1));
            }
            ArrayList<Position> test1 = castlingPositionTest(game, testPosn1);
            ArrayList<Position> test2 = castlingPositionTest(game, testPosn2);
            if (!test1.isEmpty() && !test2.isEmpty() && test3) {
                moves.add(testPosn2);
            }
        }
        return moves;
    }

    private ArrayList<Position> castlingPositionTest(Game game, Position posn) {
        ArrayList<Position> moves = new ArrayList<Position>();
        Board bd = game.getBoard();
        int posnIndex = posn.toSingleValue() - 1;
        int initialX = this.posX;
        int initialY = this.posY;
        boolean initialMove = move;
        if (Objects.isNull(bd.getOnBoard().get(posnIndex))) {
            game.move(this,posn.getPosX(),posn.getPosY());
            if (!game.check(colour)) {
                moves.add(posn);
            }
            game.move(this,initialX,initialY);
            move = initialMove;
        }
        return moves;
    }

}
