package ViewModel;

import Model.Model;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    Model model;
//    data binding
    Character[][] board;
    public List<DataChanges> changesList;
    int currentPlayerId;
    List<Character> myHand;

    public ViewModel() {
        board = new Character[15][15];
        changesList = new ArrayList<>();
        currentPlayerId = 0;
        myHand = new ArrayList<>();
        // Todo: add more initialization here
    }

    /**
     * Run when the player clicks the "Place Word" button.
     * Tries to place the word on the board if it is valid (from changesList) using the Model.
     * If the word is invalid, notify the view
     */
    public void tryPlaceWord() {
        if(isChangeValid()) {
            model.tryPlaceWord(getWord(), getWordStartRow(), getWordStartCol(), isWordVertical());
            return;
        }
        // Todo: notify the view that the change is invalid
    }

    /**
     * @return true if the word is valid, false otherwise (from changesList)
     */
    public boolean isChangeValid() {
        // TODO: Implement
        int numberOfColRowChanges = 0;
        int colForCheck = changesList.get(0).getNewCol();
        int rowForCheck = changesList.get(0).getNewRow();
        for(DataChanges dc : changesList) {
            if(dc.getNewCol() != colForCheck) {
                numberOfColRowChanges++;
                colForCheck = dc.getNewCol();
            }
            if (dc.getNewRow() != rowForCheck) {
                numberOfColRowChanges++;
                rowForCheck = dc.getNewRow();
            }
        }

        return numberOfColRowChanges == changesList.size() - 1;
    }

    /**
     * @return the word that the player is trying to place
     */
    public String getWord() {
        // Todo: sort changesList by row and column

        StringBuilder sb = new StringBuilder();
        int row = changesList.get(0).getNewRow();
        int col = changesList.get(0).getNewCol();
        for (DataChanges dc : changesList) {
            if(dc.getNewCol() > col + 1 || dc.getNewRow() > row + 1) {
                for(int i = 0; i < dc.getNewCol() - col - 1; i++) {
                    sb.append('_');
                }
            }
            sb.append(dc.getLetter());
            col = dc.getNewCol();
            row = dc.getNewRow();
        }
        return sb.toString();
    }

    /**
     * @return the row of the first letter of the word
     */
    private int getWordStartRow() {
        int minRow = changesList.get(0).getNewRow();
        for (DataChanges dc : changesList) {
            if(dc.getNewRow() < minRow) {
                minRow = dc.getNewRow();
            }
        }
        return minRow;
    }

    /**
     * @return the column of the first letter of the word
     */
    private int getWordStartCol() {
        int minCol = changesList.get(0).getNewCol();
        for (DataChanges dc : changesList) {
            if(dc.getNewCol() < minCol) {
                minCol = dc.getNewCol();
            }
        }
        return minCol;
    }

    /**
     * @return true if the word is vertical, false if it is horizontal
     */
    private boolean isWordVertical() {
        // TODO: Implement
        return false;
    }
}
