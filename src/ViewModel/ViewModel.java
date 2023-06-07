package ViewModel;

import Model.Model;

import java.util.*;

public class ViewModel implements Observer {
    //    data binding
    public Character[][] board;
    public List<DataChanges> changesList;
    Model model;
    int currentPlayerId;
    List<Character> myHand;
    private String word;
    private int wordStartRow;
    private int wordStartCol;

    public ViewModel() {
        board = new Character[15][15];
        changesList = new ArrayList<>();
        currentPlayerId = 0;
        myHand = new ArrayList<>();
        // Todo: add more initialization here
    }

    /**
     * Run when the player clicks the "Place Word" button.
     * Tries to place the word on the board if it is valid (from changesList) using the Model.
     * If the word is invalid, notify the view
     */
    public void tryPlaceWord() {
        if (isChangeValid()) {
            model.tryPlaceWord(getWord(), getWordStartRow(), getWordStartCol(), isWordVertical());
            word = null;
            wordStartRow = -1;
            wordStartCol = -1;
            return;
        }
        // Todo: notify the view that the change is invalid
    }

    /**
     * @return true if the word is valid, false otherwise (from changesList)
     */
    public boolean isChangeValid() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        int numberOfColRowChanges = 0;
        int colForCheck = sortedChangesList.get(0).getNewCol();
        int rowForCheck = sortedChangesList.get(0).getNewRow();
        for (DataChanges dc : sortedChangesList) {
            if (dc.getNewCol() != colForCheck) {
                numberOfColRowChanges++;
                colForCheck = dc.getNewCol();
            }
            if (dc.getNewRow() != rowForCheck) {
                numberOfColRowChanges++;
                rowForCheck = dc.getNewRow();
            }
        }

        return numberOfColRowChanges == sortedChangesList.size() - 1;
    }

    /**
     * @return the word that the player is trying to place
     */
    public String getWord() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        StringBuilder sb = new StringBuilder();
        if (isWordVertical()) {
            int startRow = sortedChangesList.get(0).getNewRow();
            while (startRow > 0 && board[startRow - 1][sortedChangesList.get(0).getNewCol()] != null) {
                startRow--;
            }
            for (int i = startRow; i < sortedChangesList.get(0).getNewRow(); i++) {
                sb.append('_');
            }
        } else {
            int startCol = sortedChangesList.get(0).getNewCol();
            while (startCol > 0 && board[sortedChangesList.get(0).getNewRow()][startCol - 1] != null) {
                startCol--;
            }
            for (int i = startCol; i < sortedChangesList.get(0).getNewCol(); i++) {
                sb.append('_');
            }
        }
        int row = changesList.get(0).getNewRow();
        int col = changesList.get(0).getNewCol();
        for (DataChanges dc : sortedChangesList) {
            if (dc.getNewCol() > col + 1 || dc.getNewRow() > row + 1) {
                for (int i = 0; i < dc.getNewCol() - col - 1; i++) {
                    sb.append('_');
                }
            }
            sb.append(dc.getLetter());
            col = dc.getNewCol();
            row = dc.getNewRow();
        }

        if (isWordVertical()) {
            int endRow = sortedChangesList.get(sortedChangesList.size() - 1).getNewRow();
            while (endRow < 14 && board[endRow + 1][sortedChangesList.get(0).getNewCol()] != null) {
                endRow++;
            }
            for (int i = endRow; i > sortedChangesList.get(sortedChangesList.size() - 1).getNewRow(); i--) {
                sb.append('_');
            }
        } else {
            int endCol = sortedChangesList.get(sortedChangesList.size() - 1).getNewCol();
            while (endCol < 14 && board[sortedChangesList.get(0).getNewRow()][endCol + 1] != null) {
                endCol++;
            }
            for (int i = endCol; i > sortedChangesList.get(sortedChangesList.size() - 1).getNewCol(); i--) {
                sb.append('_');
            }
        }
        this.word = sb.toString();
        return this.word;
    }

    /**
     * @return the row of the first letter of the word
     */
    public int getWordStartRow() {
        int minRow = changesList.get(0).getNewRow();
        for (DataChanges dc : changesList) {
            if (dc.getNewRow() < minRow) minRow = dc.getNewRow();
        }
        if (isWordVertical()) {
            for (Character c : this.word.toCharArray()) {
                if (c == '_') minRow--;
                else break;
            }
        }
        this.wordStartRow = minRow;
        return this.wordStartRow;
    }

    /**
     * @return the column of the first letter of the word
     */
    public int getWordStartCol() {
        int minCol = changesList.get(0).getNewCol();
        for (DataChanges dc : changesList) {
            if (dc.getNewCol() < minCol) minCol = dc.getNewCol();
        }
        if (!isWordVertical()) {
            for (Character c : this.word.toCharArray()) {
                if (c == '_') minCol--;
                else break;
            }
        }
        this.wordStartCol = minCol;
        return this.wordStartCol;
    }

    /**
     * @return true if the word is vertical, false if it is horizontal
     */
    public boolean isWordVertical() {
        List<DataChanges> sortedChangesList = getSortedChangesListByRowCol();
        return sortedChangesList.get(0).getNewCol() == sortedChangesList.get(1).getNewCol();
    }

    private List<DataChanges> getSortedChangesListByRowCol() {
        List<DataChanges> sortedChangesList = new ArrayList<>();
        sortedChangesList.addAll(changesList);
        sortedChangesList.sort((o1, o2) -> {
            if (o1.getNewRow() == o2.getNewRow()) {
                return o1.getNewCol() - o2.getNewCol();
            }
            return o1.getNewRow() - o2.getNewRow();
        });
        return sortedChangesList;
    }

    private Character[][] stringToMatrix(String boardStatusStr) {
        Character[][] boardStatus = new Character[15][15];
        for(int i = 0; i < 15; i++) {
            for(int j = 0; j < 15; j++) {
                boardStatus[i][j] = boardStatusStr.charAt(i * 15 + j);
            }
        }
        return boardStatus;
    }

    private void setBoard(Character[][] boardStatus) {
        this.board = boardStatus;
    }

    @Override
    public void update(Observable o, Object arg) {
        String messages = (String) arg;
        Queue<String> messagesQ = new LinkedList<>(Arrays.asList(messages.split("\n")));
        while (!messagesQ.isEmpty()) {
            String message = messagesQ.poll();
            int playerId = Integer.parseInt(message.split(":")[0]);
            String methodName = message.split(":")[1];
            String[] args = message.split(":")[2].split(",");

            switch (methodName) {
                case "boardUpdated":
                    setBoard(stringToMatrix(args[0]));
                    break;
                case "scoreUpdated":
                    break;
                case "numOfTilesUpdated":
                    break;
                case "setHand":
                    break;
                case "newPlayerTurn":
                    break;
                case "setId":
                    break;
                case "playerListUpdated":
                    break;
                case "startGame":
                    break;
                case "challenge":
                    break;
                case "tryPlaceWord":
                    break;
                case "tilesInBagUpdated":
                    break;
            }

        }
    }
}
