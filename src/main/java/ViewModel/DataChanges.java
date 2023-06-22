package ViewModel;

public class DataChanges {
    char letter;
    int newRow;
    int newCol;

    public DataChanges(char letter, int newRow, int newCol) {
        this.letter = letter;
        this.newRow = newRow;
        this.newCol = newCol;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getNewRow() {
        return newRow;
    }

    public void setNewRow(int newRow) {
        this.newRow = newRow;
    }

    public int getNewCol() {
        return newCol;
    }

    public void setNewCol(int newCol) {
        this.newCol = newCol;
    }
}
