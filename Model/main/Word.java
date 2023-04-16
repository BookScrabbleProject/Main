package main;


import java.util.Arrays;

public class Word {

	private Tile[] tiles;
    private int row;
    private int col;
    private boolean vertical;

    /**
     *
     * @param tiles an array of tiles
     * @param row the row of the first letter
     * @param col the column of the first letter
     * @param vertical whether the word is vertical or not
     */
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

    /**
     *
     * @return the tiles array of the word
     */
    public Tile[] getTiles() {
        return tiles;
    }

    /**
     *
     * @return the row of the first letter
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return the column of the first letter
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @return true if the word is vertical, false if horizontal
     */
    public boolean isVertical() {
        return vertical;
    }

}
