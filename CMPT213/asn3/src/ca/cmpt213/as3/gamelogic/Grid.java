package ca.cmpt213.as3.gamelogic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {
    public List<Cell> theGrid = new ArrayList<Cell>();
    public boolean isCheatMode;
    public ArrayList<Integer> randNumbers = new ArrayList<Integer>();
    public int tokimonsRem; //gets initialized in method call initializeGrid in TokimonFinder
    int numFokimons;

    Grid(int numTokis, int numFokis, boolean cheatMode){
        isCheatMode = cheatMode;
        tokimonsRem = numTokis;
        numFokimons = numFokis;

        for(int i = 0; i < 100 ; i++){
            Cell addCell = new Cell();
            theGrid.add(addCell);
        }


        for(int i = 0 ; i < 100 ; i++){
            randNumbers.add(new Integer(i));
        }
        Collections.shuffle(randNumbers);


        for(int i = 0 ; i < numTokis ; i++){

            theGrid.get(randNumbers.get(i)).containsToki = true;

        }

        for(int i = numTokis ; i < numTokis + numFokis ; i++ ){

            theGrid.get(randNumbers.get(i)).containsFoki = true;
        }


    }


}
