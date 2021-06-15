package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final JFrame gameWindow;
    private final BoardPanel boardPanel;
    private final Color lightSquare = new Color(227,193,111);
    private final Color darkSquare = new Color(184, 139, 74);

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension SQUARE_PANEL_DIMENSION = new Dimension(10, 10);

    public Table() {
        this.gameWindow = new JFrame("Chess");
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameWindow.setLayout(new BorderLayout());
        this.gameWindow.setJMenuBar(tableMenuBar);
        this.gameWindow.setSize(OUTER_FRAME_DIMENSION);

        this.boardPanel = new BoardPanel();
        this.gameWindow.add(boardPanel, BorderLayout.CENTER);

        this.gameWindow.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File"); // standard file format for games
        openPGN.addActionListener(e -> System.out.println("Opening PGN file"));
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    // making the gui for the board
    private class BoardPanel extends JPanel {
        final List<SquarePanel> boardSquares;

        BoardPanel() {
            super(new GridLayout(8,8));
            this.boardSquares = new ArrayList<>();
            for (int i = 0; i < Utilities.NUM_SQUARES; i++) {
                final SquarePanel squarePanel = new SquarePanel(this, i);
                this.boardSquares.add(squarePanel);
                add(squarePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }
    }

    // making the gui for the squares
    private class SquarePanel extends JPanel {
        private final int squareId;

        SquarePanel(final BoardPanel boardPanel, final int squareId) {
            super(new GridBagLayout());
            this.squareId = squareId;
            setPreferredSize(SQUARE_PANEL_DIMENSION);
            setSquareColor();
            validate();
        }

        private void setSquareColor() {
            if (Utilities.EIGHT_RANK[this.squareId] ||
                    Utilities.SIXTH_RANK[this.squareId] ||
                    Utilities.FOURTH_RANK[this.squareId] ||
                    Utilities.SECOND_RANK[this.squareId]) {
                setBackground(this.squareId % 2 == 0 ? lightSquare : darkSquare);
            } else if (Utilities.SEVENTH_RANK[this.squareId] ||
                    Utilities.FIFTH_RANK[this.squareId] ||
                    Utilities.THIRD_RANK[this.squareId] ||
                    Utilities.FIRST_RANK[this.squareId]) {
                setBackground(this.squareId % 2 == 0 ? darkSquare : lightSquare);
            }
        }

        private void setChessPiece(final Board board) {
            this.removeAll();
            if (board.getSquare(this.squareId).isSquareFilled()) {
                String pieceIconPath = "";
                try {
                    // name every piece on the file and concatenate .gif on it
                    // also will return the color of the piece
                    // e.g. WhiteBishop -> W
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath +
                            board.getSquare(this.squareId).getPiece().getPieceColor().
                                    toString().substring(0, 1) + board.getSquare(this.squareId).getPiece().toString()
                                    + ".gif"));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}