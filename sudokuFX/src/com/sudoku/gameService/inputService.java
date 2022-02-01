package com.sudoku.gameService;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class inputService {


    public  int[] getInput(KeyEvent event) {
        int id = getId(event);
        String s = String.valueOf(event.getCode());
        switch (s) {
            case "UP":
            case "K":
                if (id - 9 < 0)
                    return new int[]{80 - Math.abs(id - 9) + 1};
                else
                    return new int[]{id - 9};
            case "RIGHT":
            case "L":
                if ((id + 1) % 9 == 0)
                    return new int[]{id - id % 9};
                else
                    return new int[]{id + 1};
            case "DOWN":
            case "J":
                if (id + 9 > 80)
                    return new int[]{id + 9 - 80 - 1};
                else
                    return new int[]{id + 9};
            case "LEFT":
            case "H":
                if (id % 9 == 0)
                    return new int[]{id + 8};
                else
                    return new int[]{id - 1};

            case "ADD":         return new int[]{id, 1};
            case "ENTER":       return new int[]{id, 2};
            case "SUBTRACT":    return new int[]{id, 3};

            default:

                if (Character.isDigit(s.charAt(0)))
                    return new int[]{id, 0, Character.getNumericValue(s.charAt(0))};
                else if (Character.isDigit(s.charAt(s.length() - 1)))
                    return new int[]{id, 0, Integer.parseInt(s.substring(s.length() - 1))};
                else
                    return null;
        }
    }

    public  int  getId(KeyEvent event) {
        return Integer.parseInt(((TextArea) event.getSource()).getId().substring(1));
    }

    public  int getId(MouseEvent event) {
        return Integer.parseInt(((TextArea) event.getSource()).getId().substring(1));
    }


}
