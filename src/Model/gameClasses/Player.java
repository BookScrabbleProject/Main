package Model.gameClasses;

import Model.gameClasses.Tile;

import java.util.ArrayList;
import java.util.List;

public class Player {
    int id; //unique id
    String name;
    int score;
    List<Character> hand;

    public Player(int id, String name, int score, List<Character> hand) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.hand = hand;
    }
    public Player(String name) {
        this.id = -1;
        this.name = name;
        this.score = 0;
        hand = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Character> getTiles() {
        return hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHand(List<Character> hand) {
        this.hand = hand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id= id;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void addTiles(String st) {
        for (int i = 0; i < st.length(); i++) {
            hand.add(st.charAt(i));
        }
    }

    public void removeTiles(String st) {
        for (int i = 0; i < st.length(); i++) {
            hand.remove(st.charAt(i));
        }
    }
}
