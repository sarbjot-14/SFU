package tokimonArena.controller;


import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import java.io.*;
import java.io.File;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tokimonArena.model.Tokimon;
import org.springframework.web.bind.annotation.RestController;
import tokimonArena.model.TokimonTeam;

import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

/**
 * Acts as Server that listens for requests from client regarding
 * the changes of tokimons.
 * Updates Json file found in data folder to reflect changes made.
 */
@RestController
public class TokimonController {
    private final AtomicLong counter = new AtomicLong(0);
    String tokimonInJson;
    TokimonTeam allTokimons = null;
    Gson gson = new Gson();

    @GetMapping("/api/tokimon/all")
    public TokimonTeam getTokimon(){
        loadingJsonToki();

        return allTokimons;
    }

    @GetMapping("/api/tokimon/{tid}")
    public Tokimon getTokimon(@PathVariable int tid){
        loadingJsonToki();
        Tokimon returnTokimon = null;
        for(int i=0 ; i< allTokimons.team.size() ; i++){
            if(allTokimons.team.get(i).id ==  tid){
                returnTokimon =allTokimons.team.get(i);

            }
        }
        return returnTokimon;
    }


    @PostMapping("/api/tokimon/add")
    public ResponseEntity addTokimon(@RequestBody Tokimon newTokimon){
        // ... create a new Tokimon

        allTokimons.team.add(newTokimon);
        updatingJson();
        return new ResponseEntity(HttpStatus.CREATED);


    }


    @DeleteMapping("/api/tokimon/{tid}")
    public ResponseEntity deleteTokimon(@PathVariable long tid){
        // ... delete Tokimon(tid)
        loadingJsonToki();
        for(int i=0; i< allTokimons.team.size(); i++){
            if(allTokimons.team.get(i).id== tid ){
                allTokimons.team.remove(i);
                break;
            }
            if(allTokimons.team.size()-1 == i){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }
        updatingJson();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/api/train/tokimon/{tid}")
    public void trainTokimon(@PathVariable long tid){
        // ... training Tokimon(tid)

        loadingJsonToki();
        for(int i=0; i< allTokimons.team.size(); i++){
            if(allTokimons.team.get(i).id== tid ){
                allTokimons.team.get(i).strength = allTokimons.team.get(i).strength +2;
                break;
            }
        }
        updatingJson();
    }

    @PostConstruct
    public void init(){
        System.out.println("POST CONSTRUCT CODE");
        loadingJsonToki();

    }

    /**
     * Reads current tokimons from Json folder
     */
    public void loadingJsonToki(){
       // System.out.println("loading");
        File file  = new File("data/tokimon.json");
        try {
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        }
        catch(FileNotFoundException fnfe){
            System.out.println("Error: Input folder does not exist");
            return;
        }

        Scanner tokimonInput = null;
        try {
            tokimonInput = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Scanner did not initialize");
        }
        tokimonInJson = tokimonInput.nextLine();
        while (tokimonInput.hasNext()) {
            tokimonInJson += tokimonInput.nextLine();
        }
        //System.out.println(tokimonInJson);

        //find errors in json files
        allTokimons = null;
        try {
            allTokimons = gson.fromJson(tokimonInJson, TokimonTeam.class);
            //System.out.println(allTokimons.team.get(0).health);
        }
        catch(JsonSyntaxException jse){
            System.out.println("Error: Could not convert Json to Tokimon class" );
            System.exit(-1);
        }
    }

    /**
     * change json folder so that it reflects current state
     * of tokimons
     */
    public void updatingJson(){
        //System.out.println("updating json");
        FileWriter fileWriter = null;
        //create output csv file
        try {
            fileWriter = new FileWriter("data/tokimon.json");
        } catch (IOException e) {
            System.out.println("Error: Output folder does not exist");
            return;
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);

        //print headers into output csv file
        printWriter.println("{\n\t\"team\":[");
        for(int i = 0; i< allTokimons.team.size(); i++){

            if(i!=0){
                printWriter.print("\t,");
            }
            else{
                printWriter.print("\t");
            }
            printWriter.println("{\n\t\t\"name\":\""+allTokimons.team.get(i).name+"\",\n\t\t\"id\":\""+allTokimons.team.get(i).id+"\",\n\t\t\"ability\":\""+allTokimons.team.get(i).ability+"\",\n\t\t\"color\":\"" +allTokimons.team.get(i).color+"\",\n\t\t\"strength\":\""+allTokimons.team.get(i).strength+"\",\n\t\t\"weight\":\""+allTokimons.team.get(i).weight+"\",\n\t\t\"height\":\""+allTokimons.team.get(i).height+"\",\"health\":\""+allTokimons.team.get(i).health+"\"\t\t\n\t\t}");
        }

        printWriter.println("\t]\n}");
        printWriter.close();
    }
}