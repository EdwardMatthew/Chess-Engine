package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Square;
import com.chess.engine.board.Utilities;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final JFrame gameWindow;
    private final GameHistory gameHistory;
    private final TakenPieces takenPieces;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private Board chessBoard;

    private Square sourceSquare;
    private Square destinationSquare;
    private Piece humanPiece;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    private final Color lightSquare = new Color(227,193,111);
    private final Color darkSquare = new Color(184, 139, 74);

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension SQUARE_PANEL_DIMENSION = new Dimension(10, 10);

    private static String defaultPieceImageDir = "art/simple/";

    public Table() {
        this.gameWindow = new JFrame("Chess");
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameWindow.setLayout(new BorderLayout());
        this.gameWindow.setJMenuBar(tableMenuBar);
        this.gameWindow.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistory = new GameHistory();
        this.takenPieces = new TakenPieces();

        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameWindow.add(takenPieces, BorderLayout.WEST);
        this.gameWindow.add(gameHistory, BorderLayout.EAST);
        this.gameWindow.add(boardPanel, BorderLayout.CENTER);

        this.gameWindow.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferenceMenu());
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

    private JMenu createPreferenceMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);

        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves",
                false);

        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });

        preferencesMenu.add(legalMoveHighlighterCheckbox);

        return preferencesMenu;
    }

    public enum BoardDirection {
        NORMAL {
            @Override
            List<SquarePanel> traverse(final List<SquarePanel> boardSquares) {
                return boardSquares;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<SquarePanel> traverse(final List<SquarePanel> boardSquares) {
                return Lists.reverse(boardSquares);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<SquarePanel> traverse(final List<SquarePanel> boardSquares);
        abstract BoardDirection opposite();
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

        public void drawBoard(final Board board) {
            removeAll();
            for (final SquarePanel squarePanel : boardDirection.traverse(boardSquares)) {
                squarePanel.drawSquare(board);
                add(squarePanel);
            }
            validate();
            repaint();
        }
    }


    // keeps track of the moves executed
    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
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
            setChessPiece(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
                    if (isRightMouseButton(event)) {
                        sourceSquare = null;
                        destinationSquare = null;
                        humanPiece = null;
                    } else if (isLeftMouseButton(event)) {
                        if(sourceSquare == null) {
                            sourceSquare = chessBoard.getSquare(squareId);
                            humanPiece = sourceSquare.getPiece();
                            if (humanPiece == null) {
                                sourceSquare = null;
                            }
                        } else {
                            destinationSquare = chessBoard.getSquare(squareId);
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceSquare.getSquarePosition(),
                                    destinationSquare.getSquarePosition());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceSquare = null;
                            destinationSquare = null;
                            humanPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                gameHistory.redo(chessBoard, moveLog);
                                takenPieces.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

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
                try {
                    // name every piece on the file and concatenate .gif on it
                    // also will return the color of the piece
                    // e.g. WhiteBishop -> W
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImageDir +
                            board.getSquare(this.squareId).getPiece().getPieceColor().
                                    toString().substring(0, 1) + board.getSquare(this.squareId).getPiece().toString()
                                    + ".gif"));
                    this.add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void highlightLegals(final Board board) {
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    MoveTransition transition = board.currentPlayer().makeMove(move);
                    if(!transition.getMoveStatus().isDone()) {
                        continue;
                    }
                    if (move.getDestinationPosition() == this.squareId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanPiece != null && humanPiece.getPieceColor() == board.currentPlayer().getColor()) {
                return humanPiece.findLegalMove(board);
            }
            return Collections.emptyList();
        }

        public void drawSquare(final Board board) {
            setSquareColor();
            setChessPiece(board);
            highlightLegals(board);
            validate();
            repaint();
        }
    }
}
