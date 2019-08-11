package ca.cmpt213.as3.gamelogic;

import ca.cmpt213.as3.*;
import ca.cmpt213.as3.userInterface.DisplayGrid;
import ca.cmpt213.as3.userInterface.UserInputOutput;

import static java.lang.Integer.parseInt;

public class TokimonFinder {
    public static void main(String args[]){
        String initialPos;
        String newMove;
        boolean initSuccess= false;
        boolean updateSuccess = false;
        Trainer aTrainer = new Trainer();

        Grid gameGrid = initializeGrid(args, aTrainer);

        UserInputOutput gameUI = new UserInputOutput();

        while(!initSuccess) {
            //get initial position
            initialPos = gameUI.getInitialPos();
            //set initial position
            initSuccess = aTrainer.setInitialPos(initialPos, gameGrid, gameUI);
        }
        //gameGrid.setInitialPos(initialPos);

        DisplayGrid.printRemainingTokis(aTrainer);
        DisplayGrid.printGrid(gameGrid);


        while(aTrainer.tokimonsRemaining>0) {
            //get player move
            //newMove = gameUI.getPlayerMove();
            updateSuccess =false;
            while(!updateSuccess) {
                newMove = gameUI.getPlayerMove();
                updateSuccess = aTrainer.updatePlayerMove(newMove, gameGrid, gameUI);

            }
            DisplayGrid.printRemainingTokis(aTrainer);
            DisplayGrid.printGrid(gameGrid);

            if(aTrainer.tokimonsRemaining==0){
                System.out.println("you won!");
            }
        }


    }

    public static Grid initializeGrid(String args[], Trainer theTrainer){
        int startOfNumber;
        int numTokis = 10; //default
        int numFokis = 5; //default

        boolean isCheatMode = false;
        for(String argument : args){

            if(argument.trim().equals("--cheat")) {
                isCheatMode = true;
            }
            else if(argument.trim().contains("numToki")){
                startOfNumber = args[0].indexOf("=") +1;

                numTokis = parseInt(args[0].substring(startOfNumber));

            }
            else if(argument.trim().contains("numFoki")){
                startOfNumber = argument.indexOf("=") +1;

                numFokis = parseInt(argument.substring(startOfNumber));
            }

        }
        //***********ERROR HANDLING***************
        if(numFokis + numTokis>100){
            System.out.println("sorry too many Tokimons and Fokimons entered");
            System.exit(-1);
        }
        if(numFokis<5 && numTokis < 5){
            System.out.println("Not enough Tokimons or Fokimons specified");

        }

        theTrainer.fokimonsRemaining= numFokis; //moving this variable to Grid
        theTrainer.tokimonsRemaining = numTokis;

        Grid gameGrid = new Grid(numTokis, numFokis, isCheatMode);
        return gameGrid;
    }
}
