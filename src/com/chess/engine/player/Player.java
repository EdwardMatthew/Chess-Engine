package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.MoveTransition;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

import java.util.Collection;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opposingMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = legalMoves;
    }

    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        // looping to find the king, if not found throw runtime exception
        throw new RuntimeException("Board not valid");
    }

    // method for calculating if move is legal
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }

    // checking game ending conditions
    public boolean inCheck() {
        return false;
    }

    public boolean isCheckMate() {
        return false;
    }

    public boolean isStalemate() {
        return false;
    }

    // checking if player has castled or not
    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        return null;
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Color getColor();
    public abstract Player getOpponent();
}
