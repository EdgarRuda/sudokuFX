package com.sudoku.gameService;

import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ioService {
    private final int N =9;

    public void printBoard(int[][] board) {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

    }

    public void printBoardAsString(int[][] board) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                s.append(board[i][j]);
        System.out.println(s);
    }

    public  String getBoardAsString(int[][] board) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                s.append(board[i][j]);
        return s.toString();
    }

    public void fillExportBox(TextField exportBox, int[][] board){
        exportBox.setText(getBoardAsString(board));
    }

    public void copyString(TextField exportBox){
        Toolkit.getDefaultToolkit().
                getSystemClipboard().
                setContents(
                        new StringSelection(exportBox.getText())
                        , null);
    }




}
