package ViewModel;

import Model.Model;

import java.util.List;

public class ViewModel {
    Model model;
//    data binding
    Character[][] board;
    List<DataChanges> changesList;
    int currentPlayerId;
    List<Character> myHand;

    public ViewModel() {
        // TODO: Implement
    }

    public void tryPlaceWord() {
        // TODO: Implement
    }

    private boolean isChangeValid() {
        // TODO: Implement

        return false;
    }

    private String getWord() {
        // TODO: Implement
        return null;
    }

    private int getWordStartRow() {
        // TODO: Implement
        return -1;
    }

    private int getWordStartCol() {
        // TODO: Implement
        return -1;
    }

    private boolean isWordVertical() {
        // TODO: Implement
        return false;
    }
}
