package com.sudoku.gameService;

import com.sudoku.Domain.sudokuBoard;
import com.sudoku.Domain.cellHistoryNode;
import com.sudoku.sudokuLogic.Generator;
import com.sudoku.sudokuLogic.Solver;
import com.sudoku.sudokuLogic.Validator;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.*;


public class gameService {

    @FXML
    private CheckBox notesSwitch;
    @FXML
    private CheckBox errorHighlightSwitch;
    @FXML
    private MenuItem exportButton;
    @FXML
    private Label timeLabel;


    @FXML
    protected static List<Rectangle> rectanglesForHighlight;
    @FXML
    protected static List<TextArea> defModeCells;
    @FXML
    protected static List<GridPane> noteModeCells;

    private final inputService keyService = new inputService();
    private final highlightService highlightService = new highlightService();
    private final Validator validator = new Validator();
    private final Solver solver = new Solver();
    private final ioService ioService = new ioService();
    private final timerService timerService = new timerService();
    private final Generator generator = new Generator();

    private com.sudoku.Domain.sudokuBoard sudokuBoard;


    private boolean inputEnabled;
    private boolean gameStarted = false;
    private boolean notesEnabled;
    private boolean errorHighlight;
    private boolean gameIsPaused;

    private int lastId;
    private int cellsLeft;

    //Setters
    public void setGridPanes(List<GridPane> ob) {
        noteModeCells = ob;
    }

    public void setTextAreas(List<TextArea> ob) {
        defModeCells = ob;
    }

    public void setRectangles(List<Rectangle> ob) {
        rectanglesForHighlight = ob;
    }

    public void setNotesSwitch(CheckBox ob) {
        notesSwitch = ob;
    }

    public void setErrorHighlightSwitch(CheckBox ob) {
        errorHighlightSwitch = ob;
    }

    public void setExportButton(MenuItem ob) {
        exportButton = ob;
    }

    public void setTimeLabel(Label ob) {
        timeLabel = ob;
    }


