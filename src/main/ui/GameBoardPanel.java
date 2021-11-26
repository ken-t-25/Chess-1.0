package ui;

import model.*;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class GameBoardPanel extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int PEN_THICKNESS = 4;
    private static final int PEN_HIGHLIGHT = 4;
    private static final Color LIGHT_SQUARE_COLOUR = new Color(248, 246, 231);
    private static final Color DARK_SQUARE_COLOUR = new Color(186, 172, 144);
    private static final Color SQUARE_COLOUR = new Color(153, 204, 255);
    private static final Color MOVE_COLOUR = new Color(153, 255, 255);

    private Game game;
    private ChessPiece clickedChess;

    // EFFECTS: constructs a game board panel
    public GameBoardPanel(Game game) {
        this.game = game;
        clickedChess = null;
        this.setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    // MODIFIES: this
    // EFFECTS: paint this game board panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGameBoard(g2d);
        drawChessPieces(g2d, game);
        highlightBoard(g2d);
    }

    // MODIFIES: this
    // EFFECTS: assign given cp to clickedChess
    public void setClickedChess(ChessPiece cp) {
        clickedChess = cp;
    }

    // EFFECTS: returns clickedChess
    public ChessPiece getClickedChess() {
        return clickedChess;
    }

    // MODIFIES: this
    // EFFECTS: create the graphic of a chess board
    public void drawGameBoard(Graphics2D g) {
        g.setColor(DARK_SQUARE_COLOUR);
        g.drawRect(0, 0, 800, 800);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                drawEvenColumns(g, i);
            } else {
                drawOddColumns(g, i);
            }
        }
        g.setColor(LIGHT_SQUARE_COLOUR);
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                drawOddColumns(g, i);
            } else {
                drawEvenColumns(g, i);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draw squares on even columns in row i
    public void drawEvenColumns(Graphics2D g, int i) {
        for (int j = 0; j < 4; j++) {
            g.fillRect(i * 100, j * 200 + 100, 100, 100);
        }
    }

    // MODIFIES: this
    // EFFECTS: draw squares on odd columns in row i
    public void drawOddColumns(Graphics2D g, int i) {
        for (int j = 0; j < 4; j++) {
            g.fillRect(i * 100, j * 200, 100, 100);
        }
    }

    // MODIFIES: this
    // EFFECTS: draw chess pieces of given game on chess board
    public void drawChessPieces(Graphics2D g, Game game) {
        g.setStroke(new BasicStroke(PEN_THICKNESS));
        ArrayList<ChessPiece> cps = game.getBoard().getOnBoard();
        for (ChessPiece cp : cps) {
            if (cp != null) {
                int x = 100 * (cp.getPosX() - 1);
                int y = 100 * (cp.getPosY() - 1);
                drawChess(g, x, y, cp);
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: draw given chess on given x, y position
    public void drawChess(Graphics2D g, int x, int y, ChessPiece cp) {
        Color outline;
        Color fill;
        if (cp.getColour().equals("white")) {
            outline = Color.BLACK;
            fill = Color.WHITE;
        } else {
            outline = Color.WHITE;
            fill = Color.BLACK;
        }
        if (cp instanceof Pawn) {
            drawPawn(g, x, y, outline, fill);
        } else if (cp instanceof Rook) {
            drawRook(g, x, y, outline, fill);
        } else if (cp instanceof Knight) {
            drawKnight(g, x, y, outline, fill);
        } else if (cp instanceof Bishop) {
            drawBishop(g, x, y, outline, fill);
        } else if (cp instanceof Queen) {
            drawQueen(g, x, y, outline, fill);
        } else {
            drawKing(g, x, y, outline, fill);
        }
    }

    // MODIFIES: this
    // EFFECTS: draw a pawn at x, y with given colour information
    public void drawPawn(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] bottomX = new int[]{x + 30, x + 50, x + 70};
        int[] bottomY = new int[]{y + 90, y + 25, y + 90};
        g.setColor(outline);
        g.drawPolygon(bottomX, bottomY, 3);
        g.setColor(fill);
        g.fillPolygon(bottomX, bottomY, 3);
        g.setColor(outline);
        g.drawOval(x + 35, y + 20, 30, 30);
        g.setColor(fill);
        g.fillOval(x + 35, y + 20, 30, 30);
    }

    // MODIFIES: this
    // EFFECTS: draw a rook at x, y with given colour information
    public void drawRook(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] topRookX = new int[]{x + 25, x + 35, x + 35, x + 45, x + 45, x + 55, x + 55, x + 65, x + 65,
                x + 75, x + 75, x + 25};
        int[] topRookY = new int[]{y + 15, y + 15, y + 25, y + 25, y + 15, y + 15, y + 25, y + 25, y + 15,
                y + 15, y + 35, y + 35};
        g.setColor(outline);
        g.drawRect(x + 30, y + 30, 40, 45);
        g.setColor(fill);
        g.fillRect(x + 30, y + 30, 40, 45);
        g.setColor(outline);
        g.drawPolygon(topRookX, topRookY, 12);
        g.drawRect(x + 20, y + 75, 60, 15);
        g.setColor(fill);
        g.fillPolygon(topRookX, topRookY, 12);
        g.fillRect(x + 20, y + 75, 60, 15);
    }

    // MODIFIES: this
    // EFFECTS: draw a knight at x, y with given colour information
    public void drawKnight(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] horseX = new int[]{x + 22, x + 40, x + 60, x + 85, x + 75, x + 60, x + 78};
        int[] horseY = new int[]{y + 90, y + 12, y + 12, y + 47, y + 57, y + 52, y + 90};
        g.setColor(outline);
        g.drawPolygon(horseX, horseY, 7);
        g.setColor(fill);
        g.fillPolygon(horseX, horseY, 7);
        g.setColor(outline);
        g.drawRect(x + 20, y + 75, 60, 15);
        g.fillOval(x + 50, y + 25, 10, 10);
        g.setColor(fill);
        g.fillRect(x + 20, y + 75, 60, 15);
    }

    // MODIFIES: this
    // EFFECTS: draw a bishop at x, y with given colour information
    public void drawBishop(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] tipX = new int[]{x + 41, x + 50, x + 59};
        int[] tipY = new int[]{y + 20, y + 10, y + 20};
        int[] midX = new int[]{x + 45, x + 55, x + 67, x + 33};
        int[] midY = new int[]{y + 35, y + 35, y + 48, y + 48};
        int[] bottomX = new int[]{x + 33, x + 67, x + 75, x + 25};
        int[] bottomY = new int[]{y + 75, y + 75, y + 90, y + 90};
        g.setColor(outline);
        g.drawRect(x + 36, y + 48, 28, 25);
        g.setColor(fill);
        g.fillRect(x + 35, y + 50, 30, 25);
        g.setColor(outline);
        g.drawPolygon(midX, midY, 4);
        g.setColor(fill);
        g.fillPolygon(midX, midY, 4);
        g.setColor(outline);
        g.drawPolygon(tipX, tipY, 3);
        g.drawOval(x + 37, y + 16, 26, 26);
        g.drawPolygon(bottomX, bottomY, 4);
        g.setColor(fill);
        g.fillPolygon(tipX, tipY, 3);
        g.fillOval(x + 37, y + 16, 26, 26);
        g.fillPolygon(bottomX, bottomY, 4);
    }

    // MODIFIES: this
    // EFFECTS: draw a queen at x, y with given colour information
    public void drawQueen(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] crownX = new int[]{x + 20, x + 35, x + 40, x + 50, x + 60, x + 65, x + 80, x + 65, x + 35};
        int[] crownY = new int[]{y + 20, y + 25, y + 15, y + 25, y + 15, y + 25, y + 20, y + 40, y + 40};
        int[] bottomX = new int[]{x + 30, x + 70, x + 80, x + 20};
        int[] bottomY = new int[]{y + 75, y + 75, y + 90, y + 90};
        g.setColor(outline);
        g.drawOval(x + 45, y + 10, 10, 10);
        g.drawRect(x + 35, y + 50, 30, 25);
        g.setColor(fill);
        g.fillOval(x + 45, y + 10, 10, 10);
        g.fillRect(x + 35, y + 50, 30, 25);
        g.setColor(outline);
        g.drawRect(x + 35, y + 40, 30, 10);
        g.drawPolygon(bottomX, bottomY, 4);
        g.setColor(fill);
        g.fillRect(x + 35, y + 40, 30, 10);
        g.fillPolygon(bottomX, bottomY, 4);
        g.setColor(outline);
        g.drawPolygon(crownX, crownY, 9);
        g.setColor(fill);
        g.fillPolygon(crownX, crownY, 9);
    }

    // MODIFIES: this
    // EFFECTS: draw a king at x, y with given colour information
    public void drawKing(Graphics2D g, int x, int y, Color outline, Color fill) {
        int[] crownX = new int[]{x + 20, x + 30, x + 40, x + 60, x + 70, x + 80, x + 75, x + 65, x + 35, x + 25};
        int[] crownY = new int[]{y + 35, y + 30, y + 35, y + 35, y + 30, y + 35, y + 50, y + 55, y + 55, y + 50};
        int[] crossX = new int[]{x + 45, x + 55, x + 55, x + 60, x + 60, x + 55, x + 55, x + 45, x + 45, x + 40, x + 40,
                x + 45};
        int[] crossY = new int[]{y + 10, y + 10, y + 15, y + 15, y + 25, y + 25, y + 30, y + 30, y + 25, y + 25, y + 15,
                y + 15};
        int[] bottomX = new int[]{x + 30, x + 70, x + 80, x + 20};
        int[] bottomY = new int[]{y + 75, y + 75, y + 90, y + 90};
        g.setColor(outline);
        g.drawPolygon(crossX, crossY, 12);
        g.drawRect(x + 35, y + 55, 30, 20);
        g.setColor(fill);
        g.fillPolygon(crossX, crossY, 12);
        g.fillRect(x + 35, y + 55, 30, 20);
        g.setColor(outline);
        g.drawPolygon(crownX, crownY, 10);
        g.drawPolygon(bottomX, bottomY, 4);
        g.setColor(fill);
        g.fillPolygon(crownX, crownY, 10);
        g.fillPolygon(bottomX, bottomY, 4);
    }

    // MODIFIES: this
    // EFFECTS: highlight the clicked chess and its possible moves
    public void highlightBoard(Graphics2D g) {
        if (clickedChess != null) {
            int x = clickedChess.getPosX();
            int y = clickedChess.getPosY();
            highlightSquare(g, x, y, SQUARE_COLOUR);
            ArrayList<Position> moves = clickedChess.possibleMoves(game);
            for (Position pos : moves) {
                int posX = pos.getPosX();
                int posY = pos.getPosY();
                highlightSquare(g, posX, posY, MOVE_COLOUR);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: highlight square with x, y position on game board with given colour
    public void highlightSquare(Graphics2D g, int x, int y, Color c) {
        int topLeftX = 100 * (x - 1) + 5;
        int topLeftY = 100 * (y - 1) + 5;
        g.setStroke(new BasicStroke(PEN_HIGHLIGHT));
        g.setColor(c);
        g.drawRect(topLeftX, topLeftY, 90, 90);
    }
}
