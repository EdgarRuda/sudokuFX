package com.sudoku.gameService;

import com.sudoku.Domain.sudokuBoard;

import static com.sudoku.gameService.gameService.*;

public class highlightService {

    //board styles
    private final String defBackground = ";-fx-background-color:transparent;";
    private final String sameNumBackground = "; -fx-background-color:#94add7;";
    private final String conflictBackground = ";-fx-background-color:#f7cfd6;";
    private final String highlightBackground = ";-fx-background-color:#e2ebf3;";

    private final String conflictFont = ";-fx-text-fill: #e55c6c;";
    private final String openCellFont = ";-fx-text-fill: blue;";
    private final String lockedCellFont = ";-fx-text-fill: black;";

    //row->col->sector
    private static int[] prevHighlight;



    public void highlightCells(int id,sudokuBoard sudokuBoard, boolean highlightErrors) {
        if (prevHighlight == null) prevHighlight = new int[3];
        else clearHighlights();

        highlightRectangles(id);
        highlightAll(id, sudokuBoard, highlightErrors);

    }

    private void highlightRectangles(int id) {
        int row = id / 9;
        int col = 9 + id % 9;
        int sector = 18 + row / 3 * 3 + (col - 9) / 3;

        rectanglesForHighlight.get(row).setVisible(true);
        rectanglesForHighlight.get(col).setVisible(true);
        rectanglesForHighlight.get(sector).setVisible(true);

        prevHighlight[0] = row;
        prevHighlight[1] = col;
        prevHighlight[2] = sector;
    }

    public void clearHighlights() {
        rectanglesForHighlight.get(prevHighlight[0]).setVisible(false);
        rectanglesForHighlight.get(prevHighlight[1]).setVisible(false);
        rectanglesForHighlight.get(prevHighlight[2]).setVisible(false);
    }


    private void highlightAll(int id, sudokuBoard sudokuBoard, boolean highlightErrors) {
        int row = id / 9;
        int col = id % 9;
        int val = sudokuBoard.board[row][col];

        defModeCells.get(id).requestFocus();

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                if (sudokuBoard.board[i][j] != 0) {
                    if (sudokuBoard.getCellMode(i,j)) continue;

                    defModeCells.get(sudokuBoard.getCellId(i,j)).setStyle(getStyle(i, j, val, sudokuBoard, highlightErrors));
                } else
                    defModeCells.get(sudokuBoard.getCellId(i,j)).setStyle("");
            }

    }

    private String getStyle(int i, int j, int val,sudokuBoard sudokuBoard, boolean highlightErrors) {

        String font, background;
        boolean isError=false;
        if(highlightErrors)
            isError = sudokuBoard.board[i][j] != sudokuBoard.solvedBoard[i][j];

        if (sudokuBoard.board[i][j] == val) {
            background = sameNumBackground;
            font = sudokuBoard.getCellConflictStatus(i,j) || isError ? conflictFont :
                    sudokuBoard.getCellType(i,j) ? openCellFont : lockedCellFont;

        } else if (sudokuBoard.getCellConflictStatus(i,j) || isError) {
            background = conflictBackground;
            font = sudokuBoard.getCellType(i,j) ? conflictFont : lockedCellFont;
        } else {
            background = defBackground;
            font = sudokuBoard.getCellType(i,j)  ? openCellFont : lockedCellFont;
        }
        return font + background;
    }

}
