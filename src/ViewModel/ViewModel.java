package ViewModel;

import Model.Model;

import java.util.ArrayList;
import java.util.List;

public class ViewModel {
    Model model;
//    data binding
    Character[][] board;
    List<DataChanges> changesList;
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
    private boolean isChangeValid() {
        // TODO: Implement

        return false;
    }

    /**
     * @return the word that the player is trying to place
     */
    private String getWord() {
        // TODO: Implement
        return null;
    }

    /**
     * @return the row of the first letter of the word
     */
    private int getWordStartRow() {
        return changesList.get(0).getNewRow();
    }

    /**
     * @return the column of the first letter of the word
     */
    private int getWordStartCol() {
        return changesList.get(0).getNewCol();
    }

    /**
     * @return true if the word is vertical, false if it is horizontal
     */
    private boolean isWordVertical() {
        // TODO: Implement
        return false;
    }
}
