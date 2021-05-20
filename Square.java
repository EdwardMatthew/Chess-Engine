package com.company;

public abstract class Square {
    int squarePosition;

    Square(int squarePosition) {
        this.squarePosition = squarePosition;
    }

    public abstract boolean isSquareFilled();
    public abstract Piece getPiece();

    public static final class EmptySquare extends Square{
        EmptySquare(int position) {
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
        Piece pieceOnSquare;

        filledSquare(int squarePosition, Piece pieceOnSquare) {
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
