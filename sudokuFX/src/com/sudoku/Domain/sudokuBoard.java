package com.sudoku.Domain;

import java.util.*;

public class sudokuBoard {
    public int getCellId(int r, int c) {
        return boardId[r][c];
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolvedBoard() {return solvedBoard;}

    public boolean isConflict() {return isConflict;}

    public void setConflictStatus(boolean isConflict) {this.isConflict = isConflict;}

    public boolean getCellType(int r, int c) {return statusBoard[r][c][0];}

    public boolean getCellMode(int r, int c) {return statusBoard[r][c][1];}

    public void setCellMode(int r, int c, boolean noteMode) {statusBoard[r][c][1] = noteMode;}

    public boolean getCellConflictStatus(int r, int c) {return statusBoard[r][c][2];}

    public void setCellConflictStatus(int r, int c, boolean isConflict) {statusBoard[r][c][2] = isConflict;}

    public boolean checkIfCellIsInNoteMode(int id){return statusBoard[id/9][id%9][1];}


    public int[][] boardId;
    public int[][] board;
    public int[][] solvedBoard;
    public boolean[][][] statusBoard;   //1. is cell open 2. is note mode enabled 3.is conflict
    public Stack<Stack<Integer>> moveHistory;  //stores affected cells id
    public Map<Integer, Stack<cellHistoryNode>> cellHistory;    //stores cells states
    public boolean isConflict;



    public sudokuBoard() {
        boardId = new int[9][9];
        board = new int[9][9];
        solvedBoard = new int[9][9];
        statusBoard = new boolean[9][9][3];
        moveHistory = new Stack<>();
        cellHistory = new HashMap<>();
        isConflict = false;
        fillBoardId();
    }

    private void fillBoardId() {
        int count = 0;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                boardId[i][j] = count++;
    }





}
