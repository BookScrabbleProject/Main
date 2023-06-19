package ViewModel;

public class DataChanges {
    char letter;
    int newRow;
    int newCol;
    int oldRow;
    int oldCol;

    public DataChanges(char letter, int newRow, int newCol, int oldRow, int oldCol) {
        this.letter = letter;
        this.newRow = newRow;
        this.newCol = newCol;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
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

    public int getOldRow() {
        return oldRow;
    }

    public void setOldRow(int oldRow) {
        this.oldRow = oldRow;
    }

    public int getOldCol() {
        return oldCol;
    }

    public void setOldCol(int oldCol) {
        this.oldCol = oldCol;
    }
}
