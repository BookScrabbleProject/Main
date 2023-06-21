package ViewModel;

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
    }

    public PlayerVVM(int id, String name, int score, int numberOfTiles) {
        setId(id);
        this.nameProperty = new javafx.beans.property.SimpleStringProperty(name);
        setName(name);
        this.scoreProperty = new javafx.beans.property.SimpleStringProperty(String.valueOf(score));
        setScore(score);
        this.numberOfTilesProperty = new javafx.beans.property.SimpleStringProperty(String.valueOf(numberOfTiles));
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
        nameProperty.setValue(name);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        scoreProperty.setValue(String.valueOf(score));
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
        numberOfTilesProperty.setValue(String.valueOf(numberOfTiles));
    }
}
