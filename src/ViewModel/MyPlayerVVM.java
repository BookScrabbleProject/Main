package ViewModel;

import java.util.List;

public class MyPlayerVVM extends PlayerVVM {

    public List<Character> hand;

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
