package com.sudoku.Domain;

public class cellHistoryNode {
    public int num;
    public boolean[] notes;
    public boolean isNoteMode;

    public cellHistoryNode(boolean isNoteMode, int num) {
        this.isNoteMode = isNoteMode;
        if (isNoteMode) {
            notes = new boolean[9];
            notes[num - 1] = true;
        } else
            this.num = num;
    }

    public cellHistoryNode(boolean isNoteMode, boolean[] n, int num) {
        this.isNoteMode = isNoteMode;
        this.notes = n;
        notes[num - 1] = !notes[num - 1];
    }

}
