package ca.cmpt213.as3.userInterface;

import ca.cmpt213.as3.gamelogic.Cell;
import ca.cmpt213.as3.gamelogic.Grid;
import ca.cmpt213.as3.gamelogic.Trainer;

import java.util.List;

public class DisplayGrid {

    public static void printGrid(Grid aGrid){

        String alphabet = "ABCDEFGHIJ";

        System.out.print("   ");
        for(int i = 0; i < 10 ; i++){
            System.out.print(i+1);
            System.out.print("  ");
        }
        System.out.print("\n");
        for(int i = 0; i < 10 ; i++){
            System.out.print(alphabet.charAt(i) + "  ");
            for(int j = 0; j < 10 ; j++){
                if(!aGrid.theGrid.get(i * 10 + j).isUndiscovered){
                    aGrid.theGrid.get(i * 10 + j).currentState = " ";
                }
                if(aGrid.theGrid.get(i * 10 + j).containsTrainer){

                    aGrid.theGrid.get(i * 10 + j).currentState = "@";
                }

                if(aGrid.isCheatMode) {
                    if(aGrid.theGrid.get(i * 10 + j).containsToki){
                        aGrid.theGrid.get(i * 10 + j).currentState = "$";
                        if(aGrid.theGrid.get(i * 10 + j).containsTrainer){
                            aGrid.theGrid.get(i * 10 + j).currentState = "@$";
                        }

                    }
                    else if(aGrid.theGrid.get(i * 10 + j).containsFoki){
                        aGrid.theGrid.get(i * 10 + j).currentState = "X";
                        if(aGrid.theGrid.get(i * 10 + j).containsTrainer){
                            aGrid.theGrid.get(i * 10 + j).currentState = "@X";
                        }
                    }
                    System.out.print(aGrid.theGrid.get(i * 10 + j).currentState + "  ");

                }
                else{
                    if(aGrid.theGrid.get(i * 10 + j).containsTrainer && aGrid.theGrid.get(i * 10 + j).containsToki){
                        aGrid.theGrid.get(i * 10 + j).currentState = "@$";
                    }
                    else if(aGrid.theGrid.get(i * 10 + j).containsTrainer && aGrid.theGrid.get(i * 10 + j).containsFoki){
                        aGrid.theGrid.get(i * 10 + j).currentState = "@X";
                    }
                    else if(!aGrid.theGrid.get(i * 10 + j).isUndiscovered){
                        if(aGrid.theGrid.get(i * 10 + j).containsToki){
                            aGrid.theGrid.get(i * 10 + j).currentState = "$";
                        }
                        else if(aGrid.theGrid.get(i * 10 + j).containsFoki){
                            aGrid.theGrid.get(i * 10 + j).currentState = "$";
                        }
                    }
                    System.out.print(aGrid.theGrid.get(i * 10 + j).currentState + "  ");
                }

            }
            System.out.print("\n");

        }
    }

    public static void printRemainingTokis(Trainer aTrainer) {
        System.out.print("Number of Tokis Left "+ aTrainer.tokimonsRemaining + ": ");
        for(int i =0; i<aTrainer.tokimonsRemaining; i++){
            System.out.print(" $");
            if(i %40 == 0 && i!=0){
                System.out.print("\n                         ");
            }
        }
        System.out.print("\n\n");
    }
}
