package Model.gameClasses;
import java.util.Objects;

public class Tile {
    public final char letter;
    public final int score;

    /**
     *
     * @param letter a letter that the tile represents
     * @param score the score that the tile holds
     */
    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    /**
     *
     * @return the letter that the tile represents
     */
    public char getLetter() {
        return letter;
    }

    /**
     *
     * @return the score that the tile holds
     */
    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        public int[] letters;
        public int[] originalAmountOfLetters;
        public int[] scores;
        public Tile[] tiles;
        public int totalTiles;
        private static Bag single_Bag = null;

        private Bag() {
            this.scores = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10}; //score of the letters
            this.originalAmountOfLetters = new int[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            //current amount of letters
            this.letters = new int[] {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
            this.tiles = new Tile[26];
            this.totalTiles=98;
            int i=0;
            for(char c = 'A'; c<='Z'; c++, i++) {
                this.tiles[i] = new Tile(c, this.scores[i]);
            }
        }

        /**
         *
         * @return a random tile from the bag
         */
        public Tile getRand(){
            if(size()==0){
                return null;
            }
            int randomNum;
            while(true) {
                randomNum = (int)(Math.random()*26);
                if(this.letters[randomNum]>0) {
                    totalTiles--;
                    this.letters[randomNum]--;
                    return this.tiles[randomNum];
                } // else continue look for another tile.
            }
        }

        /**
         *
         * @param c an alphabetic letter
         * @return a tile that represents the given letter
         */
        public Tile getTile(char c) {

            if(c > 'Z' || c < 'A'){
                return null;
            }
            int indexchar = c-'A';
            if(this.letters[indexchar]==0) {
                return null;
            }
            totalTiles--;
            this.letters[indexchar]--;
            return this.tiles[indexchar];
        }

        public void addTile(char c) {
            if(c > 'Z' || c < 'A'){
                return;
            }
            int indexchar = c-'A';
            if(this.letters[indexchar]==0) {
                return;
            }
            totalTiles++;
            this.letters[indexchar]++;
        }

        public Tile getTileToWord(char c) {
            c = Character.toUpperCase(c);
            if (c > 'Z' || c < 'A')
                return null;

            addTile(c);
            return getTile(c);
        }

        /**
         *
         * @param t return a tile to the bag
         */
        public void put(Tile t){
            if(this.letters[t.letter-'A']==this.originalAmountOfLetters[t.letter-'A']){
                return;
            }
            this.letters[t.letter-'A']++;
            this.totalTiles++;

        }

        /**
         *
         * @return the total amount of tiles in the bag
         */
        public int size(){
            return this.totalTiles;
        }

        /**
         *
         * @return an amounts array of the tiles in the bag
         */
        public int[] getQuantities(){
            int[] Quantities = new int[26];
            System.arraycopy(this.letters, 0, Quantities, 0, 26);
            return Quantities;
        }

        /**
         *
         * @return the bag - Singleton
         */
        public static Bag getBag(){
            if(single_Bag == null){
                single_Bag = new Bag();
            }
            return single_Bag;
        }
    }
}






