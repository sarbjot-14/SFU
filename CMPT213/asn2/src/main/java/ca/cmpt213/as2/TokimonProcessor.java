package ca.cmpt213.as2;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.*;
import java.io.File;


public class TokimonProcessor {


    public static void main(String[] args) throws FileNotFoundException {

        List<TokimonTeam> allTokimonFeedbacks = new ArrayList<TokimonTeam>();

        String tokimonInJson;
        int creatingTeamNumber = 1;
        List<String> tempTeam = new ArrayList<String>();
        int tempTeamSize = 0;
        TokimonTeam feedbackOfTeam = null;
        Gson gson = new Gson();

        //error check arguments
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException iae) {

            System.out.println("Error: Incorrect number of arguments provided. Please proved two arguments");
            System.exit(-1); ;
        }
        //make write file
        FileWriter fileWriter = null;
        //create output csv file
        try {
            fileWriter = new FileWriter(args[1] + "/team_info.csv");
        } catch (IOException e) {
            System.out.println("Error: Output folder does not exist");
            return;
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        //print headers into output csv file
        printWriter.println("Team#,From Toki,To Toki,Score,Comment,,Extra");

        File file = new File(args[0]);
        try {
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        }
        catch(FileNotFoundException fnfe){
            System.out.println("Error: Input folder does not exist");
            return;
        }
        //recursively find all json files
        List<String> aList = new ArrayList<String>();
        aList = recurse(aList, file);
        File thisIsTokimonFile;

        //reading json objects and converting into java objects
        for (String member : aList) {
            thisIsTokimonFile =  new File(member);
            Scanner tokimonInput = new Scanner(thisIsTokimonFile);  //teamateInJason should have only one json file after filtering

            tokimonInJson = tokimonInput.nextLine();
            while (tokimonInput.hasNext()) {
                tokimonInJson += tokimonInput.nextLine();
            }

            //find errors in json files
            try {
                feedbackOfTeam = gson.fromJson(tokimonInJson, TokimonTeam.class);
            }
            catch(JsonSyntaxException jse){
                System.out.println("Error: Incorrect json format\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                System.exit(-1);
            }

            try{
                if(feedbackOfTeam.extra_comments == null){
                    System.out.println("Error: Fields Missing or Incorrect Field Input\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                    throw new Exception();
                }
                if(feedbackOfTeam.team.size()==0){
                    System.out.println("Error: Feedback of team not found\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                    throw new Exception();
                }

                for(Tokimon checkFields : feedbackOfTeam.team){
                    if(checkFields.name == null || checkFields.id == null || checkFields.compatibility.comment == null || checkFields.compatibility.score == 0.0){
                        System.out.println("Error: Fields Missing or Incorrect Field Input\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                        throw new Exception();
                    }
                    if(checkFields.compatibility.score<0){
                        System.out.println("Error: Score is less than 0\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                        throw new Exception();
                    }

                }
            }
            catch(Exception fieldIsMissing){
                //System.out.println("Error: Field Missing or Incorrect Field Input\nFile name: "+thisIsTokimonFile.getName()+"\nFile Path: " + thisIsTokimonFile.getAbsolutePath() );
                System.exit(-1);
            }


            //add java object of a Tokimon's feedbacks into an array
            allTokimonFeedbacks.add(feedbackOfTeam);

        }
        try{
            if(allTokimonFeedbacks.size() == 0){
                throw new Exception();
            }
        }
        catch(Exception noJsonFilesFound){
            System.out.println("Error: No Json files found");
            System.exit(-1);
        }
        /*
        Error Checking:
         • It is an error if Tokimon S is mentioned by Tokimons who are in two different
        teams. (i.e., S can only be a member of one team).
        • It is an error if any Tokimon who is mentioned in the
         */
        String idOfCorruptTokimon="a Tokimon";
        String idOfCorruptTokimonOtherTeam="a Tokimon";
        String traiterToki = "traiter";
        try{
            String checkingCurrentTeam;
            String innerString;
            String arr[];
            String innerArr[];
            String checkingCurrentToki;
            for(TokimonTeam incrementing : allTokimonFeedbacks ){
                arr= incrementing.team.get(0).name.split(" ", 2);
                checkingCurrentTeam= arr[0];
                for(Tokimon toki : incrementing.team){
                    checkingCurrentToki= toki.id;
                    for(TokimonTeam findingTraiter: allTokimonFeedbacks){
                        for(Tokimon allToki : findingTraiter.team){
                            innerArr = findingTraiter.team.get(0).name.split(" ", 2);
                            innerString = innerArr[0];

                            if (allToki.id.equalsIgnoreCase(toki.id) && !checkingCurrentTeam.equalsIgnoreCase(innerString)) {
                                idOfCorruptTokimon = findingTraiter.team.get(0).id;
                                idOfCorruptTokimonOtherTeam = incrementing.team.get(0).id;
                                traiterToki = toki.id;
                                throw new Exception();
                            }
                        }
                    }
                }

            }

        }
        catch(Exception traiter){
            //call method to print out file name and path and exit
            System.out.println("Error: Tokimon "+traiterToki+ " is in mentioned in feedbacks of two different teams");
            //System.out.println("traiter in " + idOfCorruptTokimon + "and" + idOfCorruptTokimonOtherTeam);
            File inputFile = new File(args[0]);
            System.out.println("Locations of error is: ");
            displayFileNameAndFilePath(idOfCorruptTokimon, inputFile);
            System.out.println("and here :");
            displayFileNameAndFilePath(idOfCorruptTokimonOtherTeam, inputFile);
            System.exit(-1);

        }
        /*
        Error Checking:
        • It is an error if any Tokimon who is mentioned in the feedback of another
Tokimon in the team fails to submit a JSON feedback file.
         */
        boolean foundFeedbackFile= false;
        File inputFileIs ;
        file = new File(args[0]);

        try {
            inputFileIs = new File(args[0]);
            List<String> tempTeamHasJsonFolder = new ArrayList<String>();
            for (TokimonTeam oneTokimonFeedback : allTokimonFeedbacks) {//

                //if(oneTokimonFeedback.whichTeam ==0) {

                tempTeamHasJsonFolder = new ArrayList<String>();
                tempTeamSize = oneTokimonFeedback.team.size();
                for (int p = 0; p < oneTokimonFeedback.team.size(); p++) {
                    tempTeamHasJsonFolder.add(p, oneTokimonFeedback.team.get(p).id);

                }
                for(String idOfTokiWithFolder : tempTeamHasJsonFolder){
                    foundFeedbackFile = searchForJsonFile(idOfTokiWithFolder, inputFileIs, foundFeedbackFile );
                    if(!foundFeedbackFile){
                        System.out.println("\nError: Could not find " + idOfTokiWithFolder + "'s json file, which is mentioned in the feed back of: "+ tempTeamHasJsonFolder.get(0).trim() );
                        //find the folder name and path to this guy cause he mentioned idOfTokiWithFolder that did not submit a folder
                        //System.out.println(tempTeamHasJsonFolder.get(0));
                        System.out.println("Source of error: " );
                        displayFileNameAndFilePath(tempTeamHasJsonFolder.get(0).trim(), file) ;

                        System.exit(-1);
                    }
                    foundFeedbackFile= false;


                }

            }
        }
        catch(Exception anException){


            System.exit(-1);
        }
        /*
        Error Checking:
        It is an error if any Tokimon in a team does not provide feedback about all other
        Tokimons in that team.
         */
        int fullTeam=0;
        int teamCount=0;
        file = new File(args[0]);
        String[] teamNumberString;
        String[] teamNumberStringb;
        try {
            for (TokimonTeam aTeam : allTokimonFeedbacks) {
                teamNumberString = aTeam.team.get(0).name.split(" ", 2);
                for (TokimonTeam bTeam : allTokimonFeedbacks) {
                    teamNumberStringb = bTeam.team.get(0).name.split(" ", 2);
                    if (teamNumberString[0].trim().equalsIgnoreCase(teamNumberStringb[0].trim())) {
                        fullTeam = aTeam.team.size();
                        for (Tokimon aToki : aTeam.team) {
                            for (Tokimon bToki : bTeam.team) {
                                if (aToki.id.trim().equalsIgnoreCase(bToki.id.trim())) {
                                    teamCount++;
                                }

                            }
                        }

                        if (teamCount != fullTeam ) {
                            if(aTeam.team.size() > bTeam.team.size()){
                                System.out.println("Error: " + bTeam.team.get(0).id + " has not provided feedback for all of it's team");
                                displayFileNameAndFilePath(bTeam.team.get(0).id, file) ;
                            }
                            else{
                                System.out.println("Error: " + aTeam.team.get(0).id + " has not provided feedback for all of it's team");
                                displayFileNameAndFilePath(aTeam.team.get(0).id, file) ;


                            }

                            throw new Exception();
                        }
                        teamCount = 0;
                    }
                }
            }
        }
        catch(Exception tokimonMissingFeedbacks){
            System.exit(-1);
        }


        /*
        Error Checking:
        If Tokimon S is already in a team, it is an error if S’s property does not match its
        existing recorded properties. For example, if S was defined to have name “Toki1”,
        it cannot later have name “Toki2” in another JSON file; this is an error.
         */
        //string array for tokimon id and name
        String[] tokiIdName = new String[2];

        try{
            inputFileIs = new File(args[0]);
            List<String> aTempTeam = new ArrayList<String>();
            for (TokimonTeam oneTokimonFeedback : allTokimonFeedbacks) {
                tokiIdName[0]= oneTokimonFeedback.team.get(0).id.trim();
                tokiIdName[1] = oneTokimonFeedback.team.get(0).name.trim();

                for(TokimonTeam singleTokimonFeedback : allTokimonFeedbacks){
                    for(Tokimon aToki : singleTokimonFeedback.team){
                        if(aToki.id.trim().equalsIgnoreCase(tokiIdName[0].trim())){
                            if(!aToki.name.trim().equalsIgnoreCase(tokiIdName[1].trim())){
                                System.out.println("Error: Tokimon: "+aToki.id+" does not match names across records");
                                System.out.println("Tokimon name conflict: " + aToki.name+" and "+tokiIdName[1]+" "+"does not match between these two files:");
                                displayFileNameAndFilePath(singleTokimonFeedback.team.get(0).id.trim(), file) ;
                                System.out.println("and");
                                displayFileNameAndFilePath(aToki.id.trim(), file) ;
                                throw new Exception();
                            }
                        }
                    }


                }
            }

        }
        catch(Exception nameChangeError){

            System.exit(-1);

        }


        //alphabetically sort allTokimonFeedbacks
        sortTokimonTeamById((ArrayList<TokimonTeam>) allTokimonFeedbacks);

        for(TokimonTeam aTokimonF : allTokimonFeedbacks) {
            String tempString = aTokimonF.team.get(0).id;
            aTokimonF.team.get(0).id = "aaaa";
            sortFeedbacksById(aTokimonF.team);
            aTokimonF.team.get(0).id = tempString;
        }

        //print out the Tokimon feedbacks in their relative teams
        for(TokimonTeam oneTokimonFeedback : allTokimonFeedbacks){//

            if(oneTokimonFeedback.whichTeam ==0) {

                tempTeam = new ArrayList<String>();
                tempTeamSize= oneTokimonFeedback.team.size();
                for (int p = 0; p < oneTokimonFeedback.team.size(); p++) {
                    tempTeam.add(p,oneTokimonFeedback.team.get(p).id);

                }

                printWriter.println("Team " + creatingTeamNumber + ",,,,,,");
                for (int q = 0; q < tempTeamSize; q++) {
                    for (TokimonTeam aTokimonFeedback : allTokimonFeedbacks) {

                        if ((tempTeam.get(q).trim()).equalsIgnoreCase(aTokimonFeedback.team.get(0).id.trim())) {
                            aTokimonFeedback.whichTeam = creatingTeamNumber;



                            for (int m = 1; m < aTokimonFeedback.team.size(); m++) {
                                printWriter.println("," + aTokimonFeedback.team.get(0).id + "," + aTokimonFeedback.team.get(m).id + "," + aTokimonFeedback.team.get(m).compatibility.score + ",\"" + aTokimonFeedback.team.get(m).compatibility.comment + "\"");
                            }//want to print the tokimon feedback owner last
                            printWriter.println("," + aTokimonFeedback.team.get(0).id + "," + "-" + "," + aTokimonFeedback.team.get(0).compatibility.score + ",\"" + aTokimonFeedback.team.get(0).compatibility.comment + "\"" +",,"+ aTokimonFeedback.extra_comments);
                        }
                    }
                }
                creatingTeamNumber++;

            }

        }
        printWriter.close();


    }

    /**
     * Searches for the folder of the Tokimon with id idOfTokiWithFolder.
     * Returns true if file is found, else returns false
     * @param idOfTokiWithFolder
     * @param file
     * @param foundJsonFile
     * @return
     */
    private static boolean searchForJsonFile(String idOfTokiWithFolder, File file ,boolean foundJsonFile) {

        Gson gson = new Gson();
        File[] fileList ;
        File thisIsTokimonFile;
        String tokimonInJson;
        Scanner tokimonInput;
        TokimonTeam feedbackOfTeam;
        if(file.isDirectory()) {
            fileList = file.listFiles();
            if(fileList.length ==0 ){
                return foundJsonFile;
            }
            for (File aFile : fileList) {
                foundJsonFile =searchForJsonFile(idOfTokiWithFolder, aFile, foundJsonFile);
            }
            return foundJsonFile;

        }
        else{
            //thisIsTokimonFile =  new File(file);
            try {
                tokimonInput = new Scanner(file);  //teamateInJason should have only one json file after filtering
                tokimonInJson = tokimonInput.nextLine();
                while (tokimonInput.hasNext()) {
                    tokimonInJson += tokimonInput.nextLine();
                }
                feedbackOfTeam = gson.fromJson(tokimonInJson, TokimonTeam.class);
                if(feedbackOfTeam.team.get(0).id.trim().equalsIgnoreCase(idOfTokiWithFolder)){
                    //System.out.println("FOUND THIS ONE: " + feedbackOfTeam.team.get(0).id + idOfTokiWithFolder );
                    foundJsonFile = true;
                    return foundJsonFile;
                }
            }
            catch(FileNotFoundException fnor){
                System.out.println("file not found exception in SearchForJsonFile method");
            }

        }
        return foundJsonFile;
    }

    /**
     * Recursivley searches for .json files using the file parameter.
     * Then adds the path to that file to aList and returns that list.
     * @param aList
     * @param file
     * @return
     */
    public static List<String> recurse(List<String> aList, File file ){

        File[] teamateInJason;
        File[] fileList ;
        if(file.isDirectory()) {
            fileList = file.listFiles();
            for (File aFile : fileList) {
                aList = recurse(aList, aFile);
            }
            return aList;

        }
        else{
            String pathTo = file.getParent();
            File parentOf = new File(pathTo);
            teamateInJason = JsonFileFilter.anonFilter(parentOf.getPath());
            if(teamateInJason.length!=0){

                aList.add(teamateInJason[0].getPath());
            }

            return aList;
        }
    }

    /**
     * Sorts Tokimon Teams feedbacks alphabetically with the Tokimon's id that produced all
     * these feedbacks
     * @param allTokimonFeedbacks
     */
    private static void sortTokimonTeamById(ArrayList<TokimonTeam> allTokimonFeedbacks){
        Comparator<TokimonTeam> compareID = new Comparator<TokimonTeam>() {
            @Override
            public int compare(TokimonTeam o1, TokimonTeam o2) {
                if(o1.team.get(0).id.equalsIgnoreCase(o2.team.get(0).id)){
                    return 0;
                }
                else{
                    return o1.team.get(0).id.compareTo(o2.team.get(0).id);
                }

            }
        };
        java.util.Collections.sort(allTokimonFeedbacks, compareID);
    }

    /**
     * Alphabetically sorts the list of Tokimon by thier id.
     * @param aTokimonFeedback
     */
    private static void sortFeedbacksById(List<Tokimon> aTokimonFeedback){
        Comparator<Tokimon> comp = new Comparator<Tokimon>() {
            @Override
            public int compare(Tokimon object1, Tokimon object2) {

                if(object1.id.trim().equalsIgnoreCase(object2.id.trim())){
                    return 0;
                }
                else{
                    return object1.id.trim().compareTo(object2.id.trim());
                }

            }
        };
        java.util.Collections.sort(aTokimonFeedback, comp);
    }

    /**
     * Displays the file name and path of a Tokimon json using it's id
     * @param idOfCorruptTokimon, , This is the id of the Tokimon file with error
     * @param file , this is the base of the folder so the method can find the json file within this file.
     */
    private static void displayFileNameAndFilePath(String idOfCorruptTokimon, File file) {

        File[] fileList ;
        if(file.isDirectory()) {
            fileList = file.listFiles();
            for (File aFile : fileList) {
                displayFileNameAndFilePath(idOfCorruptTokimon, aFile);
            }
            return;

        }
        else{
            String pathTo = file.getParent();
            File parentOf = new File(pathTo);
            //System.out.println(parentOf.getName());
            if(parentOf.getName().equalsIgnoreCase(idOfCorruptTokimon)){
                System.out.println("FileName: " + file.getName());
                System.out.println("FilePath: " + file.getAbsolutePath());
            }
            return;

        }
    }

}


