package ViewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayerVVM {
    private int id;
    private String name;
    private int score;
    private int numberOfTiles;

    public StringProperty nameProperty;
    public StringProperty scoreProperty;
    public StringProperty numberOfTilesProperty;

    public PlayerVVM(int id, String name) {
        this.id = id;
        this.name = name;
        this.score = 0;
        this.numberOfTiles = 0;
        scoreProperty = new SimpleStringProperty(String.valueOf(score));
        numberOfTilesProperty = new SimpleStringProperty(String.valueOf(numberOfTiles));
    }

    public PlayerVVM(int id, String name, int score, int numberOfTiles) {
        setId(id);
        setName(name);
        setScore(score);
        setNumberOfTiles(numberOfTiles);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreProperty = new SimpleStringProperty(String.valueOf(score));
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
        this.numberOfTilesProperty = new SimpleStringProperty(String.valueOf(numberOfTiles));
    }
}
