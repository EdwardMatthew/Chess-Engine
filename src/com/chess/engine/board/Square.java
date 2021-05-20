package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;


public abstract class Square {
    // immutability enhancing -> once set in the constructor, it can't be mutated
    protected final int squarePosition;

    // caching for the valid empty tile. Looking in this cache for valid empty squares makes it so that
    // we don't have to create a new empty square every time, but we could just look at the squares
    private static final Map<Integer, EmptySquare> EMPTY_SQUARE_CACHE = createAllPossibleEmptySquare();

    private static Map<Integer, EmptySquare> createAllPossibleEmptySquare() {
        final Map<Integer, EmptySquare> emptySquareMap = new HashMap<>();

        for (int i = 0; i < 64; i++) {
            emptySquareMap.put(i, new EmptySquare(i));
        }
        // immutable map of all possible empty square
        // declaring as immutable, so it couldn't be changed
        return ImmutableMap.copyOf(emptySquareMap);
    }

    Square(int squarePosition) {
        this.squarePosition = squarePosition;
    }

    public abstract boolean isSquareFilled();
    public abstract Piece getPiece();

    public static final class EmptySquare extends Square{
        private EmptySquare(int position) {
            super(position);
        }

        // initial square is always initialized as empty
        @Override
        public boolean isSquareFilled() {
            return false;
        }

        // returns null because there is no piece on the square
        @Override
        public Piece getPiece() {
            return null;
        }
    }

    // defining the filled squares
    public static final class filledSquare extends Square {
        // need to call getPiece to get this var, immutability enhancement
        private final Piece pieceOnSquare;

        private filledSquare(int squarePosition, Piece pieceOnSquare) {
            super(squarePosition);
            this.pieceOnSquare = pieceOnSquare;
        }

        // the override method for the filled square
        @Override
        public boolean isSquareFilled() {
            return true;
        }

        // returns the piece on the square
        @Override
        public Piece getPiece() {
            return this.pieceOnSquare;
        }
    }
}
