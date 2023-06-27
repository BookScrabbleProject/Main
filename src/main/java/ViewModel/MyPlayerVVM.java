package ViewModel;

import javafx.beans.property.StringProperty;

import java.util.List;

public class MyPlayerVVM extends PlayerVVM {

    private List<Character> hand;

    public MyPlayerVVM(int id, String name, int score, int numberOfTiles) {
        super(id, name, score, numberOfTiles);
    }

    public List<Character> getHand() {
        return hand;
    }

    public void setHand(List<Character> hand) {
        this.hand = hand;
    }
}
