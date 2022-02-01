package com.sudoku.sudokuLogic;

public class Solver {
    private final Validator validator ;
    private final int N;

    public Solver(){
        validator = new Validator();
        this.N = 9;
    }


    public boolean backtrack(int[][] b, int i, int j) {
        if (j > 8) {
            i++;
            j = 0;
        }
        if (i > 8)
            return true;

        if (b[i][j] != 0)
            return backtrack(b, i, j + 1);

        for (int num = 1; num <= N; num++) {
            b[i][j] = num;
            if (validator.validateBoard(b, i, j)) {
                if (backtrack(b, i, j + 1))
                    return true;
            }
        }
        b[i][j] = 0;
        return false;
    }

    protected boolean onlyOneSolution(int[][] board, int row, int col) {
        int solutionCount = 0;
        int temp = board[row][col];
        int[][] copy = new int[N][N];

        for (int i = 1; i <= N; i++) {
            board[row][col] = i;
            if (validator.validateBoard(board, row, col)) {

                for (int k = 0; k < N; k++)
                    for (int l = 0; l < N; l++)
                        copy[k][l] = board[k][l];

                if (backtrack(copy, 0, 0))
                    if (++solutionCount > 1) {
                        board[row][col] = temp;
                        return false;
                    }
            }
        }
        return true;
    }
}
