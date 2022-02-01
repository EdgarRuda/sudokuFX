package com.sudoku;

import com.sudoku.gameService.gameService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class Main extends Application {
    @FXML
    private CheckBox notesSwitch;
    @FXML
    private CheckBox errorHighlightSwitch;
    @FXML
    private List<Rectangle> rectanglesForHighlight;
    @FXML
    private List<TextArea> textAreas;
    @FXML
    private List<GridPane> gridPanes;
    @FXML
    private Pane importPane;
    @FXML
    private TextField importBox;
    @FXML
    private Pane exportPane;
    @FXML
    private TextField exportBox;
    @FXML
    private MenuItem exportButton;
    @FXML
    private Label timeLabel;


    private final gameService gameService = new gameService();

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("icon.png"));
        stage.setTitle("sudokuFX");
        stage.show();
    }

    public void initialize() {
        gameService.setGridPanes(gridPanes);
        gameService.setTextAreas(textAreas);
        gameService.setRectangles(rectanglesForHighlight);
        gameService.setNotesSwitch(notesSwitch);
        gameService.setErrorHighlightSwitch(errorHighlightSwitch);
        gameService.setExportButton(exportButton);
        gameService.setTimeLabel(timeLabel);
    }

    public void generateBoard() {
        hideImportPane();
        hideExportPane();
        gameService.generateAndStartGame();
    }

    public void keyEvent(KeyEvent event) {
        gameService.processEvent(event);
    }
    public void mouseEvent(MouseEvent event) {
        gameService.processEvent(event);
    }
    public void toggleNotesSwitch() {gameService.toggleNoteSwitch();}
    public void toggleErrorHighlight() {
        gameService.toggleErrorHighlight();
    }

    public void importBoard(){
        int errorCode = gameService.importAndStartGame(importBox);
        if(errorCode == 0) hideImportPane();
        else if(errorCode == 1)
            importPaneErrorDisplay("wrong board size");
        else if(errorCode == 2)
            importPaneErrorDisplay("board has no / more than one solution");

    }

    public void showImportPane() {
        importPane.setViewOrder(-1);
    }

    private void importPaneErrorDisplay(String s){
        importBox.setStyle("-fx-border-color: red");
        importBox.setText(s);
    }
    public void hideImportPane(){
        importBox.setStyle("-fx-border-color: transparent;");
        importBox.setText("");
        importPane.setViewOrder(9);
    }

    public void showExportPane() {
        gameService.fillExportBox(exportBox);
        exportPane.setViewOrder(-1);

    }
    public void hideExportPane(){
        exportBox.setText("");
        exportPane.setViewOrder(10);
    }
    public void copyExportContent(){
        gameService.copyString(exportBox);
        hideExportPane();
    }

    public void toggleGamePause(){
        gameService.toggleGamePause();
    }

    public static void main(String[] args) {launch(args);}

}