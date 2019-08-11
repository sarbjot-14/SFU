package sample;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sample.model.TokimonTeam;

import static sample.Main.update;

/**
 * Takes tokimon to train from combobox. Increases outline of toki signifying stronger
 * Tokimon. Updates display and Server.
 */
public class TokiCare {
    ListView<String> picTrainee;
    TokimonTeam theTeam;// = new TokimonTeam();
    String jsonTokimonTeam="";
    public VBox train(GridPane gridPane){

        VBox trainVbBox = new VBox();
        jsonTokimonTeam = "";
        jsonTokimonTeam = ServerRequests.showAll(jsonTokimonTeam);

        Gson gson = new Gson();

        try {
            theTeam = gson.fromJson(jsonTokimonTeam, TokimonTeam.class);
        }
        catch(Exception error){
            System.out.println("server not running");
        }

        picTrainee = new ListView<>();
        for(int i = 0; i< theTeam.team.size(); i++){
            picTrainee.getItems().add(theTeam.team.get(i).name);
        }

        Label trainingLabel = new Label("Choose Tokimon to Train");
        VBox trainingVBox = new VBox();
        Button thisTrainee = new Button("Train");
        thisTrainee.setOnAction(new TraineeClickHandler());
        trainingVBox.getChildren().addAll(trainingLabel, picTrainee,thisTrainee);
        return  trainingVBox;
    }



    class TraineeClickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){


            jsonTokimonTeam = "";
            jsonTokimonTeam = ServerRequests.showAll(jsonTokimonTeam);

            Gson gson = new Gson();

            try {
                theTeam = gson.fromJson(jsonTokimonTeam, TokimonTeam.class);
            }
            catch(Exception error){
                System.out.println("server not running here");
            }
           String selected =  picTrainee.getSelectionModel().getSelectedItem();
           int idTrainee = 0;

           for(int i = 0; i< theTeam.team.size() ; i++){
               if(theTeam.team.get(i).name.equalsIgnoreCase(selected)){
                   idTrainee = theTeam.team.get(i).id;
                   update(idTrainee,i, theTeam.team.get(i).strength);
                   break;
               }
           }




        }

    }
}
