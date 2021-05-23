package com.chess.engine.board;

public class Utilities {
    // values breaking the algorithm in each column
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    // constants to replace magic numbers
    public static final int NUM_SQUARES = 64;
    public static final int NUM_SQUARES_PER_ROW = 8;
    private Utilities() {
        throw new RuntimeException("Cannot be initialized. Utilities only");
    }

    // check if the position is in the chessboard
    public static boolean isValidSquarePosition(final int position) {
        return position >= 0 && position < 64;
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[64]; // being the number of the squares in a chessboard

        // initialize the column to true
        do {
            column[columnNumber] = true;
            columnNumber += NUM_SQUARES_PER_ROW;
        } while (columnNumber < NUM_SQUARES);
        return column;
    }
}

