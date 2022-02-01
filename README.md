# sudokuFX

complete GUI for sudoku games with board highlights and sudoku generator

![image](https://user-images.githubusercontent.com/53341064/149345148-18a55942-8313-47cf-a6f3-44532d5b3ea8.png)


Features:

same num, cel conflict and mistakes highlight

notes mode

undo move(notes included) 


Board generator:

generates random board with only one solution. difficulty varies from super easy to super hard (unfortunately)

how it works
1. board sectors on one diagonal are filled with random values
2. rest of the board is being solved with backtracking algorithm
3. removes random cell from the board if it does not lead to multiple solutions, if it does, then that cell is never checked again
4. when all cells are checked and there is nothing else to remove without producing multimple solutions - board is ready