    public void toggleGamePause() {
        if (gameStarted) {
            inputEnabled = !inputEnabled;
            timerService.toggleTimer();
            if (!gameIsPaused) {
                highlightService.clearHighlights();
                disableCells();
                gameIsPaused = true;
            } else {
                gameIsPaused = false;
                enableCells();
                highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);

            }
        }
    }


    private void disableCells() {
        for (TextArea t : defModeCells)
            t.setDisable(true);
    }

    private void enableCells() {
        for (TextArea t : defModeCells)
            t.setDisable(false);
    }

    public void toggleNoteSwitch() {
        notesEnabled = !notesEnabled;
        defModeCells.get(lastId).requestFocus();

        notesSwitch.setSelected(notesEnabled);
    }

    public void toggleErrorHighlight() {
        errorHighlight = !errorHighlight;
        defModeCells.get(lastId).requestFocus();
        errorHighlightSwitch.setSelected(errorHighlight);
    }

    public void generateAndStartGame() {
        startGame(generateBoard());
    }

    public int importAndStartGame(TextField importBox) {
        String s = importBox.getText();

        if (s.length() == 81) {
            sudokuBoard tempBoard =
                    createImportedBoard(s);
            if (solver.backtrack(tempBoard.getSolvedBoard(), 0, 0)) {
                startGame(tempBoard);
                return 0;
            } else return 2;//board is not solvable
        } else return 1;//board string has wrong length
    }

    private void startGame(sudokuBoard newBoard) {

        cellsLeft = 81;
        lastId = 40;

        if (gameStarted)
            clearGrid();

        sudokuBoard = newBoard;

        prepareGrid();
        enableCells();
        highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);

        gameIsPaused = false;
        gameStarted = true;
        inputEnabled = true;
        exportButton.setDisable(false);
        notesSwitch.setDisable(false);
        errorHighlightSwitch.setDisable(false);
        timerService.setTimeLabel(timeLabel);
        timerService.startTimer();

        System.out.println("Current board: ");
        ioService.printBoard(sudokuBoard.getBoard());
        System.out.println("Solved board: ");
        ioService.printBoard(sudokuBoard.getSolvedBoard());
    }

    private sudokuBoard generateBoard() {
        sudokuBoard temp = new sudokuBoard();
        generator.generateNewBoard(temp);
        System.out.println("board is ready");

        return temp;
    }


    private void prepareGrid() {
        TextArea t;

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                t = defModeCells.get(sudokuBoard.getCellId(i, j));

                if (sudokuBoard.board[i][j] != 0) {
                    sudokuBoard.statusBoard[i][j][0] = false;
                    t.setText(String.valueOf(sudokuBoard.board[i][j]));
                    t.setStyle(";-fx-text-fill:black;");
                    cellsLeft--;

                } else
                    sudokuBoard.statusBoard[i][j][0] = true;
            }
    }


    private void clearGrid() {
        for (TextArea t : defModeCells) {
            t.setText("");
            t.setStyle(";-fx-text-fill:#2f78f4;");
        }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                if (sudokuBoard.getCellMode(i, j))
                    for (Node n : noteModeCells.get(sudokuBoard.getCellId(i, j)).getChildren())
                        n.setVisible(false);
    }

    public void processEvent(KeyEvent event) {
        if (gameStarted && !gameIsPaused && inputEnabled)
            processInput(keyService.getInput(event));
    }

    public void processEvent(MouseEvent event) {
        if (gameStarted && !gameIsPaused  && inputEnabled)
            processInput(new int[]{keyService.getId(event)});
    }


    private void processInput(int[] data) {
        lastId = data[0];
        if (data.length == 1)
            requestCellFocus();
        if (data.length == 2)
            optionsToggle(data[1]);
        if (data.length == 3)
            processNumericInput(data[2]);

    }

    private void requestCellFocus(){
        defModeCells.get(lastId).requestFocus();
        highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);
    }

    private void optionsToggle(int option){
        switch (option) {
            case 1 -> toggleNoteSwitch();
            case 2 -> {
                toggleErrorHighlight();
                highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);
            }
            case 3 -> undoInput();
        }
    }

    private void processNumericInput(int inputNum){
        int row = lastId / 9;
        int col = lastId % 9;

        if (sudokuBoard.getCellType(row, col)){
            if (notesEnabled) {
                if (!sudokuBoard.getCellMode(row, col)) {
                    addToBoard(0);
                    validator.checkConflicts(lastId, sudokuBoard);
                }
                toggleNoteCell(inputNum);

            } else {
                if (sudokuBoard.getCellMode(row, col))
                    clearNotes();
                addToBoard(inputNum);
                validator.checkConflicts(lastId, sudokuBoard);
                checkIfWon();
            }
        pushCellHistory(inputNum, notesEnabled);
        }
        highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);

    }

    private void pushCellHistory(int num, boolean isNoteMode) {
        sudokuBoard.moveHistory.push(lastId);
        if (sudokuBoard.cellHistory.containsKey(lastId) && !sudokuBoard.cellHistory.get(lastId).isEmpty()) {
            if (isNoteMode) {
                if (sudokuBoard.cellHistory.get(lastId).peek().isNoteMode)
                    sudokuBoard.cellHistory.get(lastId).push(new cellHistoryNode(true, sudokuBoard.cellHistory.get(lastId).peek().notes.clone(), num));
                else
                    sudokuBoard.cellHistory.get(lastId).push(new cellHistoryNode(true, num));
            } else {
                sudokuBoard.cellHistory.get(lastId).push(new cellHistoryNode(false, num));
            }
        } else {
            sudokuBoard.cellHistory.put(lastId, new Stack<>());
            if (isNoteMode)
                sudokuBoard.cellHistory.get(lastId).push(new cellHistoryNode(true, num));
            else
                sudokuBoard.cellHistory.get(lastId).push(new cellHistoryNode(false, num));
        }

    }


    private void undoInput() {
        if (!sudokuBoard.moveHistory.isEmpty()) {
            lastId = sudokuBoard.moveHistory.pop();
            sudokuBoard.cellHistory.get(lastId).pop();

            if (sudokuBoard.cellHistory.get(lastId).isEmpty()) {
                if (sudokuBoard.checkIfCellIsInNoteMode(lastId))
                    clearNotes();
                addToBoard(0);
            } else if (sudokuBoard.cellHistory.get(lastId).peek().isNoteMode) {
                toggleNoteList(sudokuBoard.cellHistory.get(lastId).peek().notes);
                addToBoard(0);
            } else {
                if (sudokuBoard.checkIfCellIsInNoteMode(lastId))
                    clearNotes();
                addToBoard(sudokuBoard.cellHistory.get(lastId).peek().num);
            }

            validator.checkConflicts(lastId, sudokuBoard);
            highlightService.highlightCells(lastId, sudokuBoard, errorHighlight);
        }
    }

    private void addToBoard(int num) {
        int row = lastId / 9;
        int col = lastId % 9;
        if (num == 0 || num == sudokuBoard.board[row][col]) {
            defModeCells.get(lastId).setText("");
            sudokuBoard.board[row][col] = 0;
            cellsLeft++;
        } else {
            defModeCells.get(lastId).setText(String.valueOf(num));
            if (sudokuBoard.board[row][col] == 0)
                cellsLeft--;
            sudokuBoard.board[row][col] = num;
        }

        defModeCells.get(lastId).requestFocus();
    }

    private void checkIfWon() {
        if (cellsLeft != 0 || !sudokuBoard.isConflict())
            return;

        inputEnabled = false;
        timerService.stopTimer();
    }


    private void toggleNoteCell(int num) {

        GridPane g = noteModeCells.get(lastId);
        sudokuBoard.setCellMode(lastId / 9, lastId % 9, true);

        for (Node n : g.getChildren())
            if (Integer.parseInt(n.getId()) == num) {
                n.setVisible(!n.isVisible());
                break;
            }

    }

    private void clearNotes() {

        GridPane g = noteModeCells.get(lastId);
        sudokuBoard.setCellMode(lastId / 9, lastId % 9, false);

        for (Node n : g.getChildren())
            n.setVisible(false);
    }

    private void toggleNoteList(boolean[] flags) {

        GridPane g = noteModeCells.get(lastId);
        sudokuBoard.setCellMode(lastId / 9, lastId % 9, true);

        int count = 0;
        for (Node n : g.getChildren())
            n.setVisible(flags[count++]);

    }

    //import-export
    public void copyString(TextField exportBox) {
        ioService.copyString(exportBox);
    }

    public void fillExportBox(TextField exportBox) {
        if (gameStarted)
            ioService.fillExportBox(exportBox, sudokuBoard.board);
    }


    private sudokuBoard createImportedBoard(String board) {
        sudokuBoard temp = new sudokuBoard();
        int count = 0;
        char ch;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                ch = board.charAt(count++);
                if (Character.isDigit(ch)) {
                    temp.board[i][j] = Character.getNumericValue(ch);
                    temp.solvedBoard[i][j] = Character.getNumericValue(ch);
                } else {
                    temp.board[i][j] = 0;
                    temp.solvedBoard[i][j] = 0;
                }
            }
        return temp;
    }

}
