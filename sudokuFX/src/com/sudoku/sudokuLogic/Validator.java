package com.sudoku.sudokuLogic;

import com.sudoku.Domain.sudokuBoard;

import java.util.ArrayList;
import java.util.List;

public class Validator{

    protected boolean validateBoard(int[][] board, int row, int col) {
        int num = board[row][col];
        board[row][col] = 0;

        List<int[]> correspondingCells = getCorrespondingCells(row, col);

        for (int[] cells : correspondingCells)
            if (board[cells[0]][cells[1]] == num) {
                board[row][col] = num;
                return false;
            }


        board[row][col] = num;
        return true;
    }

    public List<int[]> getAllConflicts(int[][] board) {

        List<int[]> conflicts = new ArrayList<>();
        List<int[]> correspondingCells;
        int num;

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (board[i][j] != 0) {

                    num = board[i][j];
                    board[i][j] = 0;

                    correspondingCells = getCorrespondingCells(i, j);

                    for (int[] cells : correspondingCells)
                        if (board[cells[0]][cells[1]] == num)
                            conflicts.add(new int[]{cells[0], cells[1]});

                    board[i][j] = num;
                }
        return conflicts;
    }


    public List<int[]> getCellConflicts(int[][] board, int row, int col) {
        int num = board[row][col];
        if(num == 0)
            return  null;
        board[row][col] = 0;

        List<int[]> conflicts = new ArrayList<>();
        List<int[]> correspondingCells = getCorrespondingCells(row, col);


        for (int[] cells : correspondingCells)
            if (board[cells[0]][cells[1]] == num)
                conflicts.add(new int[]{cells[0], cells[1]});

        board[row][col] = num;

        if (!conflicts.isEmpty())
            conflicts.add(new int[]{row, col});

        return conflicts;
    }


public List<int[]> getCorrespondingCells(int row, int col) {
    List<int[]> cells = new ArrayList<>();

    int sRow = row - row % 3;
    int sCol = col - col % 3;

    for (int count = 0; count < sCol; count++)
    cells.add(new int[]{row, count});
    for (int count = sCol + 3; count < 9; count++)
        cells.add(new int[]{row, count});


    for (int count = 0; count < sRow; count++)
        cells.add(new int[]{count, col});
    for (int count = sRow + 3; count < 9; count++)
        cells.add(new int[]{count, col});


    for (int k = sRow; k < sRow + 3; k++)
        for (int l = sCol; l < sCol + 3; l++)
            cells.add(new int[]{k, l});

    return cells;
}

    public void checkConflicts(int id, sudokuBoard sudokuBoard) {
        List<int[]> conflicts;
        int row = id / 9;
        int col = id % 9;

        if (!sudokuBoard.isConflict()) {
            conflicts = getCellConflicts(sudokuBoard.getBoard(), row, col);
            if (conflicts == null || conflicts.isEmpty())
                return;

        } else {
            conflicts = getAllConflicts(sudokuBoard.getBoard());

            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    sudokuBoard.setCellConflictStatus(i,j,false);
        }

        if (!conflicts.isEmpty()) {
            for (int[] cell : conflicts)
                sudokuBoard.setCellConflictStatus(cell[0],cell[1], true);

            sudokuBoard.setConflictStatus(true);
        }
        else sudokuBoard.setConflictStatus(false);
    }

}
