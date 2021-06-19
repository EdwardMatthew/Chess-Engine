package com.chess.engine.board;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class Utilities {
    // values breaking the algorithm in each column
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    // check for pawn spawn tile
    public static final boolean[] EIGHT_RANK = initRows(0);
    public static final boolean[] SEVENTH_RANK = initRows(8);
    public static final boolean[] SIXTH_RANK = initRows(16);
    public static final boolean[] FIFTH_RANK = initRows(24);
    public static final boolean[] FOURTH_RANK = initRows(32);
    public static final boolean[] THIRD_RANK = initRows(40);
    public static final boolean[] SECOND_RANK = initRows(48);
    public static final boolean[] FIRST_RANK = initRows(56);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    // constants to replace magic numbers
    public static final int NUM_SQUARES = 64;
    public static final int NUM_SQUARES_PER_ROW = 8;
    private Utilities() {
        throw new RuntimeException("Cannot be initialized. Utilities only");
    }

    private static String[] initializeAlgebraicNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for (int i = 0; i < NUM_SQUARES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
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

    private static boolean[] initRows(int rowNumber) {
        final boolean[] row = new boolean[NUM_SQUARES];

        // initializing the rows
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber  % NUM_SQUARES_PER_ROW != 0);
        return row;
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    // returns the notation in chess
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }
}

