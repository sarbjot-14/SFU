import java.util.Scanner;

/**
 * Helper class used to create menu's, menu titles with stars around it,
 * Number menu options. Provides user with prompt to take in values.
 */
public class Helper {
    Scanner in = new Scanner(System.in);
    private String[] abilities= {"fly", "fight", "throw fire", "spray water", "electrify", "freeze"};

    Helper(String menuTitle , String[] menuOptions){

        printMenuTitle(menuTitle);
        printMenuOptions(menuOptions);

    }
    public void printMenuTitle(String menuTitle){

        int titleLen  = menuTitle.length() + 4;
        //menu title with stars
        for(int i=0; i<titleLen; i++){ //create * around title
            System.out.print("*");
        }
        System.out.print("\n*");
        System.out.println(" " + menuTitle + " *");
        for(int i=0; i<titleLen; i++){
            System.out.print("*");
        }
        System.out.print("\n");


    }
    public void printMenuOptions(String[] menuOptions){
        int menuLen = menuOptions.length + 1;
        for(int i = 1; i < menuLen; i++ ){ //uses i to number options
            System.out.println(" " + i + " " + menuOptions[i-1]);
        }
    }

    /**
     * This method is used to get users menu selection.
     * @param len This is the number of options available
     * @return int This returns the users menu options
     */

    public int userMenuSelection(int len){
        int number;
        System.out.println(">");
        number = in.nextInt();
        while(number < 0 || number > len){ //re ask until selection is valid option
            System.out.println("Try again. Select option from 1 to "+len+"\n > ");
            number = in.nextInt();
        }
        return number;

    }

    /**
     * Check to see if ability is from the valid abilities. If not ask to retry.
     * @param inputAbility ability inputted from user
     * @return return valid ability
     */
    public String validateAbility(String inputAbility){
        in.nextLine();

        boolean isValidAbility=false;
        while(!isValidAbility) { //re ask for ability until valid ability is chosen
            for (String str : abilities) { //try to match input with array of valid abilities
                if (str.equals(inputAbility)) {
                    return inputAbility; //return if valid ability found
                }
            }
            //if valid ability not found then re-ask
            System.out.println("Please choose ability from following: ");
            for (String str : abilities) {
                System.out.print(" '"+str+"' ");
            }
            System.out.print("\n>");

            inputAbility = in.nextLine();

        }
        return inputAbility;
    }

}
