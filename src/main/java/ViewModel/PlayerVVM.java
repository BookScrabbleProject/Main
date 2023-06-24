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
        nameProperty = new SimpleStringProperty(name);
        scoreProperty = new SimpleStringProperty("0 pts");
        numberOfTilesProperty = new SimpleStringProperty("0");
        setId(id);
        setName(name);
        setScore(score);
        setNumberOfTiles(numberOfTiles);
    }

    public PlayerVVM(int id, String name, int score, int numberOfTiles) {
        nameProperty = new SimpleStringProperty(name);
        scoreProperty = new SimpleStringProperty("0 pts");
        numberOfTilesProperty = new SimpleStringProperty("0");
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
        this.nameProperty.setValue(name);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreProperty.setValue(score + " pts");
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
        this.numberOfTilesProperty.setValue(String.valueOf(numberOfTiles));
    }
}
