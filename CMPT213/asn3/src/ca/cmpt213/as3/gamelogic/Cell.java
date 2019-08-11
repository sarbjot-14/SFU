package ca.cmpt213.as3.gamelogic;

public class Cell {

    public String currentState;
    public boolean containsToki;
    public boolean containsFoki;
    public boolean containsTrainer;
    public boolean isUndiscovered;

    Cell(){
        currentState =  "~";
        containsTrainer = false;
        containsFoki = false;
        containsToki = false;
        isUndiscovered = true;
    }
}
