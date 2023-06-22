package Model.gameClasses;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private static final int size = 15;
    private BoardSquare[][] gameBoard;
    private static Board single_Board = null;


    /**
     * Gives a reference to the board - or creates it - Single Tone
     * @return returns the state of the board
     */
    public static Board getBoard() {
        if (single_Board == null) {
            single_Board = new Board();
        }
        return single_Board;
    }

    /**
     * the board constructor. Initializes the gameboard and builds it.
     */
    public Board() {
        gameBoard = new BoardSquare[size][size];
        buildBoard();
    }

    /**
     * Assigning all the squares according to the given template.
     */
    public void buildBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gameBoard[i][j] = new BoardSquare();
            }
        }


        //Setting Start square - Star
        gameBoard[7][7].setColor(5);

        //Setting Red squares - color 4 - Triple Word Score
        gameBoard[0][0].setColor(4);
        gameBoard[0][7].setColor(4);
        gameBoard[0][14].setColor(4);
        gameBoard[7][0].setColor(4);
        gameBoard[7][14].setColor(4);
        gameBoard[14][0].setColor(4);
        gameBoard[14][7].setColor(4);
        gameBoard[14][14].setColor(4);

        //Setting Yellow squares - color 3 - Double Word Score
        gameBoard[1][1].setColor(3);
        gameBoard[2][2].setColor(3);
        gameBoard[3][3].setColor(3);
        gameBoard[4][4].setColor(3);
        gameBoard[1][13].setColor(3);
        gameBoard[2][12].setColor(3);
        gameBoard[3][11].setColor(3);
        gameBoard[4][10].setColor(3);
        gameBoard[13][13].setColor(3);
        gameBoard[12][12].setColor(3);
        gameBoard[11][11].setColor(3);
        gameBoard[10][10].setColor(3);
        gameBoard[10][4].setColor(3);
        gameBoard[11][3].setColor(3);
        gameBoard[12][2].setColor(3);
        gameBoard[13][1].setColor(3);

        //Setting Blue squares - color 2 - Triple Letter Score
        gameBoard[1][5].setColor(2);
        gameBoard[1][9].setColor(2);
        gameBoard[5][1].setColor(2);
        gameBoard[5][5].setColor(2);
        gameBoard[5][9].setColor(2);
        gameBoard[5][13].setColor(2);
        gameBoard[9][1].setColor(2);
        gameBoard[9][5].setColor(2);
        gameBoard[9][9].setColor(2);
        gameBoard[9][13].setColor(2);
        gameBoard[13][5].setColor(2);
        gameBoard[13][9].setColor(2);

        //Setting Cyan squares - color 1 - Double Letter Score
        gameBoard[0][3].setColor(1);
        gameBoard[0][11].setColor(1);
        gameBoard[2][6].setColor(1);
        gameBoard[2][8].setColor(1);
        gameBoard[3][0].setColor(1);
        gameBoard[3][7].setColor(1);
        gameBoard[3][14].setColor(1);
        gameBoard[6][2].setColor(1);
        gameBoard[6][6].setColor(1);
        gameBoard[6][8].setColor(1);
        gameBoard[6][12].setColor(1);
        gameBoard[7][3].setColor(1);
        gameBoard[7][11].setColor(1);
        gameBoard[8][2].setColor(1);
        gameBoard[8][6].setColor(1);
        gameBoard[8][8].setColor(1);
        gameBoard[8][12].setColor(1);
        gameBoard[11][0].setColor(1);
        gameBoard[11][7].setColor(1);
        gameBoard[11][14].setColor(1);
        gameBoard[12][6].setColor(1);
        gameBoard[12][8].setColor(1);
        gameBoard[14][3].setColor(1);
        gameBoard[14][11].setColor(1);

    }

    /**
     * Creates a 2-D array of tiles and copy the status of the tiles in the board.
     * @return a 2D array of the tiles in the board.
     */
    public Tile[][] getTiles() {
        Tile[][] theTiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (gameBoard[i][j].tile != null) {
                    theTiles[i][j] = gameBoard[i][j].getTile();
                } else {
                    theTiles[i][j] = null;
                }
            }
        }
        return theTiles;

    }

    /**
     * Checks if a given word assignment on the board is legal
     * @param word a given word to check whether we can place it on the board
     * @return if the word placement is legal or not
     */
    public boolean boardLegal(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;
        if (word.getTiles().length < 2)
            return false;
        //If the center square is null - so it's the first turn -
        // we must check that the word pass in the Star square
        if (!isWordInBorders(word)) {
            return false;
        }
        if (gameBoard[7][7].getTile() == null) {
            return isWordOnStarSquare(word);
        }

        //not the first word on the boarder
        return (isLeaning(word) && donotReplaceLetter(word));
        /*if(!donotReplaceLetter(word)){//changes the word
            return false;
        }
        if(!isLeaning(word){
            return false;
        }
        return true;*/
    }

    /**
     * get the whole score for placing the word
     * @param word a given word to check the score we will get after placing iot
     * @return an int which represent the score we will get after the word placement
     */
    private int getScore(Word word) {
        int sum=0;
        int mul=1;
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;
        for(int i=0;i<len;i++){
            switch (gameBoard[row][col].color){
                case 1:
                    sum+=word.getTiles()[i].getScore()*2;
                    break;
                case 2:
                    sum+=word.getTiles()[i].getScore()*3;
                    break;
                case 3:
                    sum+=word.getTiles()[i].getScore();
                    mul*=2;
                    break;
                case 4:
                    sum+=word.getTiles()[i].getScore();
                    mul*=3;
                    break;
                case 5:
                    sum+=word.getTiles()[i].getScore();
                    if(gameBoard[7][7].getTile()==null){
                        mul*=2;
                    }
                    break;
                default:
                    sum+=word.getTiles()[i].getScore();
            }
            if(word.isVertical())
                row++;
            else
                col++;
        }
        return sum*mul;
    }

    /**
     * Checks if the word is a valid word from the game dictionary
     * @param word - to check if it is a valid word
     * @return if the word is valid or not.
     */
    public boolean dictionaryLegal(Word word) {
        return true;
    }

    /**
     * Gets all the possible words that are created if a given word would be placed.
     * @param word a given word that the user might place.
     * @return a list of words that are created by the given word.
     */
    public ArrayList<Word> getWords(Word word) {
        ArrayList<Word> createdWords = new ArrayList<Word>();
        int col = word.getCol();
        int row = word.getRow();
        boolean flag = false;
        for (int i = 0; i < word.getTiles().length; i++) {
            if (word.getTiles()[i] != null) {
                if(word.isVertical()){
                    if(i==0 && row>0 && gameBoard[row-1][col].tile!=null || i==word.getTiles().length-1 && row<14 && gameBoard[row+1][col].tile!=null){
                        if(!flag)
                            createdWords.add(getWord(row, col, word, i, true));
                        flag = true;
                    }
                    if(col>0 && gameBoard[row][col-1].tile!=null || col<14 && gameBoard[row][col+1].tile!=null){
                        createdWords.add(getWord(row, col, word, i, false));
                    }

                }
                else{
                    if(i==0 && col>0 && gameBoard[row][col-1].tile!=null || i==word.getTiles().length-1 && col<14 && gameBoard[row][col+1].tile!=null){
                        if(!flag)
                            createdWords.add(getWord(row, col, word, i, false));
                        flag = true;
                    }
                    if(row>0 && gameBoard[row-1][col].tile!=null || row<14 && gameBoard[row+1][col].tile!=null){
                        createdWords.add(getWord(row, col, word, i, true));
                    }
                }
            }
            if(word.isVertical())
                row++;
            else{
                col++;
            }
        }
        if(!flag)
            createdWords.add(fullWord(word));
        return createdWords;
    }

    public List<Tile> getWord(String word) {
        List<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < word.length(); i++) {
            tiles.add(Tile.Bag.getBag().getTileToWord(word.charAt(i)));
        }
        return tiles;
    }

    /**
     * gets a word that is created by placing a letter of a given word
     * @param row the row of the first letter of the given word
     * @param col the col of the first letter of the given word
     * @param word a given word
     * @param i the index of a letter in the word
     * @param vertical if the given word is vertical or horizontal
     * @return a word that is creater by placing the i's letter of the given word.
     */
    private Word getWord(int row, int col, Word word, int i, boolean vertical){
        int startRow = row;
        int startCol = col;
        int endRow = row;
        int endCol = col;
        int index = i;
        if(vertical){
            if(word.isVertical()) {
                index=0;
                startRow = word.getRow();
                endRow = word.getRow() + word.getTiles().length - 1;
            }

            while(startRow>0 && gameBoard[startRow-1][col].tile!=null){
                startRow--;
            }
            while(endRow<14 && gameBoard[endRow+1][col].tile!=null){
                endRow++;
            }
            /*if(word.isVertical())
                startRow=word.getRow();*/
            Tile [] tiles = new Tile[endRow-startRow+1];
            for(int j=0;j<tiles.length;j++){
                if(gameBoard[startRow+j][col].tile==null){
                    tiles[j] = word.getTiles()[index];
                    index++;
                    while(index<word.getTiles().length-1 && word.getTiles()[index]==null){
                        index++;
                    }
                }
                else {
                    tiles[j] = gameBoard[startRow + j][col].getTile();
                }
            }
            return new Word(tiles, startRow, col, vertical);
        }
        else{
            if(!word.isVertical()){
                index=0;
                startCol = word.getCol();
                endCol = word.getCol() + word.getTiles().length - 1;
            }
            while(startCol>0 && gameBoard[row][startCol-1].tile!=null){
                startCol--;
            }
            while(endCol<14 && gameBoard[row][endCol+1].tile!=null){
                endCol++;
            }
            Tile [] tiles = new Tile[endCol-startCol+1];
            for(int j=0;j<tiles.length;j++){
                if(gameBoard[row][startCol+j].tile==null){
                    tiles[j] = word.getTiles()[index];
                    index++;
                    while(index<word.getTiles().length-1 && word.getTiles()[index]==null){
                        index++;
                    }
                }
                else
                    tiles[j] = gameBoard[row][startCol+j].getTile();
            }
            return new Word(tiles, row, startCol, vertical);
        }



    }


    /**
     * checks all the condition and tries to place the word.
     * @param word a word that the user tries to place.
     * @return return the score by placing that word (0 if didn't succeed).
     */
    public int tryPlaceWord(Word word){
        ArrayList<Word> createdWords = new ArrayList<Word>();
        int sum=0;
        if(boardLegal(word)){
            createdWords = getWords(word);
            for(Word w:createdWords){
                if(!dictionaryLegal(w)){
                    return 0;
                }
                else{
                    sum+=getScore(w);
                }
            }
        }
        else{
            return 0;
        }
        wordPlacing(word);
        return sum;
    }

    /**
     * Actually places the word on the board
     * @param word a given word to actually place on the board
     */
    private void wordPlacing(Word word){
        int row = word.getRow();
        int col = word.getCol();
        int len = word.getTiles().length;
        for(int i=0;i<len;i++){
            if(gameBoard[row][col].getTile()==null){
                gameBoard[row][col].setTile(word.getTiles()[i]);
            }
            if(word.isVertical())
                row++;
            else{
                col++;
            }
        }
    }

    public void setTiles(Tile[][] tiles){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                gameBoard[i][j].setTile(tiles[i][j]);
            }
        }
    }

    /*
    //////////////Old Functions instead of GetWord///////////////////

    //finds new word from left
    private Word leftWord(int row, int col, Word w, int index) {
        Word toReturn;
        Tile[] wordTiles;
        int startCol = col;
        int endCol = col;
        int len;

        while (startCol - 1 >= 0 && gameBoard[row][col - 1] != null)
            startCol--;
        while ((endCol + 1 < size && gameBoard[row][startCol + 1] != null) || endCol<col)
            endCol++;
        if(!w.isVertical())
            endCol=w.getCol()-1+w.getTiles().length;
        len = endCol - startCol + 1;
        wordTiles = new Tile[len];
        for (int i = 0; i < len; i++) {
            wordTiles[i] = gameBoard[row][startCol + i].getTile();
            if(wordTiles[i]==null) {
                wordTiles[i] = w.getTiles()[index];
                index++;
            }
        }
        toReturn = new Word(wordTiles, row, startCol, false);
        return toReturn;
    }

    //finds new word from right
    private Word rightWord(int row, int col, Word w, int index) {
        Word toReturn;
        Tile[] wordTiles;
        int startCol = col;
        int endCol = col;
        int len;
        while (endCol + 1 < size && gameBoard[row][endCol + 1].getTile() != null)
            endCol++;
        len = endCol - startCol + 1;
        wordTiles = new Tile[len];
        for (int i = 0; i < len; i++) {
            wordTiles[i] = gameBoard[row][col + i].getTile();
            if(wordTiles[i]==null){
                wordTiles[i] = w.getTiles()[index];
            }
        }
        toReturn = new Word(wordTiles, row, startCol, false);
        return toReturn;
    }

    //finds new word from top
    private Word topWord(int row, int col, Word w, int index) {
        Word toReturn;
        Tile[] wordTiles;
        int startRow = row;
        int endRow = row;
        int len;



        while (startRow - 1 >= 0 && gameBoard[startRow - 1][col].getTile() != null)
            startRow--;
        while ((endRow + 1 < size && gameBoard[endRow + 1][col].getTile() != null) || endRow<row)
            endRow++;
        if(w.isVertical()) //added
            endRow=w.getRow()-1+w.getTiles().length; //added
        len = endRow - startRow + 1;
        wordTiles = new Tile[len];
        for (int i = 0; i < len; i++) {
            wordTiles[i] = gameBoard[startRow + i][col].getTile();
            if(wordTiles[i]==null) {
                wordTiles[i] = w.getTiles()[index];
                index++; //added
            }
        }

        toReturn = new Word(wordTiles, startRow, col, true);
        return toReturn;
    }

    //finds new word from bottom
    private Word bottomWord(int row, int col, Word w, int index) {
        Word toReturn;
        Tile[] wordTiles;
        int startRow = row;
        int endRow = row;
        int len;
        while (endRow + 1 < size && gameBoard[endRow + 1][col].getTile() != null)
            endRow++;
        len = endRow - startRow + 1;
        wordTiles = new Tile[len];
        for (int i = 0; i < len; i++) {
            wordTiles[i] = gameBoard[startRow + i][col].getTile();
            if(wordTiles[i]==null){
                wordTiles[i] = w.getTiles()[index];
            }
        }
        toReturn = new Word(wordTiles, startRow, col, true);
        return toReturn;
    }*/

    /**
     * Checks if the whole word is inside the board borders.
     * @param word a given word to check.
     * @return if the whole word is inside the borders or not.
     */
    private boolean isWordInBorders(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;
        if (col < 0 || col > 14 || row < 0 || row > 14) {
            return false;
        }
        if (word.isVertical() && len > size - row) {
            return false;
        }
        if ((!word.isVertical()) && len > size - col) {
            return false;
        }
        return true;
    }

    /**
     * checks if the word is placed on the Star Square (middle square)
     * @param word
     * @return if the given word touches the Star square
     */
    private boolean isWordOnStarSquare(Word word) {
        int col = word.getCol();
        int row = word.getRow();
        int len = word.getTiles().length;

        if (word.isVertical()) {
            if (col != 7 || row > 7 || row + size - 1 < 7) {
                return false;
            }
        } else {
            if (row != 7 || col > 7 || col + size - 1 < 7) {
                return false;
            }
        }
        return true;
    }

    /**
     * checks that the word doesn't change any letter
     * @param word
     * @return
     */
    private boolean donotReplaceLetter(Word word) {
        if (word.isVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                if (word.getTiles()[i] != null && gameBoard[word.getRow() + i][word.getCol()].getTile() != null)
                    return false;
                if (word.getTiles()[i] == null && gameBoard[word.getRow() + i][word.getCol()].getTile() == null)
                    return false;
            }
        } else {
            for (int i = 0; i < word.getTiles().length; i++) {
                if (word.getTiles()[i] != null && gameBoard[word.getRow()][word.getCol() + i].getTile() != null)
                    return false;
                if (word.getTiles()[i] == null && gameBoard[word.getRow()][word.getCol() + i].getTile() == null)
                    return false;
            }
        }
        return true;
    }

    /**
     * checks if the word leans on another word (or uses any letter of it)
     * @param word
     * @return
     */
    private boolean isLeaning(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        for (int i = 0; i < word.getTiles().length; i++) {
            if (row - 1 >= 0 && gameBoard[row - 1][col].getTile() != null)
                return true;
            if (row + 1 >= 0 && gameBoard[row + 1][col].getTile() != null)
                return true;
            if (col - 1 >= 0 && gameBoard[row][col - 1].getTile() != null)
                return true;
            if (col + 1 >= 0 && gameBoard[row][col + 1].getTile() != null)
                return true;

            if (word.isVertical()) {
                row += 1;
            } else {
                col += 1;
            }
        }
        return false;
    }

    /**
     * gets the full word without null tiles
     * @param word a given word that could be with under lines (for example F_RM).
     * @return the full word
     */
    private Word fullWord(Word word) {
        int len = word.getTiles().length;
        int col = word.getCol();
        int row = word.getRow();
        Word fullWord;
        Tile[] fullWordTiles = new Tile[len];
        for (int i = 0; i < len; i++) {
            if (word.getTiles()[i] != null) {
                fullWordTiles[i] = word.getTiles()[i];
            }
            else {
                if (word.isVertical()) {
                    fullWordTiles[i] = gameBoard[row + i][col].getTile();
                } else {
                    fullWordTiles[i] = gameBoard[row][col + i].getTile();
                }

            }
        }
        fullWord = new Word(fullWordTiles,row,col,word.isVertical());
        return fullWord;
    }

    /**
     * This class represents every square in the board - it has a Tile and a color
     */
    public static class BoardSquare {
        private int color;
        private Tile tile;

        public BoardSquare() {
            color = 0;
            tile = null;
        }

        /**
         *
         * @param color an int that represent the following colors:
         * Green = 0
         * Cyan - 1
         * Blue - 2
         * Yellow - 3
         * Red - 4
         * Star - 5
         */
        public void setColor(int color) {
            if (color >= 1 && color <= 5)
                this.color = color;
        }

        /**
         * initialize a given tile into a square
         * @param tile a tile to place on the square
         */
        public void setTile(Tile tile) {
            this.tile = tile;
        }

        /**
         * get the square color
         * @return the color of the square
         */
        public int getColor() {
            return color;
        }

        /**
         *
         * @return the tile that placed on this square
         */
        public Tile getTile() {
            return tile;
        }
    }

}
