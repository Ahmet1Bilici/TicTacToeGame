import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class AIBoard {

    List<String> vals;
    int minmaxScore;

    /**
     * Constructor for AIBoard.
     * @param newBoardButtonList List<String>
     */
    public AIBoard(List<String> newBoardButtonList) {
        vals = new ArrayList<>();
        vals.addAll(newBoardButtonList);
        // this.vals = newBoardButtonList;
    }


    /**
     * This method creates a game board in List format.
     * @param ticTacToeButtons Button[][]
     * @return AIBoard
     */
    public static AIBoard makeBoard(Button[][] ticTacToeButtons) {
        List<String> newList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //System.out.println(ticTacToeButtons[i][j].getText());
                newList.add(ticTacToeButtons[i][j].getText());
            }

        }

        return new AIBoard(newList);
    }

    /**
     * This method recreates the game board in 2D array format.
     * @param newList2D Button[][]
     * @param newBoard AIBoard
     * @return Button[][]
     */
    public static Button[][] turnBack(Button[][] newList2D, AIBoard newBoard) {

        int iter = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //System.out.println(newBoard.getBoard().get(iter));
                newList2D[i][j].setText(newBoard.getBoard().get(iter));
                iter++;
            }
        }

        return newList2D;
    }

    /**
     * Getter for minmaxScore
     * @return int
     */
    public int getScore() {
        return minmaxScore;
    }

    /**
     * Setter for minmaxScore
     * @param newScore int
     */
    public void setScore(int newScore) {
                        minmaxScore = newScore;
    }

    /**
     * Getter for vals.
     * @return List<String>
     */
    public List<String> getBoard() {
        return vals;
    }

    /**
     * Setter for vals.
     * @param tempBoard List<String>
     */
    public void setBoard(List<String> tempBoard) {
        vals = tempBoard;
    }

    /**
     * This method returns all empty spaces
     * @return List<Integer>
     */
    public List<Integer> openSpaces() {
        List<Integer> tempSpaceCol = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (vals.get(i).equals("")) {
                //System.out.println(i);
                tempSpaceCol.add(i);
            }
        }
        return tempSpaceCol;
    }

    /**
     * This method used to print board for test purpose
     */
    public void printBoard() {

        for (int i = 0; i < vals.size(); i++) {
            if ((i + 1) % 3 == 0) {
                //System.out.print(vals.get(i) + "\n");
            } else {
                //System.out.print(vals.get(i));
            }
        }

    }

    /**
     * This method checks if game is finished by any side
     * @return boolean
     */
    public boolean isFinished() {
        if (vals.get(0).equals(vals.get(1))
                && vals.get(1).equals(vals.get(2))
                && !vals.get(0).equals("")) {
            return true;
        }
        if (vals.get(3).equals(vals.get(4))
                && vals.get(4).equals(vals.get(5))
                && !vals.get(3).equals("")) {
            return true;
        }
        if (vals.get(6).equals(vals.get(7))
                && vals.get(7).equals(vals.get(8))
                && !vals.get(6).equals("")) {
            return true;
        }
        if (vals.get(0).equals(vals.get(3))
                && vals.get(3).equals(vals.get(6))
                && !vals.get(0).equals("")) {
            return true;
        }
        if (vals.get(1).equals(vals.get(4))
                && vals.get(4).equals(vals.get(7))
                && !vals.get(1).equals("")) {
            return true;
        }
        if (vals.get(2).equals(vals.get(5))
                && vals.get(5).equals(vals.get(8))
                && !vals.get(2).equals("")) {
            return true;
        }
        if (vals.get(0).equals(vals.get(4))
                && vals.get(4).equals(vals.get(8))
                && !vals.get(0).equals("")) {
            return true;
        }
        if (vals.get(2).equals(vals.get(4))
                && vals.get(4).equals(vals.get(6))
                && !vals.get(2).equals("")) {
            return true;
        }

        for (int i = 0; i < 9; i++) {
            if (vals.get(i).equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if passed player won.
     * @param player String
     * @return boolean
     */
    public boolean didSomeoneWin(String player) {
        // ai always plays O
        if (vals.get(0).equals(vals.get(1))
                && vals.get(1).equals(vals.get(2))
                && vals.get(0).equals(player)) {
            return true;
        }
        if (vals.get(3).equals(vals.get(4))
                && vals.get(4).equals(vals.get(5))
                && vals.get(3).equals(player)) {
            return true;
        }
        if (vals.get(6).equals(vals.get(7))
                && vals.get(7).equals(vals.get(8))
                && vals.get(6).equals(player)) {
            return true;
        }
        if (vals.get(0).equals(vals.get(3))
                && vals.get(3).equals(vals.get(6))
                && vals.get(0).equals(player)) {
            return true;
        }
        if (vals.get(1).equals(vals.get(4))
                && vals.get(4).equals(vals.get(7))
                && vals.get(1).equals(player)) {
            return true;
        }
        if (vals.get(2).equals(vals.get(5))
                && vals.get(5).equals(vals.get(8))
                && vals.get(2).equals(player)) {
            return true;
        }
        if (vals.get(0).equals(vals.get(4))
                && vals.get(4).equals(vals.get(8))
                && vals.get(0).equals(player)) {
            return true;
        }
        if (vals.get(2).equals(vals.get(4))
                && vals.get(4).equals(vals.get(6))
                && vals.get(2).equals(player)) {
            return true;
        }
        return false;
    }

    /**
     * This method places X or O depending on turn
     * @param i int
     * @param turn String
     */
    public void makeMove(int i, String turn) {
        if (turn.equals("ai")) {
            vals.set(i, "O");
        } else {
            vals.set(i, "X");
        }
        printBoard();
    }

}