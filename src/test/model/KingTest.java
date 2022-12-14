package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {

    King king;
    Game game;
    Board bd;
    ArrayList<ChessPiece> white;
    ArrayList<ChessPiece> black;
    ArrayList<ChessPiece> whiteOff;
    ArrayList<ChessPiece> blackOff;

    @BeforeEach
    private void setup() {
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
    public void testPossibleMovesMiddle() {
        placeOnBoard(king, 5, 4);
        white.add(king);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(4, 5));
        expected.add(new Position(5, 3));
        expected.add(new Position(5, 5));
        expected.add(new Position(6, 3));
        expected.add(new Position(6, 4));
        expected.add(new Position(6, 5));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesSide() {
        placeOnBoard(king, 8, 3);
        white.add(king);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(7, 2));
        expected.add(new Position(7, 3));
        expected.add(new Position(7, 4));
        expected.add(new Position(8, 2));
        expected.add(new Position(8, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCorner() {
        placeOnBoard(king, 1, 8);
        white.add(king);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(1, 7));
        expected.add(new Position(2, 7));
        expected.add(new Position(2, 8));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockedByTeamMate() {
        placeOnBoard(king, 5, 4);
        Bishop bishop = new Bishop("white");
        placeOnBoard(bishop, 5, 5);
        white.add(king);
        white.add(bishop);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(4, 5));
        expected.add(new Position(5, 3));
        expected.add(new Position(6, 3));
        expected.add(new Position(6, 4));
        expected.add(new Position(6, 5));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockedByOpponentCanAttack() {
        placeOnBoard(king, 5, 4);
        Rook rook = new Rook("black");
        placeOnBoard(rook, 5, 5);
        white.add(king);
        black.add(rook);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 4));
        expected.add(new Position(5, 5));
        expected.add(new Position(6, 3));
        expected.add(new Position(6, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesBlockedByOpponentCannotAttack() {
        placeOnBoard(king, 5, 4);
        Rook rook = new Rook("black");
        Queen queen = new Queen("black");
        placeOnBoard(rook, 5, 5);
        placeOnBoard(queen, 6, 6);
        white.add(king);
        black.add(rook);
        black.add(queen);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(4, 3));
        expected.add(new Position(4, 4));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingNoEnemiesRookKingHasNotMoved() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(6, 1));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingNoEnemiesRookMoved() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Rook rook3 = new Rook("white");
        Rook rook4 = new Rook("white");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 6, 1);
        placeOnBoard(rook3, 4, 3);
        placeOnBoard(rook4, 8, 2);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        white.add(rook3);
        white.add(rook4);
        setGame();
        game.move(rook1, 1, 2);
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
        game.move(rook1, 1, 1);
        pm = king.possibleMoves(game);
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
        game.move(rook1, 2, 1);
        pm = king.possibleMoves(game);
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
        game.move(rook1, 2, 2);
        pm = king.possibleMoves(game);
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);

    }

    @Test
    public void testPossibleMovesCastlingNoEnemiesKingMoved() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        setGame();
        game.move(king, 3, 1);
        game.move(king, 4, 1);
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingNoEnemiesPathBlocked() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Bishop bishop = new Bishop("white");
        Bishop bishop1 = new Bishop("white");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        placeOnBoard(bishop, 7, 1);
        placeOnBoard(bishop1, 2, 1);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        white.add(bishop);
        white.add(bishop1);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingEnemiesAttackingKing() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Rook rook3 = new Rook("black");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        placeOnBoard(rook3, 4, 6);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        black.add(rook3);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingEnemiesAttackingPath() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Rook rook3 = new Rook("black");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        placeOnBoard(rook3, 5, 6);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        black.add(rook3);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(2, 1));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }

    @Test
    public void testPossibleMovesCastlingEnemiesWillAttackEndKing() {
        placeOnBoard(king, 4, 1);
        Rook rook1 = new Rook("white");
        Rook rook2 = new Rook("white");
        Rook rook3 = new Rook("black");
        placeOnBoard(rook1, 1, 1);
        placeOnBoard(rook2, 8, 1);
        placeOnBoard(rook3, 6, 6);
        white.add(king);
        white.add(rook1);
        white.add(rook2);
        black.add(rook3);
        setGame();
        ArrayList<Position> pm = king.possibleMoves(game);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(3, 1));
        expected.add(new Position(3, 2));
        expected.add(new Position(4, 2));
        expected.add(new Position(5, 1));
        expected.add(new Position(5, 2));
        expected.add(new Position(2, 1));
        assertEquals(expected.size(), pm.size());
        arrayListEquals(pm, expected);
    }
}
