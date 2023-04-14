package main;


import java.util.Arrays;

public class Word {

	private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = new Tile[tiles.length];
        System.arraycopy(tiles, 0, this.tiles, 0, tiles.length);
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return row == word.row && col == word.col && vertical == word.vertical && Arrays.equals(tiles, word.tiles);
    }


    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

}
