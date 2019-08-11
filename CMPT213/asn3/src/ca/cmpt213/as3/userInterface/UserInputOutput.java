package ca.cmpt213.as3.userInterface;

import ca.cmpt213.as3.gamelogic.Grid;

import java.util.Scanner;

public class UserInputOutput {
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    public String getInitialPos(){
        //Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a initial position: ");
        String position = reader.nextLine() ;// Scans the next token of the input as an int.

        return position;
    }

    public String getPlayerMove(){
        System.out.println("Enter player move. Only w,a,s,d or fdlf for spells: ");
        String playerMove = reader.nextLine();
        return playerMove;
    }
    public void printTokimonFound(){
        System.out.println("Congradulaitons you have found a Tokimon!!!");
    }
}
