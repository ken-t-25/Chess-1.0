package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {

    Bishop bishop;
    King king;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        bishop = new Bishop("white");
        king = new King("white");
        game = new Game();
        bd = new Board();
        white = new ArrayList<>();
        black = new ArrayList<>();
        whiteOff = new ArrayList<>();
        blackOff = new ArrayList<>();
    }

    private void arrayListEquals(ArrayList<Position> list1, ArrayList<Position> list2) {
        for (int i = 0; i < list2.size(); i++) {
            Position expectedMove = list2.get(i);
            Position actualMove = list1.get(i);
            assertEquals(expectedMove.getPosX(), actualMove.getPosX());
            assertEquals(expectedMove.getPosY(), actualMove.getPosY());
        }
    }

    private void setGame() {
        game.setGameBoard(bd);
        game.setWhiteChessPiecesOnBoard(white);
        game.setBlackChessPiecesOnBoard(black);
        game.setWhiteChessPiecesOffBoard(whiteOff);
        game.setBlackChessPiecesOffBoard(blackOff);
    }

    private void placeOnBoard(ChessPiece cp, int x, int y) {
        bd.place(cp, x, y);
        cp.setPosX(x);
        cp.setPosY(y);
    }

    @Test
    public void testBishopPossibleMovesNoBlockingMiddle() {
        placeOnBoard(bishop, 5, 4);
        placeOnBoard(king,5,5);
        white.add(bishop);
        white.add(king);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(4, 5));
        expected.add(new Position(3, 6));
        expected.add(new Position(2, 7));
        expected.add(new Position(1, 8));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testBishopPossibleMovesNoBlockingSide() {
        placeOnBoard(bishop, 1, 4);
        placeOnBoard(king,1,3);
        white.add(bishop);
        white.add(king);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(2, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 1));
        expected.add(new Position(2, 5));
        expected.add(new Position(3, 6));
        expected.add(new Position(4, 7));
        expected.add(new Position(5, 8));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testBishopPossibleMovesNoBlockingCorner() {
        placeOnBoard(bishop, 1, 1);
        placeOnBoard(king,1,2);
        white.add(bishop);
        white.add(king);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(2, 2));
        expected.add(new Position(3, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(5, 5));
        expected.add(new Position(6, 6));
        expected.add(new Position(7, 7));
        expected.add(new Position(8, 8));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testBishopPossibleMovesBlockBySameTeam() {
        Rook rook = new Rook("white");
        placeOnBoard(bishop, 5, 4);
        placeOnBoard(king,5,5);
        placeOnBoard(rook, 3,6);
        white.add(bishop);
        white.add(king);
        white.add(rook);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(3, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(4, 5));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testBishopPossibleMovesBlockByOpponents() {
        Rook rook = new Rook("black");
        placeOnBoard(bishop, 5, 4);
        placeOnBoard(king,5,5);
        placeOnBoard(rook, 4,3);
        white.add(bishop);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 5));
        expected.add(new Position(3, 6));
        expected.add(new Position(2, 7));
        expected.add(new Position(1, 8));
        expected.add(new Position(6, 3));
        expected.add(new Position(7, 2));
        expected.add(new Position(8, 1));
        expected.add(new Position(6, 5));
        expected.add(new Position(7, 6));
        expected.add(new Position(8, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testBishopPossibleMovesKingWillBeChecked() {
        Rook rook = new Rook("black");
        placeOnBoard(bishop, 5, 4);
        placeOnBoard(rook, 5,3);
        placeOnBoard(king, 5,5);
        white.add(bishop);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        assertEquals(0,pm.size());
    }

    @Test
    public void testBishopPossibleMovesKingIsChecked() {
        Knight knight = new Knight("black");
        placeOnBoard(bishop, 5, 4);
        placeOnBoard(knight, 6,5);
        placeOnBoard(king,4,6);
        white.add(bishop);
        white.add(king);
        black.add(knight);
        setGame();
        ArrayList<Position> pm = bishop.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(6, 5));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }
}
