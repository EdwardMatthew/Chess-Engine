package com.chess.engine.player;

import com.chess.engine.Color;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean inCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opposingMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,
                          calculateKingCastling(legalMoves, opposingMoves)));
        // does the opponent move put the current player in check
        // passing the kings position and the opposing moves
        // if opposing move overlaps with the current player's king position, king isCheck
        this.inCheck = !Player.findAttackOnSquare(this.playerKing.getPiecePosition(), opposingMoves).isEmpty();
    }

    protected static Collection<Move> findAttackOnSquare(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMove = new ArrayList<>();
        for (final Move move : moves) {
            if (piecePosition == move.getDestinationPosition()) {
                attackMove.add(move);
            }
        }
        return ImmutableList.copyOf(attackMove);
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
    public boolean isInCheck() {
        return this.inCheck;
    }

    public boolean isCheckMate() {
        return this.inCheck && !hasSafeMoves();
    }

    protected boolean hasSafeMoves() {
        for (final Move move : this.legalMoves) {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public boolean isStalemate() {
        return !this.inCheck && !hasSafeMoves();
    }

    // checking if player has castled or not
    public boolean isCastled() {
        return false;
    }

    public MoveTransition makeMove(final Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttack = Player.findAttackOnSquare(transitionBoard.currentPlayer().
                getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttack.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Color getColor();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastling(Collection<Move> playerLegals,
                                                              Collection<Move> opponentLegals);
}
