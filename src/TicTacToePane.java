import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicTacToePane extends VBox {

    //data
    private int wins, losses, ties = 0, numOfGames, turn = 0;
    private boolean gameOver = false;
    private boolean aiThinking = false;


    //Gui Components
    private Button[][] ticTacToeButtons;
    private final Label headerLabel;

    private final Label winsLabel;
    private final Label lossesLabel;
    private final Label tiesLabel;
    private final Label numOfGamesLabel;


    /**
     * This method contains the GUI design
     */
    public TicTacToePane() {

        ticTacToeButtons = new Button[3][3];

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        //sets panel
        for(int c = 0; c < 3; c++) {
            for(int r = 0; r < 3; r++) {

                Button button = new Button();
                button.setText("");
                button.setFont(new Font("Courier", 36));
                button.setAlignment(Pos.CENTER);
                button.getStyleClass().add("gameButton");
                button.setPrefHeight(100);
                button.setPrefWidth(100);
                button.setStyle("-fx-border-color: #000066");

                gridPane.add(button,r,c);
                ticTacToeButtons[c][r] = button;
                button.setOnAction(this::doButtonAction);

            }
        }

        headerLabel = new Label("X's turn. Click button to take spot");
        headerLabel.setFont(new Font(24));
        headerLabel.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 24));

        //Set stat labels
        this.winsLabel = new Label("Wins: " + wins);
        this.lossesLabel = new Label("Losses: " + losses);
        this.tiesLabel = new Label("Ties: " + ties);
        this.numOfGamesLabel = new Label("Games: " + numOfGames);

        VBox statBox = new VBox(this.winsLabel, this.lossesLabel, this.tiesLabel, this.numOfGamesLabel);
        statBox.setAlignment(Pos.CENTER_RIGHT);
        statBox.setSpacing(10);
        statBox.setPrefWidth(100);

        HBox middleBox = new HBox(statBox, gridPane);
        middleBox.setSpacing(20);

        setAlignment(Pos.CENTER);
        getChildren().addAll(headerLabel, middleBox);

        this.setStyle("-fx-background-color: #FFFF99");
        setData();

    }

    /**
     * This method decides who's turn it is and displays it.
     * @param event ActionEvent
     */
    private void doButtonAction(ActionEvent event) {
        System.out.println("doButtonAction");
        if(!gameOver && !aiThinking) {
            Button clickedBtn = (Button) event.getSource();

            //If spot taken, exit method
            if (clickedBtn.getText().length() > 0) {
                return;
            }
            //Spot not taken
            String place;
            if (turn % 2 == 0) {
                //currently X's turn
                place = "X";
            } else {
                //currently O's turn
                place = "O";
            }
            clickedBtn.setText(place);
            if(!checkForWinner().equals("")){
                return;
            }
            if(checkForTie()){
                return;
            }
            turn++;


            headerLabel.setText(String.format("%ss turn.",  turn % 2 == 0 ? "Your" : "PC"));

            if((turn % 2 != 0) && turn < 8){
                //AI's turn
                aiThinking = true;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            if (turn == 1) {
                                firstRound();
                            }else {
                                System.out.println("AI IS thinking");
                                doTurnForAI();
                            }
                                if (!checkForWinner().equals("")) {
                                    return;
                                }
                                if (checkForTie()) {
                                    return;
                                }
                                turn++;

                        });
                    }
                };
                ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.schedule(runnable, 1000, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * This method runs only on the first turn of AI in purpose of making it little dumb.
     */
    private void firstRound() {
        //TODO - Make smart
        //In meantime - AI is dumb
        if(!attemptDiagonalOneBlock()) {
            aiThinking = true;
            int r1 =  (int) (Math.random() * 3);
            int r2= (int) (Math.random() * 3);
            while(!ticTacToeButtons[r1][r2].getText().equals("")){
                r1 =  (int) (Math.random() * 3);
                r2= (int) (Math.random() * 3);
            }
            ticTacToeButtons[r1][r2].setText("O");
            aiThinking = false;
        }
    }

    /**
     * This method checks if game ends with draw.
     * @return boolean
     */
    private Boolean checkForTie(){
        if(turn >= 8) {

            gameOver = true;
            this.ties++;
            this.numOfGames++;
            headerLabel.setText("Game over... No winner.");
            writeData();
            resetGame();
            return true;
        }
        return false;
    }

    /**
     * This method starts running after turn 4 to detect if one of the sides win the game.
     * @return String
     */
    private String checkForWinner(){
        String place;
        if (turn % 2 == 0) {
            //currently X's turn
            place = "X";
        } else {
            //currently O's turn
            place = "O";
        }
        String whoTurn = turn % 2 == 0 ? "You" : "PC";
        if (turn >= 4) {
            //TODO - Check for win
            if (checkIfWon(place)) {
                //TODO - Stop game
                headerLabel.setText(String.format(whoTurn + " won!", place));
                gameOver = true;
                if(place.equals("X")){
                    this.wins++;
                    this.numOfGames++;
                    writeData();
                    resetGame();
                    return "X";
                }else{
                    this.losses++;
                    this.numOfGames++;
                    writeData();
                    resetGame();
                    return "O";
                }
            }
        }return "";
    }

    /**
     * This method uses the MINMAX algorithm for AI to play.
     */
    private void doTurnForAI() {

        //TODO - Make smart
        //In meantime - AI is dumb
        if(!attemptDiagonalOneBlock()) {
          //  ticTacToeButtons[(int)(Math.random()*3)][(int)(Math.random()*3)].fire();
            aiThinking = true;
              AIBoard.turnBack(ticTacToeButtons, MINMAX(AIBoard.makeBoard(ticTacToeButtons)));
            aiThinking = false;
         //   ticTacToeButtons[AIBoard.turnBack(MINMAX(AIBoard.makeBoard(ticTacToeButtons)))].fire();
        }
    }

    /**
     * This method uses diagonal block matrix.
     * @return boolean
     */
    private boolean attemptDiagonalOneBlock() {
        boolean hasOneX = false;
        boolean hasTwoX = false;
        int colBlank = -1;
        int rowBlank = -1;
        if(ticTacToeButtons[0][0].getText().equalsIgnoreCase("X")) {
            hasOneX = true;
        } else if (ticTacToeButtons[0][0].getText().length() == 0) {
            colBlank = 0;
            rowBlank = 0;
        } else {
            //you have an O in the way. not need to continue checking.
            return false;
        }

        if(ticTacToeButtons[1][1].getText().equalsIgnoreCase("X")) {
            if(hasOneX) {
                hasTwoX = true;
            } else {
                hasOneX = true;
            }
        } else if (ticTacToeButtons[0][0].getText().length() == 0) {
            colBlank = 1;
            rowBlank = 1;
        } else {
            //you have an O in the way. not need to continue checking.
            return false;
        }

        if(ticTacToeButtons[2][2].getText().equalsIgnoreCase("X")) {
            if(hasOneX && !hasTwoX) {
                hasTwoX = true;
            } else if(hasTwoX) {
                //X won, but logic to win should have found this
                return false;
            } else {
                return false;
            }
        } else if (ticTacToeButtons[0][0].getText().length() == 0) {
            colBlank = 2;
            rowBlank = 2;
        } else {
            //you have an O in the way. not need to continue checking.
            return false;
        }

        if(hasOneX && hasTwoX) {

            ticTacToeButtons[colBlank][rowBlank].fire();
            return true;
        }
        return false;
    }

    /**
     * This method check every possibility to win game.
     * @param player String
     * @return boolean
     */
    private boolean checkIfWon(String player) {
        //TODO - Check ticTacToeButtons array to see if player won.

        //TODO -use for loop to check all cols vertically
        if(player.equals(ticTacToeButtons[0][0].getText()) &&
                player.equals(ticTacToeButtons[0][1].getText()) &&
                player.equals(ticTacToeButtons[0][2].getText())) {
            return true;
        }else if(player.equals(ticTacToeButtons[1][0].getText()) &&
                player.equals(ticTacToeButtons[1][1].getText()) &&
                player.equals(ticTacToeButtons[1][2].getText())) {
            return true;
        }else if(player.equals(ticTacToeButtons[2][0].getText()) &&
                player.equals(ticTacToeButtons[2][1].getText()) &&
                player.equals(ticTacToeButtons[2][2].getText())) {
            return true;
        }
        //TODO - use for loop to check all rows horizontally
        if(player.equals(ticTacToeButtons[0][0].getText()) &&
                player.equals(ticTacToeButtons[1][0].getText()) &&
                player.equals(ticTacToeButtons[2][0].getText())) {
            return true;
        }else if(player.equals(ticTacToeButtons[0][1].getText()) &&
                player.equals(ticTacToeButtons[1][1].getText()) &&
                player.equals(ticTacToeButtons[2][1].getText())) {
            return true;
        }else if(player.equals(ticTacToeButtons[0][2].getText()) &&
                player.equals(ticTacToeButtons[1][2].getText()) &&
                player.equals(ticTacToeButtons[2][2].getText())) {
            return true;
        }
        //TODO - check diagonally both ways
        if(player.equals(ticTacToeButtons[0][0].getText()) &&
                player.equals(ticTacToeButtons[1][1].getText()) &&
                player.equals(ticTacToeButtons[2][2].getText())) {
            return true;
        }else return player.equals(ticTacToeButtons[2][0].getText()) &&
                player.equals(ticTacToeButtons[1][1].getText()) &&
                player.equals(ticTacToeButtons[0][2].getText());
    }

    /**
     * This method writes game data on the file and saves it for next games.
     */
    private void writeData(){

        try {
            FileWriter fw = new FileWriter("data.txt");


            fw.write("" + this.wins);
            fw.write("\n");
            fw.write("" + this.losses);
            fw.write("\n");
            fw.write("" + this.ties);
            fw.write("\n");
            fw.write("" + this.numOfGames);
            fw.write("\n");


            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method recalls the saved data from the local file.
     */
    private void setData() {
        //read a file for data: wins, losses, ties, numbersOfGames
        File dataFile = new File("data.txt");
        try {

            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
            this.wins = Integer.parseInt(bufferedReader.readLine());
            this.losses = Integer.parseInt(bufferedReader.readLine());
            this.ties = Integer.parseInt(bufferedReader.readLine());
            this.numOfGames = Integer.parseInt(bufferedReader.readLine());

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //If file can't be read, set data
            this.wins = 0;
            this.losses = 0;
            this.ties = 0;
            this.numOfGames = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }


        //TODO - Read file and set wins, losses, ties, numberOfGames.
        this.winsLabel.setText("Wins: " + this.wins);
        this.lossesLabel.setText("Losses: " + this.losses);
        this.tiesLabel.setText("Ties: " + this.ties);
        this.numOfGamesLabel.setText("Games: " + this.numOfGames);

    }

    /**
     * This method resets game by emptying the all boxes.
     */
    private void resetGame() {
        for(int c = 0; c < 3; c++) {
            for(int r = 0; r < 3; r++) {
                ticTacToeButtons[c][r].setText("");
            }
        }
        setData();
        turn = 0;
        gameOver = false;
        headerLabel.setText("X's turn. Click button to take spot");

    }


    /**
     * The MINMAX is a algorithm to find out best possible play.
     * The MINMAX checks every possible move and assigns a value to each play depending on its result.
     * In the end it helps AI to play best play.
     * @param currentBoard AIBoard
     * @return AIBoard
     */
    public AIBoard MINMAX(AIBoard currentBoard) {
        List<Integer> getPossibleMoves = currentBoard.openSpaces();

        List<AIBoard> possibleBoards = new ArrayList<>();

        for (Integer getPossibleMove : getPossibleMoves) {
            AIBoard tempBoard = new AIBoard(currentBoard.getBoard());
            tempBoard.makeMove(getPossibleMove, "ai");
            possibleBoards.add(MINMAX_HELPER(tempBoard, "human"));
        }


        return getMaxBoard(possibleBoards);
    }

    /**
     * This method finds the best possible score by checking all possible scores.
     * @param possibleBoards List<AIBoard>
     * @return int
     */
    public int getMaxPoint(List<AIBoard> possibleBoards) {
        int currMax = possibleBoards.get(0).getScore();
        for(int i = 1; i < possibleBoards.size(); i++)
        {
            currMax = Math.max(currMax, possibleBoards.get(i).getScore());
        }
        return currMax;
    }

    /**
     * This method finds the worst possible score by checking all possible scores.
     * @param possibleBoards List<AIBoard>
     * @return
     */
    int getMinPoint(List<AIBoard> possibleBoards) {
        int currMin = possibleBoards.get(0).getScore();
        for(int i = 1; i < possibleBoards.size(); i++)
        {
            currMin = Math.min(currMin, possibleBoards.get(i).getScore());
        }
        return currMin;
    }

    /**
     *This method returns the best plays'.
     * @param possibleBoards List<AIBoard>
     * @return AIBoard
     */
    public AIBoard getMaxBoard(List<AIBoard> possibleBoards) {
        int maxIndex = 0;
        int maxPoint = possibleBoards.get(0).getScore();
        for(int i = 1; i < possibleBoards.size(); i++)
        {
            if(possibleBoards.get(i).getScore() > maxPoint)
            {
                maxPoint = possibleBoards.get(i).getScore();
                maxIndex = i;
            }
        }
        return possibleBoards.get(maxIndex);
    }


    /**
     * This method gives scores to the all possible plays depending on who win and returns the best play for AI.
     * @param currentBoard AIBoard
     * @param turn String
     * @return AIBoard
     */
    public AIBoard MINMAX_HELPER(AIBoard currentBoard, String turn) {

        if(currentBoard.isFinished())
        {
            if(currentBoard.didSomeoneWin("O"))
            {
                currentBoard.setScore(1);
            } else if(currentBoard.didSomeoneWin("X"))
            {
                currentBoard.setScore(-1);

            } else
            {
                currentBoard.setScore(0);
            }

            return currentBoard;
        }

        List<Integer> getPossibleMoves = currentBoard.openSpaces();

        List<AIBoard> possibleBoards = new ArrayList<>();

        for (Integer getPossibleMove : getPossibleMoves) {
            AIBoard tempBoard = new AIBoard(currentBoard.getBoard());
            tempBoard.makeMove(getPossibleMove, turn);
            String tempTurn = turn;
            if (tempTurn.equals("ai")) {
                tempTurn = "human";
            } else {
                tempTurn = "ai";
            }
            possibleBoards.add(MINMAX_HELPER(tempBoard, tempTurn));
        }

        if(turn.equals("ai"))
        {
            // max my output
            currentBoard.setScore(getMaxPoint(possibleBoards));
            return currentBoard;
        }

        currentBoard.setScore(getMinPoint(possibleBoards));
        return currentBoard;
    }
}
