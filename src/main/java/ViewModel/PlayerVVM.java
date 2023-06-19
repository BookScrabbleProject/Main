package ViewModel;

public class PlayerVVM {
    public int id;
    public String name;
    public int score;
    public int numberOfTiles;

    public PlayerVVM(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public PlayerVVM(int id, String name, int score, int numberOfTiles) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.numberOfTiles = numberOfTiles;
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
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public void setNumberOfTiles(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
    }
}
