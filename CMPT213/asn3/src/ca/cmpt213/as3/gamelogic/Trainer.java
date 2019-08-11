package ca.cmpt213.as3.gamelogic;

import ca.cmpt213.as3.userInterface.UserInputOutput;

public class Trainer {
    int trainerPos[] = new int[2];
    int trainerPrevPos[] = new int[2];
    public int tokimonsRemaining; //gets initialized in method call initializeGrid in TokimonFinder
    int fokimonsRemaining;


    public boolean setInitialPos(String initialPos, Grid aGrid, UserInputOutput gameUI){
        int col =0;
        int row =0;
        //Find row and col
        col =   Integer.parseInt(initialPos.substring(1,initialPos.length()))-1;
        //System.out.println(initialPos.substring(1,initialPos.length()));
        if(col<0 || col >9){
            System.out.println("Sorry col is not in range 1 to 10, please try again");
            return false;
        }
        //Error handling use switch default

        switch(initialPos.substring(0,1).toUpperCase()){
            case "A" :
                row = 0;
                break;
            case "B" :
                row=1;
                break;
            case "C" :
                row = 2;
                break;
            case "D" :
                row = 3;
                break;
            case "E" :
                row = 4;
                break;
            case "F" :
                row = 5;
                break;
            case "G" :
                row = 6;
                break;
            case "H" :
                row = 7;
                break;
            case "I" :
                row = 8;
                break;
            case "J" :
                row = 9;
                break;
            default:
                System.out.println("Input for row "+ initialPos.substring(0,1)+" is not a valid row\n Try again");

                return false;
        }
        this.trainerPos[0] = row;
        this.trainerPos[1] = col;

        //from here to   ..... pass in trainerPos
        aGrid.theGrid.get(row*10 + col).containsTrainer = true;


        //aGrid.updateTokimonRemaining(trainerPos); //keep this
        if(aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).containsToki && aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).isUndiscovered ){
            this.tokimonsRemaining--;
            gameUI.printTokimonFound(); //moving toki remaining to Grid

        }
        aGrid.theGrid.get(row*10 + col).isUndiscovered = false;
        //here should be a function in aGrid that also passes in the god damn tokis reamasining

        return true;

    }
    public boolean updatePlayerMove(String newMove, Grid aGrid, UserInputOutput gameUI) {
        aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).containsTrainer = false;

        switch (newMove.trim().toLowerCase()) {
            case "w":
                this.trainerPos[0]--;
                break;
            case "a":
                this.trainerPos[1]--;
                break;
            case "s":
                this.trainerPos[0]++;
                break;
            case "d":
                this.trainerPos[1]++;
                break;

            default:
                 System.out.println("Invalid player move trainer. Try again:");
                 return false;
        }
        aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).containsTrainer = true;

        if(aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).containsToki && aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).isUndiscovered ){
            this.tokimonsRemaining--;
            gameUI.printTokimonFound();


        }

        aGrid.theGrid.get(this.trainerPos[0] * 10 + this.trainerPos[1]).isUndiscovered = false;
        return true;
    }
}
