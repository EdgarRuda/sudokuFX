package com.sudoku.sudokuLogic;

import com.sudoku.Domain.sudokuBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator{

    private final Solver solver;
    private final Random rn;

    private final int N = 9;

    public Generator() {
        solver = new Solver();
        rn = new Random();
    }

    public void generateNewBoard(sudokuBoard sudokuBoard) {
        fillDiagonal(sudokuBoard.solvedBoard);
        solver.backtrack(sudokuBoard.solvedBoard, 0, 0);
        for(int i = 0; i < 9; i++)
            for(int j = 0; j< 9; j++)
                sudokuBoard.board[i][j] = sudokuBoard.solvedBoard[i][j];
        removeValues(sudokuBoard.board);
    }

    private void fillDiagonal(int[][] solvedBoard) {
        int row, col, num;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < N; i = i + (int) Math.sqrt(N)) {
            for (int t = 1; t <= N; t++)
                list.add(t);

            for (row = i; row < i + 3; row++)
                for (col = i; col < i + 3; col++) {
                    num = rn.nextInt(list.size());
                    solvedBoard[row][col] = list.get(num);
                    list.remove(num);
                }
        }
    }

    private void removeValues(int[][] board) {
        int i, j, cell, cellValue;
        int cluesLeft = 30;
        List<Integer> list = new ArrayList<>();
        for (int t = 0; t < N * N; t++)
            list.add(t);

        //removes random cell from the board if it does not lead to multiple solutions,
        //if it does, removes it from the list and does not check it again

        //generates random difficulty game, varying from super easy to super hard

        //usually there is ~25 clues left
        //if for any reason you need more clues, change cluesLeft val

        while (list.size()>cluesLeft) {
            cell = rn.nextInt(list.size());
            cellValue = list.get(cell);
            list.remove(cell);

            i = cellValue / 9;
            j = cellValue % 9;

            if (solver.onlyOneSolution(board, i, j))
                board[i][j] = 0;

        }
    }


}
