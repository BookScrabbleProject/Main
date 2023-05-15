package Model;

import Model.gameClasses.Tile;

import java.util.List;

public class Player {
    int id; //unique id
    String name;
    int score;
    List<Tile> tiles;

    public Player(int id, String name, int score, List<Tile> tiles) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.tiles = tiles;
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

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
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

    public void addTile(Tile tile) {
        tiles.add(tile);
    }
}
