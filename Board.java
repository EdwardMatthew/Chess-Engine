package com.company;

import javax.swing.*;

public class Board {
    // the implementation of the board
    // the king is labeled as A because the knight is labeled as k
    // the commas label an empty square in the board
    static String[][] chessBoard ={
            {"r","k","b","q","a","b","k","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," ","P"," "},
            {" "," "," ","A"," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","K","B","Q","A","B","K","R"}};

    // this "global" variable keeps the record of both of the kings
    // for preventing illegal moves
    // say the bishop is pinning the pawn to the king, the pawn would not be able to move because of the check
    static int kingPositionC, kingPositionL;
    public static void main(String[] args) {
//        JFrame f = new JFrame("Chess Engine");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        UserInterface ui = new UserInterface();
//        f.add(ui);
//        f.setSize(500, 500);
//        f.setVisible(true);
        System.out.println(possibleMoves());
    }

    public static String possibleMoves() {
        String list = "";
        for (int i = 0; i < 64; i++) {
            // looping through all of the arrays
            switch (chessBoard[i/8][i%8]) {
                case "P":
                    list+=possibleP(i);
                    break;
                case "R":
                    list+=possibleR(i);
                    break;
                case "K":
                    list+=possibleK(i);
                    break;
                case "B":
                    list+=possibleB(i);
                    break;
                case "Q":
                    list+=possibleQ(i);
                    break;
                case "A":
                    list+=possibleA(i);
                    break;
            }
        }
        return list; //x1,y1,x2,y2, captured piece
    }

    public static String possibleP(int i) {
        String list="";
        return list;
    }
    public static String possibleR(int i) {
        String list="";
        return list;
    }
    public static String possibleK(int i) {
        String list="";
        return list;
    }
    public static String possibleB(int i) {
        String list="";
        return list;
    }
    public static String possibleQ(int i) {
        String list="";
        return list;
    }
    public static String possibleA(int i) {
        String list="";
        String oldPiece;
        int r= i/8, c=i%8;
        // j < 9 because the king has 8 possible squares to go to
        for (int j = 0; j < 9; j++) {
            //  4 being the king position right at that moment
            // also adding error checking if out of bounds
            if (j != 4) {
                try {
                    if (Character.isLowerCase(chessBoard[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) ||
                            " ".equals(chessBoard[r-1 + j/3][c-1 + j%3])) {
                        oldPiece = chessBoard[r - 1 + j / 3][c - 1 + j % 3];
                        chessBoard[r][c] = " ";
                        chessBoard[r-1 + j/3][c-1 + j%3] = "A";
                        int kingTemp = kingPositionC; // recording the original position of the king
                        // update the king position
                        kingPositionC = i + (j / 3) * 8 + j % 3 - 9;

                        // checking if the king is in check or not
                        if (notCheck()) {
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                        }
                        chessBoard[r][c] = "A";
                        chessBoard[r-1 + j/3][c-1 + j%3] = oldPiece;
                        kingPositionC = kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }

    // checking if the king is in check or not
    public static boolean notCheck() {
        return true;
    }
}
