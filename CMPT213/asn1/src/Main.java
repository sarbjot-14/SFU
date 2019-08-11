import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String[] menuOptions = { "List Tokimons", "Add a new Tokimon", "Remove a Tokimon", "Change Tokimon strength","DEBUG: Dump objects (toString)", "Exit"};
        String mainMenuTitle = "Main Menu";
        String programTitle = "Tokimon Run by Sarbjot Singh SN 301313399" ;
        String tokimonListTitle = "List of Tokimons";
        String[] emptyOptions = {};
        int tempStrength;
        int menuOptionNum=0;
        boolean isValidAbility=true;


        ArrayList<Tokimon> tokimonList = new ArrayList<Tokimon>();


        //print title of program (empty options because program title has no options
        Helper helperDisplay = new Helper(programTitle, emptyOptions );
        //print main menu with options
        helperDisplay.printMenuTitle(mainMenuTitle);
        helperDisplay.printMenuOptions(menuOptions);
        //takes input from user
        menuOptionNum = helperDisplay.userMenuSelection(menuOptions.length); //takes input from user
        //if user selects 6 exit while loop then end program
        while(menuOptionNum!= 6) {

            switch (menuOptionNum) {
                //if user would like to list current tokimon
                case 1:
                    if(!tokimonsExist()){
                        break;
                    }
                    helperDisplay.printMenuTitle(tokimonListTitle);
                    listTokimons(tokimonList);
                    break;
                //if user would like to add a tokimon
                case 2:
                    Tokimon tempToki = new Tokimon();
                    System.out.print("Enter Tokimon's name: ");
                    tempToki.name = in.nextLine();

                    System.out.print("Enter Tokimon's ability: ");
                    String inputAbility= in.nextLine();
                    tempToki.ability = helperDisplay.validateAbility(inputAbility);

                    System.out.print("Enter Tokimon's size: ");
                    tempToki.size = in.nextDouble();
                    in.nextLine();
                    tokimonList.add(tempToki);

                    break;

                //if user would like to remove tokimon
                case 3:
                    if(!tokimonsExist()){
                        break;
                    }
                    listTokimons(tokimonList);
                    System.out.println("Which one, enter number?");
                    menuOptionNum = helperDisplay.userMenuSelection(Tokimon.numbOfTokimon);
                    menuOptionNum--;
                    System.out.println("Tokimon " + tokimonList.get(menuOptionNum).name+ " has been removed");
                    tokimonList.remove(menuOptionNum);
                    Tokimon.numbOfTokimon--;
                    break;
                //if user would like to change strength of a tokimon
                case 4:
                    if(!tokimonsExist()){
                        break;
                    }
                    listTokimons(tokimonList);
                    System.out.println("(Enter 0 to cancel)");
                    System.out.println("Pick Tokimon by entering number: ");
                    menuOptionNum = helperDisplay.userMenuSelection(Tokimon.numbOfTokimon);
                    if(menuOptionNum==0){
                        break;
                    }
                    menuOptionNum--;
                    System.out.println("By how much:");

                    tempStrength = validStrengthInput();

                    tokimonList.get(menuOptionNum).strength = tempStrength;
                    System.out.println(tokimonList.get(menuOptionNum).name +" has changed strength to "+tokimonList.get(menuOptionNum).strength );
                    break;
                 //if user would like to debug and see properties of all tokimons in system
                case 5:
                    if(!tokimonsExist()){
                        break;
                    }
                    for(Tokimon object: tokimonList){
                        System.out.println(object);
                    }

                default:
                    assert false;
                    break;

            }
            //reprint the main menu after finished with previous menu task
            helperDisplay.printMenuTitle(mainMenuTitle);
            helperDisplay.printMenuOptions(menuOptions);
            menuOptionNum = helperDisplay.userMenuSelection(menuOptions.length); //takes input from user
        }


    }
    //if menu option 1 is clicked then this method will display all tokimons and their properties
    public static void listTokimons(ArrayList<Tokimon> tokimonList){
        for(int i=0; i<Tokimon.numbOfTokimon; i++){

            System.out.println(i+1+". "+tokimonList.get(i).name +" "+ tokimonList.get(i).size+"m, " +tokimonList.get(i).ability+" ability, "+ tokimonList.get(i).strength+" strength\n");
        }
    }
    //verify if input is between 0 and 100
    public static int validStrengthInput(){
        Scanner in = new Scanner(System.in);
        int tempStrength = in.nextInt();
        while(tempStrength < 0 || tempStrength > 100){
            System.out.println("Enter strength between 1 and 100:");
            tempStrength = in.nextInt();
        }
        return tempStrength;
    }
    public static boolean tokimonsExist(){
        if(Tokimon.numbOfTokimon<1){
            System.out.println("Sorry, no existing Tokimons. Please add with menu option 2");
            return false;
        }
        else{
            return true;
        }
    }
}
