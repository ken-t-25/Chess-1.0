package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnightTest {

    Knight knight;
    King king;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
        knight = new Knight("white");
        king = new King("white");
        game = new Game();
        bd = new Board();
        white = new ArrayList<ChessPiece>();
        black = new ArrayList<ChessPiece>();
        whiteOff = new ArrayList<ChessPiece>();
        blackOff = new ArrayList<ChessPiece>();
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
    public void testPossibleMovesMiddle() {
        placeOnBoard(knight, 4, 6);
        placeOnBoard(king,4,5);
        white.add(knight);
        white.add(king);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(3, 4));
        expected.add(new Position(2, 5));
        expected.add(new Position(3, 8));
        expected.add(new Position(2, 7));
        expected.add(new Position(5, 4));
        expected.add(new Position(6, 5));
        expected.add(new Position(5, 8));
        expected.add(new Position(6, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesSide() {
        placeOnBoard(knight, 5, 8);
        placeOnBoard(king,5,7);
        white.add(knight);
        white.add(king);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(4, 6));
        expected.add(new Position(3, 7));
        expected.add(new Position(6, 6));
        expected.add(new Position(7, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCorner() {
        placeOnBoard(knight, 8, 8);
        placeOnBoard(king,8,7);
        white.add(knight);
        white.add(king);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(7, 6));
        expected.add(new Position(6, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleBlockByTeammate() {
        placeOnBoard(knight, 4, 6);
        placeOnBoard(king,4,5);
        Bishop bishop = new Bishop("white");
        placeOnBoard(bishop,2,5);
        white.add(knight);
        white.add(bishop);
        white.add(king);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(3, 4));
        expected.add(new Position(3, 8));
        expected.add(new Position(2, 7));
        expected.add(new Position(5, 4));
        expected.add(new Position(6, 5));
        expected.add(new Position(5, 8));
        expected.add(new Position(6, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockByEnemy() {
        placeOnBoard(knight, 4, 6);
        placeOnBoard(king,4,5);
        Bishop bishop = new Bishop("black");
        placeOnBoard(bishop,6,5);
        white.add(knight);
        white.add(king);
        black.add(bishop);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(3, 4));
        expected.add(new Position(2, 5));
        expected.add(new Position(3, 8));
        expected.add(new Position(2, 7));
        expected.add(new Position(5, 4));
        expected.add(new Position(6, 5));
        expected.add(new Position(5, 8));
        expected.add(new Position(6, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingUnderAttack() {
        Rook rook = new Rook("black");
        placeOnBoard(knight, 4, 6);
        placeOnBoard(king,6,3);
        placeOnBoard(rook, 6,8);
        white.add(knight);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<Position>();
        expected.add(new Position(6, 5));
        expected.add(new Position(6, 7));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesKingWillBeUnderAttack() {
        Rook rook = new Rook("black");
        placeOnBoard(knight, 4, 6);
        placeOnBoard(king, 4,3);
        placeOnBoard(rook, 4,8);
        white.add(knight);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = knight.possibleMoves(game);
        assertEquals(0, pm.size());
    }
}
