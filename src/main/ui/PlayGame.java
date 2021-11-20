package ui;

import model.*;
import persistence.JsonWriter;
import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class PlayGame extends JPanel implements MouseListener {

    private static final String JSON_STORE = "./data/chessGame.json";
    private static final Font STANDARD_FONT = new Font("Standard", Font.BOLD, 20);
    private static final Font SMALL_BUTTON_FONT = new Font("SmallButton", Font.BOLD, 15);
    private static final Font LABEL_FONT = new Font("Label", Font.BOLD, 30);
    private static final Color BACKGROUND_GAME = new Color(255, 255, 230);
    private static final Color BACKGROUND_OTHER = new Color(255, 255, 255);
    private static final Color SELECT = new Color(204, 255, 255);
    private static final Color UNSELECT = new Color(224, 240, 255);

    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private GameBoardPanel gamePanel;
    private JLabel label;
    private JLabel message;
    private JButton start;
    private JButton load;
    private JButton exit;
    private JButton regular;
    private JButton create;
    private JButton back;
    private JButton yes;
    private JButton no;
    private JButton undo;
    private JButton draw;
    private JButton save;
    private JButton leave;
    private JButton king;
    private JButton queen;
    private JButton bishop;
    private JButton knight;
    private JButton rook;
    private JButton pawn;
    private JButton white;
    private JButton black;
    private JButton remove;
    private JButton done;
    private Boolean promote;
    private Boolean creating;
    private Boolean removing;
    private String chessTypeSelected;
    private String chessColourSelected;

    // EFFECTS: constructs a chess game
    public PlayGame() {
        game = new Game();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        JFrame frame = new JFrame();
        frame.setSize(1200, 1100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Chess");
        frame.add(this);
        setLayout(null);
        label = new JLabel("");
        message = new JLabel("");
        createStartButton();
        createLoadButton();
        createExitButton();
        paintHomePage();
        frame.setVisible(true);
        promote = false;
        creating = false;
        removing = false;
        chessTypeSelected = "";
        chessColourSelected = "";
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** MOUSE LISTENER METHODS */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // MODIFIES: this
    // EFFECTS: add a mouse listener to game panel
    @Override
    public void mouseClicked(MouseEvent e) {
        if (creating) {
            handleMouseCreateAction(e);
        } else if (!game.hasEnded() && !promote) {
            handleMouseMoveAction(e);
        }
    }

    // EFFECTS: method needed to be overridden from MouseListener interface. No implementation because nothing should
    //          happen when mouse is pressed
    @Override
    public void mousePressed(MouseEvent e) {

    }

    // EFFECTS: method needed to be overridden from MouseListener interface. No implementation because nothing should
    //          happen when mouse is released
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    // EFFECTS: method needed to be overridden from MouseListener interface. No implementation because nothing should
    //          happen when mouse enters
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    // EFFECTS: method needed to be overridden from MouseListener interface. No implementation because nothing should
    //          happen when mouse exits
    @Override
    public void mouseExited(MouseEvent e) {

    }

    // MODIFIES: this
    // EFFECTS: handle given mouse click during a game to move chess pieces
    public void handleMouseMoveAction(MouseEvent e) {
        int clickedX = roundUpHundred(e.getX()) / 100;
        int clickedY = roundUpHundred(e.getY()) / 100;
        Position pos = new Position(clickedX, clickedY);
        int posIndex = pos.toSingleValue() - 1;
        ChessPiece cp = game.getBoard().getOnBoard().get(posIndex);
        if (cp != null && cp.getColour().equals(game.getTurn())) {
            gamePanel.setClickedChess(cp);
        } else if (gamePanel.getClickedChess() != null) {
            int beginX = gamePanel.getClickedChess().getPosX();
            int beginY = gamePanel.getClickedChess().getPosY();
            handleMoveInfo(beginX, beginY, clickedX, clickedY);
            gamePanel.setClickedChess(null);
        } else {
            gamePanel.setClickedChess(null);
        }
        gamePanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: handle given mouse click when creating a game to add chess pieces
    public void handleMouseCreateAction(MouseEvent e) {
        if (colourValid(chessColourSelected) && typeValid(chessTypeSelected)) {
            remove(message);
            int clickedX = roundUpHundred(e.getX()) / 100;
            int clickedY = roundUpHundred(e.getY()) / 100;
            proceedAdd(chessColourSelected, chessTypeSelected, clickedX, clickedY);
            repaint();
        } else if (removing) {
            int clickedX = roundUpHundred(e.getX()) / 100;
            int clickedY = roundUpHundred(e.getY()) / 100;
            removeChessActon(clickedX, clickedY);
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

    // REQUIRES: n must be positive
    // EFFECTS: round up given integer to the nearest hundreds (e.g. 99 -> 100, 101 -> 200)
    public int roundUpHundred(int n) {
        int round = n;
        while (round % 100 != 0) {
            round++;
        }
        return round;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** HOME PAGE, START_OPTION PAGE, EXIT_CONFIRMATION PAGE METHODS */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // MODIFIES: this
    // EFFECTS: draws the home page of this application.
    public void paintHomePage() {
        removeAllComponents();
        URL image = PlayGame.class.getClassLoader().getResource("chessTitle2.png");
        ImageIcon title = new ImageIcon(image);
        label = new JLabel(title);
        label.setBounds(100, 70, 800, 250);
        start.setBounds(300, 270, 400, 100);
        load.setBounds(300, 420, 400, 100);
        exit.setBounds(300, 570, 400, 100);
        this.add(label);
        this.add(start);
        this.add(load);
        this.add(exit);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: creates the start button on home page. When pressed, draw the start option page
    public void createStartButton() {
        start = new JButton("Start");
        start.setFont(STANDARD_FONT);
        start.addActionListener(e -> {
                    removeAllComponents();
                    createRegularButton();
                    createCreateButton();
                    createBackButton();
                    regular.setBounds(300, 300, 400, 100);
                    create.setBounds(300, 500, 400, 100);
                    back.setBounds(10, 10, 100, 50);
                    add(regular);
                    add(create);
                    add(back);
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the load button on the home page. When pressed, load saved game
    public void createLoadButton() {
        load = new JButton("Load");
        load.setFont(STANDARD_FONT);
        load.addActionListener(e -> {
                    try {
                        game = jsonReader.readGame();
                        paintGamePage();
                    } catch (IOException exception) {
                        System.out.println("Unable to read from file: " + JSON_STORE);
                    }
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the exit button on the home page. When pressed draw the exit confirmation page
    public void createExitButton() {
        exit = new JButton("Exit");
        exit.setFont(STANDARD_FONT);
        exit.addActionListener(e -> {
                    removeAllComponents();
                    createExitYesButton();
                    createExitNoButton();
                    label = new JLabel("Are you sure to exit?");
                    label.setFont(LABEL_FONT);
                    label.setBounds(200, 100, 400, 100);
                    yes.setBounds(300, 300, 400, 100);
                    no.setBounds(300, 500, 400, 100);
                    add(label);
                    add(yes);
                    add(no);
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the regular button on the start option page. When pressed, start a regular game
    public void createRegularButton() {
        regular = new JButton("Regular Game");
        regular.setFont(STANDARD_FONT);
        regular.addActionListener(e -> {
                    game = new Game();
                    paintGamePage();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the create button on the start option page. When pressed, draw the create game page
    public void createCreateButton() {
        create = new JButton("Create Your Game");
        create.setFont(STANDARD_FONT);
        create.addActionListener(e -> {
                    creating = true;
                    game = new Game(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                            new Board(), new ArrayList<>(), "white", false);
                    paintCreateGamePage();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the back button on the start option page. When pressed, go back to home page
    public void createBackButton() {
        back = new JButton("Back");
        back.addActionListener(e -> {
                    paintHomePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the yes button on the exit confirmation page. When pressed, exit program
    public void createExitYesButton() {
        yes = new JButton("Yes");
        yes.setFont(STANDARD_FONT);
        yes.addActionListener(e -> System.exit(0));
    }

    // MODIFIES: this
    // EFFECTS: creates the no button on the exit confirmation page. When pressed, go back to home page
    public void createExitNoButton() {
        no = new JButton("No");
        no.setFont(STANDARD_FONT);
        no.addActionListener(e -> {
                    paintHomePage();
                    repaint();
                }
        );
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** CREATE GAME PAGE METHODS */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // MODIFIES: this
    // EFFECTS: creates the create game page. User will create their own game on this page
    public void paintCreateGamePage() {
        removeAllComponents();
        setBackground(BACKGROUND_GAME);
        gamePanel = new GameBoardPanel(game);
        gamePanel.setBounds(100, 80, 800, 800);
        add(gamePanel);
        addColourOptions();
        addChessOptions();
        addRemoveAndDone();
        gamePanel.addMouseListener(this);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: add chess colour options on to the create game page (black, white).
    public void addColourOptions() {
        createWhiteButton();
        createBlackButton();
        colourSetUnselectBG();
        JLabel colourLabel = new JLabel("Select a colour");
        colourLabel.setFont(STANDARD_FONT);
        colourLabel.setBounds(930, 80, 200, 40);
        white.setBounds(950, 120, 100, 50);
        black.setBounds(950, 200, 100, 50);
        white.setFont(SMALL_BUTTON_FONT);
        black.setFont(SMALL_BUTTON_FONT);
        add(white);
        add(black);
        add(colourLabel);
    }

    // MODIFIES: this
    // EFFECTS: add chess type options on to the create game page (king, queen, bishop, knight, rook, pawn)
    public void addChessOptions() {
        createSetKingButton();
        createSetQueenButton();
        createSetBishopButton();
        createSetKnightButton();
        createSetRookButton();
        createSetPawnButton();
        chessSetUnselectBG();
        JLabel chessLabel = new JLabel("Select a chess");
        chessLabel.setFont(STANDARD_FONT);
        chessLabel.setBounds(930, 310, 200, 40);
        king.setBounds(950, 350, 100, 50);
        queen.setBounds(950, 430, 100, 50);
        bishop.setBounds(950, 510, 100, 50);
        knight.setBounds(950, 590, 100, 50);
        rook.setBounds(950, 670, 100, 50);
        pawn.setBounds(950, 750, 100, 50);
        setFontThenAddChess();
        add(chessLabel);
    }

    // REQUIRES: king, queen, bishop, knight, rook, and pawn buttons must be initialized
    // MODIFIES: this
    // EFFECTS: set font for the six chess type buttons, then add them on to the create game page
    public void setFontThenAddChess() {
        add(king);
        add(queen);
        add(bishop);
        add(knight);
        add(rook);
        add(pawn);
        king.setFont(SMALL_BUTTON_FONT);
        queen.setFont(SMALL_BUTTON_FONT);
        bishop.setFont(SMALL_BUTTON_FONT);
        knight.setFont(SMALL_BUTTON_FONT);
        rook.setFont(SMALL_BUTTON_FONT);
        pawn.setFont(SMALL_BUTTON_FONT);
    }

    // MODIFIES: this
    // EFFECTS: add the remove button and done button on to the create game page
    public void addRemoveAndDone() {
        createRemoveButton();
        createDoneButton();
        remove.setBackground(UNSELECT);
        done.setBackground(UNSELECT);
        remove.setBounds(100, 900, 100, 50);
        done.setBounds(250, 900, 100, 50);
        remove.setFont(SMALL_BUTTON_FONT);
        done.setFont(SMALL_BUTTON_FONT);
        add(remove);
        add(done);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: set background of all colour option buttons to the colour UNSELECT
    public void colourSetUnselectBG() {
        white.setBackground(UNSELECT);
        black.setBackground(UNSELECT);
    }

    // MODIFIES: this
    // EFFECTS: set background of all chess type option buttons to the colour UNSELECT
    public void chessSetUnselectBG() {
        king.setBackground(UNSELECT);
        queen.setBackground(UNSELECT);
        bishop.setBackground(UNSELECT);
        knight.setBackground(UNSELECT);
        rook.setBackground(UNSELECT);
        pawn.setBackground(UNSELECT);
    }

    // MODIFIES: this
    // EFFECTS: create the white button on the create game page. When pressed, user will be adding a white chess
    public void createWhiteButton() {
        white = new JButton("White");
        white.addActionListener(e -> {
                    colourSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    white.setBackground(SELECT);
                    chessColourSelected = "white";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the black button on the create game page. When pressed, user will be adding a black chess
    public void createBlackButton() {
        black = new JButton("Black");
        black.addActionListener(e -> {
                    colourSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    black.setBackground(SELECT);
                    chessColourSelected = "black";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the king button on the create game page. When pressed, user will be adding a king
    public void createSetKingButton() {
        king = new JButton("King");
        king.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    king.setBackground(SELECT);
                    chessTypeSelected = "king";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the queen button on the create game page. When pressed, user will be adding a queen
    public void createSetQueenButton() {
        queen = new JButton("Queen");
        queen.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    queen.setBackground(SELECT);
                    chessTypeSelected = "queen";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the bishop button on the create game page. When pressed, user will be adding a bishop
    public void createSetBishopButton() {
        bishop = new JButton("Bishop");
        bishop.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    bishop.setBackground(SELECT);
                    chessTypeSelected = "bishop";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the knight button on the create game page. When pressed, user will be adding a knight
    public void createSetKnightButton() {
        knight = new JButton("Knight");
        knight.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    knight.setBackground(SELECT);
                    chessTypeSelected = "knight";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the rook button on the create game page. When pressed, user will be adding a rook
    public void createSetRookButton() {
        rook = new JButton("Rook");
        rook.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    rook.setBackground(SELECT);
                    chessTypeSelected = "rook";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the pawn button on the create game page. When pressed, user will be adding a pawn
    public void createSetPawnButton() {
        pawn = new JButton("Pawn");
        pawn.addActionListener(e -> {
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    pawn.setBackground(SELECT);
                    chessTypeSelected = "pawn";
                    removing = false;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the remove button on the create game page. When pressed, user can remove chess pieces
    public void createRemoveButton() {
        remove = new JButton("Remove");
        remove.addActionListener(e -> {
                    remove.setBackground(SELECT);
                    colourSetUnselectBG();
                    chessSetUnselectBG();
                    chessColourSelected = "";
                    chessTypeSelected = "";
                    removing = true;
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: create the done button on the create game page. When pressed, system evaluates whether the created game
    //          is valid
    public void createDoneButton() {
        done = new JButton("Done");
        done.addActionListener(e -> {
                    removing = false;
                    chessColourSelected = "";
                    chessTypeSelected = "";
                    colourSetUnselectBG();
                    chessSetUnselectBG();
                    remove.setBackground(UNSELECT);
                    remove(message);
                    repaint();
                    doneModifyAction();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: evaluates whether the created game is valid (both team must have a king, cannot be an ended game).
    //          shows a message if not valid, proceed to select team page if valid
    public void doneModifyAction() {
        message = new JLabel("");
        message.setFont(STANDARD_FONT);
        message.setForeground(Color.red);
        message.setBounds(100, 10, 800, 70);
        add(message);
        if (!hasKings()) {
            System.out.println("Both teams must have a king!");
            message.setText("Both teams must have a king!");
            repaint();
        } else if (game.hasEnded()) {
            System.out.println("There is a checkmate or stalemate! Keep modifying");
            message.setText("There is a checkmate or stalemate! Keep modifying");
            repaint();
        } else {
            creating = false;
            paintSelectFirstTeamPage();
        }
    }

    // EFFECTS: returns true there is a king on each side
    public boolean hasKings() {
        boolean hasWhiteKing = kingExist("white");
        boolean hasBlackKing = kingExist("black");
        return hasWhiteKing && hasBlackKing;
    }

    // MODIFIES: this
    // REQUIRES: colour must black or white; type must be king, queen, bishop, knight, rook, or pawn; x and y must be
    //           integers in the range 1 to 8
    // EFFECTS: proceed with add chess action with valid colour, type, and coordinate inputs
    public void proceedAdd(String colour, String type, int x, int y) {
        ChessPiece chess = buildChess(colour, type, x, y);
        Position pos = new Position(x, y);
        int index = pos.toSingleValue() - 1;
        message = new JLabel("");
        message.setFont(STANDARD_FONT);
        message.setForeground(Color.red);
        message.setBounds(100, 10, 800, 70);
        add(message);
        if (!Objects.isNull(game.getBoard().getOnBoard().get(index))) {
            System.out.println("Position is already occupied");
            message.setText("Position is already occupied!");
            repaint();
        } else if (type.equals("king") && kingExist(colour)) {
            System.out.println("Cannot have more than one king on the same team");
            message.setText("Cannot have more than one king on the same team!");
            repaint();
        } else if (type.equals("pawn") && (y == 8 || y == 1)) {
            System.out.println("Pawn of this colour cannot arrive to this position");
            message.setText("Pawn cannot arrive to this position!");
            repaint();
        } else {
            chessAdd(chess, colour, type, x, y);
        }
    }


    // MODIFIES: this
    // EFFECTS: removes chess from game board (users can only remove a chess piece when they are constructing their own
    //          game)
    public void removeChessActon(int x, int y) {
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
                repaint();
            }
        }
    }

    // REQUIRES: colour must black or white; type must be king, queen, bishop, knight, rook, or pawn; x and y must be
    //           integers in the range 1 to 8
    // EFFECTS: constructs and returns a chess piece based on given type and colour information
    public ChessPiece buildChess(String colour, String type, int x, int y) {
        ChessPiece cp;
        switch (type) {
            case "king":
                cp = new King(colour, x, y);
                break;
            case "queen":
                cp = new Queen(colour, x, y);
                break;
            case "bishop":
                cp = new Bishop(colour, x, y);
                break;
            case "knight":
                cp = new Knight(colour, x, y);
                break;
            case "rook":
                cp = new Rook(colour, x, y);
                break;
            default:
                cp = new Pawn(colour, x, y);
                break;
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
        for (ChessPiece cp : examined) {
            if (cp instanceof King) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    // MODIFIES: this
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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** CREATE SELECT_FIRST_TEAM PAGE METHODS */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // MODIFIES: this
    // EFFECTS: draws the select first team to move page
    public void paintSelectFirstTeamPage() {
        removeAllComponents();
        setBackground(BACKGROUND_OTHER);
        label = new JLabel("Pick the team to move first");
        label.setBounds(200, 100, 800, 70);
        label.setFont(LABEL_FONT);
        createSelectTeamWhiteButton();
        createSelectTeamBlackButton();
        createSelectTeamBackButton();
        white.setBounds(300, 300, 400, 100);
        black.setBounds(300, 500, 400, 100);
        back.setBounds(10, 10, 100, 50);
        add(white);
        add(black);
        add(back);
        add(label);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: creates the white button on the select first team page. When pressed, try to set white as first team
    //          to move
    public void createSelectTeamWhiteButton() {
        white = new JButton("White");
        white.setFont(STANDARD_FONT);
        white.addActionListener(e -> {
                    remove(message);
                    setFirstTeam("white");
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the black button on the select first team page. When pressed, try to set black as first team
    //          to move
    public void createSelectTeamBlackButton() {
        black = new JButton("Black");
        black.setFont(STANDARD_FONT);
        black.addActionListener(e -> {
                    remove(message);
                    setFirstTeam("black");
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the back button on the select first team page. When pressed, go back to create game page
    public void createSelectTeamBackButton() {
        back = new JButton("Back");
        back.setFont(STANDARD_FONT);
        back.addActionListener(e -> {
                    creating = true;
                    paintCreateGamePage();
                    repaint();
                }
        );
    }

    // REQUIRES: colour must be "white" or "black"
    // EFFECTS: try setting colour as first team to move. If colour is an invalid choice, shows a message
    public void setFirstTeam(String colour) {
        message = new JLabel("");
        message.setFont(STANDARD_FONT);
        message.setForeground(Color.red);
        message.setBounds(300, 200, 800, 70);
        add(message);
        if (colour.equals("black") && game.check("white")) {
            System.out.println("White is checked, white must go first");
            message.setText("White is checked! White must go first");
        } else if (colour.equals("white") && game.check("black")) {
            System.out.println("Black is checked, black must go first");
            message.setText("Black is checked! Black must go first");
        } else if (colour.equals("black")) {
            game.reverseTurn();
            game.setWhiteChessPiecesOffBoard(setOffBoard("white"));
            game.setBlackChessPiecesOffBoard(setOffBoard("black"));
            paintGamePage();
        } else if (colour.equals("white")) {
            game.setWhiteChessPiecesOffBoard(setOffBoard("white"));
            game.setBlackChessPiecesOffBoard(setOffBoard("black"));
            paintGamePage();
        }
        repaint();
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
        for (ChessPiece cp : examine) {
            if (cp instanceof Pawn) {
                numPawn++;
            }
        }
        return game.buildDefaultChessOffBoard(team, numPawn);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** CREATE GAME PAGE METHODS */
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    // MODIFIES: this
    // EFFECTS: create the graphic of the game page
    public void paintGamePage() {
        removeAllComponents();
        setBackground(BACKGROUND_GAME);
        gamePanel = new GameBoardPanel(game);
        gamePanel.addMouseListener(this);
        add(gamePanel);
        gamePanel.setBounds(100, 80, 800, 800);
        createUndoButton();
        createDrawButton();
        createSaveButton();
        createLeaveButton();
        undo.setBounds(100, 900, 100, 50);
        draw.setBounds(250, 900, 100, 50);
        save.setBounds(400, 900, 100, 50);
        leave.setBounds(550, 900, 100, 50);
        add(undo);
        add(draw);
        add(save);
        add(leave);
        printMessage();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: remove all components from this panel
    public void removeAllComponents() {
        while (this.getComponents().length > 0) {
            remove(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates the undo button on the game page. When pressed, undo most recent move
    public void createUndoButton() {
        undo = new JButton("Undo");
        undo.setFont(STANDARD_FONT);
        undo.addActionListener(e -> {
                    if (!promote) {
                        undoAction();
                        gamePanel.setClickedChess(null);
                        paintGamePage();
                        repaint();
                    }
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the draw button on the game page. When pressed, go to draw confirmation page
    public void createDrawButton() {
        draw = new JButton("Draw");
        draw.setFont(STANDARD_FONT);
        draw.addActionListener(e -> {
                    if (!promote) {
                        removeAllComponents();
                        setBackground(BACKGROUND_OTHER);
                        gamePanel.setClickedChess(null);
                        createDrawYesButton();
                        createLeaveDrawNoButton();
                        label = new JLabel("Are you sure to draw the game?");
                        label.setFont(STANDARD_FONT);
                        label.setBounds(200, 100, 800, 100);
                        yes.setBounds(300, 300, 400, 100);
                        no.setBounds(300, 500, 400, 100);
                        add(label);
                        add(yes);
                        add(no);
                        repaint();
                    }
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the save button on the game page. When pressed, save current game board
    public void createSaveButton() {
        save = new JButton("Save");
        save.setFont(STANDARD_FONT);
        save.addActionListener(e -> {
                    if (!promote) {
                        saveGame();
                        gamePanel.setClickedChess(null);
                        repaint();
                    }
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the leave button on the game page. When pressed, go to leave confirmation page
    public void createLeaveButton() {
        leave = new JButton("Leave");
        leave.setFont(STANDARD_FONT);
        leave.addActionListener(e -> {
                    if (!promote) {
                        removeAllComponents();
                        setBackground(BACKGROUND_OTHER);
                        gamePanel.setClickedChess(null);
                        createLeaveYesButton();
                        createLeaveDrawNoButton();
                        label = new JLabel("Are you sure to leave? (Remember to save your game)");
                        label.setFont(LABEL_FONT);
                        label.setBounds(200, 100, 800, 100);
                        yes.setBounds(300, 300, 400, 100);
                        no.setBounds(300, 500, 400, 100);
                        add(label);
                        add(yes);
                        add(no);
                        repaint();
                    }
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the yes button on the leave confirmation page. When pressed, go to home page
    public void createLeaveYesButton() {
        yes = new JButton("Yes");
        yes.setFont(STANDARD_FONT);
        yes.addActionListener(e -> {
                    paintHomePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the no button on the leave or draw confirmation page. When pressed, go back to game page
    public void createLeaveDrawNoButton() {
        no = new JButton("No");
        no.setFont(STANDARD_FONT);
        no.addActionListener(e -> {
                    removeAllComponents();
                    paintGamePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the yes button on the draw confirmation page. When pressed, go back to game page and draw the
    //          game
    public void createDrawYesButton() {
        yes = new JButton("Yes");
        yes.setFont(STANDARD_FONT);
        yes.addActionListener(e -> {
                    removeAllComponents();
                    game.setDrawn(true);
                    paintGamePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: determines whether the given move information is valid, then apply move
    public void handleMoveInfo(int bx, int by, int ex, int ey) {
        if (bx >= 0 && bx <= 8 && by >= 0 && by <= 8 && ex >= 0 && ex <= 8 && ey >= 0 && ey <= 8) {
            Position initialPos = new Position(bx, by);
            int initialIndex = initialPos.toSingleValue() - 1;
            ChessPiece cp = game.getBoard().getOnBoard().get(initialIndex);
            if (!Objects.isNull(cp) && cp.getColour().equals(game.getTurn())) {
                for (Position pos : cp.possibleMoves(game)) {
                    if (pos.getPosX() == ex && pos.getPosY() == ey) {
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
        game.reverseTurn();
        if (cp instanceof King && Math.abs(bx - ex) == 2) {
            castlingMove(cp, bx, by, ex, ey);
        } else if (cp instanceof Pawn && (ey == 1 || ey == 8)) {
            promotionMove(cp, bx, by, ex, ey);
        } else {
            simpleMove(cp, bx, by, ex, ey);
        }
        System.out.println("Move success");
        if (game.check(game.getTurn())) {
            System.out.println(game.getTurn() + " is checked!");
        }
        if (!promote) {
            paintGamePage();
            repaint();
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
        Position rookPos = new Position(rookBeginX, rookBeginY);
        ChessPiece rook = game.getBoard().getOnBoard().get(rookPos.toSingleValue() - 1);
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
        promote = true;
        Moves moves = new Moves();
        Position endPos = new Position(ex, ey);
        ChessPiece attackedEnemy = game.getBoard().getOnBoard().get(endPos.toSingleValue() - 1);
        if (!Objects.isNull(attackedEnemy)) {
            boolean enemyMoveStatus = attackedEnemy.hasMoved();
            game.remove(attackedEnemy);
            moves.addMove(new Move(ex, ey, 0, 0, attackedEnemy, enemyMoveStatus));
        }
        game.move(cp, ex, ey);
        userPromotionResponse(cp, bx, by, ex, ey, moves);
        game.updateHistory(moves);
    }

    // MODIFIES: this
    // EFFECTS: add promotion buttons on the side of the game panel, remove all the other buttons
    public void userPromotionResponse(ChessPiece cp, int bx, int by, int ex, int ey, Moves moves) {
        remove(undo);
        remove(draw);
        remove(save);
        remove(leave);
        createQueenButton(cp, bx, by, ex, ey, moves);
        createBishopButton(cp, bx, by, ex, ey, moves);
        createKnightButton(cp, bx, by, ex, ey, moves);
        createRookButton(cp, bx, by, ex, ey, moves);
        queen.setBounds(950, 100, 150, 100);
        bishop.setBounds(950, 300, 150, 100);
        knight.setBounds(950, 500, 150, 100);
        rook.setBounds(950, 700, 150, 100);
        add(queen);
        add(bishop);
        add(knight);
        add(rook);
        printMessage();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: creates the queen button on game page when pawn should be promoted. If pressed, promote pawn to queen
    public void createQueenButton(ChessPiece cp, int bx, int by, int ex, int ey, Moves moves) {
        queen = new JButton("Queen");
        queen.setFont(STANDARD_FONT);
        queen.addActionListener(e -> {
                    for (Move m : handlePromotionInput(cp, bx, by, ex, ey, "queen").getMoves()) {
                        moves.addMove(m);
                    }
                    promote = false;
                    paintGamePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the bishop button on game page when pawn should be promoted. If pressed, promote pawn to bishop
    public void createBishopButton(ChessPiece cp, int bx, int by, int ex, int ey, Moves moves) {
        bishop = new JButton("Bishop");
        bishop.setFont(STANDARD_FONT);
        bishop.addActionListener(e -> {
                    for (Move m : handlePromotionInput(cp, bx, by, ex, ey, "bishop").getMoves()) {
                        moves.addMove(m);
                    }
                    promote = false;
                    paintGamePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the knight button on game page when pawn should be promoted. If pressed, promote pawn to knight
    public void createKnightButton(ChessPiece cp, int bx, int by, int ex, int ey, Moves moves) {
        knight = new JButton("Knight");
        knight.setFont(STANDARD_FONT);
        knight.addActionListener(e -> {
                    for (Move m : handlePromotionInput(cp, bx, by, ex, ey, "knight").getMoves()) {
                        moves.addMove(m);
                    }
                    promote = false;
                    paintGamePage();
                    repaint();
                }
        );
    }

    // MODIFIES: this
    // EFFECTS: creates the rook button on game page when pawn should be promoted. If pressed, promote pawn to rook
    public void createRookButton(ChessPiece cp, int bx, int by, int ex, int ey, Moves moves) {
        rook = new JButton("Rook");
        rook.setFont(STANDARD_FONT);
        rook.addActionListener(e -> {
                    for (Move m : handlePromotionInput(cp, bx, by, ex, ey, "rook").getMoves()) {
                        moves.addMove(m);
                    }
                    promote = false;
                    paintGamePage();
                    repaint();
                }
        );
    }

    // REQUIRES: pro must be "queen", "bishop", "knight", "rook"
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
            moves.addMove(new Move(bx, by, 0, 0, cp, pawnMoveStatus));
            moves.addMove(new Move(0, 0, ex, ey, addChess, false));
        }
        return moves;
    }

    // MODIFIES: this
    // EFFECTS: perform a simple move with given information
    public void simpleMove(ChessPiece cp, int bx, int by, int ex, int ey) {
        Moves moves = new Moves();
        Position endPos = new Position(ex, ey);
        ChessPiece attackedEnemy = game.getBoard().getOnBoard().get(endPos.toSingleValue() - 1);
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
            promote = false;
        }
    }

    // MODIFIES: this
    // EFFECTS: print the messages in the game, including check message, result message, promote message
    public void printMessage() {
        message = new JLabel("");
        message.setFont(STANDARD_FONT);
        if (promote) {
            message.setText("Promote your Pawn!");
            message.setForeground(Color.BLUE);
        } else if (game.getDrawn() || game.stalemate(game.getTurn())) {
            message.setText("There is a tie!");
            message.setForeground(Color.BLUE);
        } else if (game.checkmate("white")) {
            message.setText("BLACK WINS!!!");
            message.setForeground(Color.GREEN);
        } else if (game.checkmate("black")) {
            message.setText("WHITE WINS!!!");
            message.setForeground(Color.GREEN);
        } else if (game.check("black") || game.check("white")) {
            message.setText(("Checked!"));
            message.setForeground(Color.RED);
        } else {
            System.out.println("Players have left the game.");
        }
        message.setBounds(425, 10, 200, 70);
        add(message);
        repaint();
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