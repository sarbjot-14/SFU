package sample;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Tokimon;
import sample.model.TokimonTeam;
import sample.sub_windows.MoreInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Launches main menu. Controls flow of system
 */
public class Main extends Application {
    static GridPane gridPane = new GridPane();
    List<Tokimon> tokiList = new ArrayList<>();
    List<Integer> occupied = new ArrayList<>();
    TextField nameTextField;
    public static Label feedBack = new Label("Welcome");
    int id=0;
    //ShapeCreator createShape = new ShapeCreator();
    TokimonTeam theTeam;
    RadioButton radioButtonFly ;
    RadioButton radioButtonFire ;
    RadioButton radioButtonWater ;
    RadioButton radioButtonElectric ;
    RadioButton radioButtonFreeze ;
    ComboBox<String> whichColor;
    Slider weightSlider;
    Slider heightSlider;
    TokiCare tokiCareTrainFeed = new TokiCare();
    Scene shapeScene;
    Parent root2;
    Stage secondStage ;
    static boolean shapeSceneSet = false;


    public static void main(String[] args) {

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {



        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Tokimon Menu");

        Button getAllButton  = new Button("Get All Tokimons");
        Button returnSingleToki = new Button("Return Single Tokimon");
        //CREATE FORM TO ADD TOKIMONS
        Label addTokiL1 = new Label("ADD TOKIMONS");
        Label addTokiL2 = new Label("Enter Name");
        nameTextField = new TextField();
        Label addTokiL3 = new Label("Choose ability");
        radioButtonFly = new RadioButton("Fly");
        radioButtonFire = new RadioButton("Fire");
        radioButtonWater = new RadioButton("Water");
        radioButtonElectric = new RadioButton("Electric");
        radioButtonFreeze = new RadioButton("Freeze");
        ToggleGroup radioGroup = new ToggleGroup();
        radioButtonFly.setToggleGroup(radioGroup);
        radioButtonWater.setSelected(true);
        radioButtonWater.setToggleGroup(radioGroup);
        radioButtonElectric.setToggleGroup(radioGroup);
        radioButtonFire.setToggleGroup(radioGroup);
        radioButtonFreeze.setToggleGroup(radioGroup);
        Button addTokimon = new Button("Submit Tokimon");

        whichColor = new ComboBox<>();
        whichColor.getItems().addAll("red","blue","yellow", "orange","green","purple");
        whichColor.getSelectionModel().selectFirst();

        Label weightLabel = new Label("Choose Weight");
        weightSlider = new Slider(15.0, 40.0, 20.0);
        weightSlider.setShowTickLabels(true);
        Label heightLabel = new Label("Choose Height");
        heightSlider = new Slider(25.0, 70.0, 45.0);
        heightSlider.setShowTickLabels(true);


        VBox addTokiVBox = new VBox();
        addTokiVBox.setSpacing(10);
        addTokiVBox.getChildren().addAll(addTokiL1,addTokiL2,nameTextField,addTokiL3, radioButtonWater, radioButtonFly,radioButtonFire, radioButtonElectric,radioButtonFreeze,whichColor, weightLabel,weightSlider, heightLabel,heightSlider,addTokimon);


        Button delTokimon = new Button("Delete A Tokimon");

        addTokimon.setOnAction(new AddTokiButtonClickHandler());
        delTokimon.setOnAction(new DelTokiButtonClickHandler());

        addTokiVBox.setAlignment(Pos.CENTER);

        BorderPane bPane = new BorderPane();


        final double WIDTH = 300.0, HEIGHT = 200.0;

        // Create the menu bar.
        MenuBar menuBar = new MenuBar();

        // Create the File menu.
        Menu createToki = new Menu("Create");
        Menu tokiCare = new Menu("Tokimon Care");

        // Add items to fileMenu
        MenuItem train = new MenuItem("Train");

        tokiCare.getItems().addAll(train);

        MenuItem create = new MenuItem("Create Tokimon");
        createToki.getItems().addAll(create);

        // Register an event handler for the exit/open items.
        create.setOnAction(event -> {
            //System.out.println("creating page");
            bPane.setCenter(addTokiVBox);
        });
        train.setOnAction(event -> {

            VBox trainingVBox = tokiCareTrainFeed.train(gridPane);

            bPane.setCenter(trainingVBox);

        });

        // Add the File menu to the menu bar.
        menuBar.getMenus().addAll(createToki,tokiCare);
        bPane.setTop(menuBar);
        bPane.setCenter(addTokiVBox);
        //button for show all
        Button showAllButton = new Button("Show AllTokimons");


        showAllButton.setOnAction(new ShowAllClickHandler());
        bPane.setRight(showAllButton);

        bPane.setBottom(feedBack);

        Scene mainMenuScene = new Scene(bPane, 500, 600);


        primaryStage.setScene(mainMenuScene);
        primaryStage.show();



    }
    class ShowAllClickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            feedBack.setText("");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");

            try{
                //System.out.println("is it showing? " + secondStage.isShowing());
                if(secondStage.isShowing()){
                    return;
                }
            }
            catch (NullPointerException npeee){

            }

            String jsonTokimonTeam="";
            jsonTokimonTeam = ServerRequests.showAll(jsonTokimonTeam);

            Gson gson = new Gson();
            theTeam = new TokimonTeam();
            try {
                theTeam = gson.fromJson(jsonTokimonTeam, TokimonTeam.class);
                //System.out.println("we made it " + theTeam.team.get(0));
                id= theTeam.team.size();
                if(theTeam.team.size() == 0 ){
                    occupied.add(-1);

                }
                for(int i = 0 ; i < theTeam.team.size() ; i++){
                    occupied.add(i,-1);
                }


                //gridPane.getChildren().removeAll();
                gridPane.setPadding(new Insets(10, 10, 20, 10));
                gridPane.setVgap(30);
                gridPane.setHgap(20);
                gridPane.setAlignment(Pos.CENTER);
                //initialize display

                if(shapeSceneSet){
                    //secondStage.setScene(shapeScene);

                }
                else{
                    secondStage = new Stage();

                    shapeScene = new Scene(gridPane, 600, 500);
                    secondStage.setScene(shapeScene);
                    shapeSceneSet = true;
                    secondStage.setTitle("Arena Display");

                    secondStage.show();
                    for(int i = 0 ; i< theTeam.team.size() ; i++){
                        addTokiToDisplay(theTeam.team.get(i));
                        id++;
                    }
                }

                secondStage.show();


            }

            catch (NullPointerException npe){
                System.out.println("server not running");
                feedBack.setText("Could not connect to Server");
                feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;;");
            }

            catch(JsonSyntaxException jse){
                //System.out.println("Error: Could not convert Json to Tokimon class" );
                System.exit(-1);
            }




        }

    }
    class AddTokiButtonClickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            feedBack.setText("");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");
            try {
                if (!(secondStage.isShowing())) {
                    feedBack.setText("Please Open arena by clicking show all tokimons first");
                    feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;;");
                    return;
                }
            }
            catch (NullPointerException errorr){
                feedBack.setText("Please Open arena by clicking show all tokimons first");
                feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;");
                return;
            }

            String name;
            String ability = null;
            String color;
            int strength;
            int health;
            double height;
            double weight;

            ///////Update Tokimon List////////
            if (!nameTextField.getText().trim().isEmpty() ) {
                //System.out.println(nameTextField.getText());
                name = nameTextField.getText();

            }
            else{
                feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;;");
                feedBack.setText("Sorry Please Enter Name First");
                return;
            }

            //what ability is it
            if(radioButtonWater.isSelected()){
                ability = "water";
            }
            else if(radioButtonElectric.isSelected()){
                ability = "electric";
            }
            else if(radioButtonFire.isSelected()){
                ability = "fire";
            }
            else if(radioButtonFly.isSelected()){
                ability = "fly";
            }
            else if(radioButtonFreeze.isSelected()){
                ability = "freeze";
            }
            else{
                System.out.println("please enter ability");
                feedBack.setText("please include ability");
                return;
            }

            color = whichColor.getValue();

            height = heightSlider.getValue();
            weight = weightSlider.getValue();


            ////Updating Display ///////

            Tokimon toki = new Tokimon(id,name, ability, height, weight,5 ,color, 10);

            id++;
            ///SEND NEW TOKIMON TO SERVER///
            //if (!nameTextField.getText().isEmpty())
            ServerRequests.addToki(toki);

            addTokiToDisplay(toki);


        }

    }
    class DelTokiButtonClickHandler implements EventHandler<ActionEvent> {
        int removing ;
        int col =0;
        int row =0;
        int indexRem;
        @Override
        public void handle(ActionEvent event){
            feedBack.setText("");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");
            Button source = (Button) event.getSource();
            source.setStyle("-fx-background-color: MediumSeaGreen");

            Node theVBox = (Node) source.getParent();

            Integer colIndex = GridPane.getColumnIndex(theVBox);
            Integer rowIndex = GridPane.getRowIndex(theVBox) ;

            //find what row and col player clicked
            row = rowIndex.intValue();
            col = colIndex.intValue();
            System.out.println("col and row "  + col + " " + row);
            System.out.println("delete");

            indexRem = 7*(row) +(col);
            //delete in server
            theTeam = ServerRequests.deleteToki(theTeam,indexRem);

            //delete in display
            ObservableList<Node> allRecs = gridPane.getChildren();
            VBox fillerVbox = new VBox();

            gridPane.getChildren().remove(theVBox);

            //System.out.println("removed " + theTeam.team.get(indexRem).id );
            theTeam.team.get(indexRem).id = -1;
            occupied.set(indexRem, -1);

        }
    }
    public void addTokiToDisplay(Tokimon toki) {
        feedBack.setText("");
        feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");
        try {
            if (!(secondStage.isShowing())) {
                feedBack.setText("Please Open arena by clicking show all tokimons first");
                feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;;");
                return;
            }
        }
        catch (NullPointerException errorr){
            feedBack.setText("Please Open arena by clicking show all tokimons first");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;");
            return;
        }
        //System.out.println("adding "+toki.id+ " with ability " + toki.ability);
        VBox tokiDeleteMoreVbox = new VBox();
        gridPane.getChildren().removeAll();

        int col = 0;
        int row = 0;


        ////////////CREATING SHAPES/////////////



        Text text;
        if(tokiDeleteMoreVbox.getChildren().size()==0){
            text = null;
        }
        Pane theImage = ShapeCreator.create(toki);
        text = new Text(toki.name );
        text.setStyle("-fx-font: 12 arial;");
        ///////////CREATING SHAPES END///////

        tokiDeleteMoreVbox = new VBox();
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(new DelTokiButtonClickHandler());
        Button moreInfoButton = new Button("More Information");
        moreInfoButton.setOnAction(new MoreInfoButtonClickHandler());

        //tokiDeleteMoreVbox.getChildren().removeAll();
        tokiDeleteMoreVbox.getChildren().addAll(theImage, text,deleteButton,moreInfoButton);


        for (int i = 0; i < theTeam.team.size(); i++) {
            //occupied.get(i);
            col = i % 7;
            if (i % 7 == 0 && i!=0) {
                row++;
            }
            if (occupied.get(i) == -1 ) {

                occupied.set(i, toki.id);
                if(theTeam.team.size()==0){
                    theTeam.team.add(0,toki);
                }
                else{
                    theTeam.team.set(i,toki);
                }

                gridPane.add(tokiDeleteMoreVbox, col, row);
                return;

            }
            else if(i == theTeam.team.size()-1){
                occupied.add(i+1, toki.id);
                theTeam.team.add(i+1,toki);

                i++;
                col = i % 7;
                if (i % 7 == 0 && i!=0) {
                    row++;
                }
                gridPane.add(tokiDeleteMoreVbox, col, row);
                //System.out.println("after");
                //System.out.println(occupied);
                for(int j = 0 ; j< theTeam.team.size(); j++){
                    //System.out.print(" " +theTeam.team.get(j).id);
                }
                //System.out.println("");
                return;

            }
        }

        int i = theTeam.team.size();
        col = i % 7;
        if (i % 7 == 0 && i != 0) {
            row++;
        }

        if(theTeam.team.size() ==0){
            occupied.add(i, toki.id);
            theTeam.team.add(i,toki);
            //System.out.println("createing col and row " + col+ " " + row);
            gridPane.add(tokiDeleteMoreVbox, col, row);

        }

    }
    class MoreInfoButtonClickHandler implements EventHandler<ActionEvent> {

        int col =0;
        int row =0;
        int indexInfo;
        String jsonTokimon;
        @Override
        public void handle(ActionEvent event){
            feedBack.setText("");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");
            Button source = (Button) event.getSource();
            Node theVBox = (Node) source.getParent();

            Integer colIndex = GridPane.getColumnIndex(theVBox);
            Integer rowIndex = GridPane.getRowIndex(theVBox) ;

            //find what row and col player clicked
            row = rowIndex.intValue();
            col = colIndex.intValue();

            indexInfo  = 7*(row) +(col);

            Tokimon theTokimon = ServerRequests.showInfo(theTeam, indexInfo);

            MoreInfo.displayMoreInfo(theTokimon);


        }
    }

    public static void update(int idTrainee,int index, int strength){
        feedBack.setText("");
        feedBack.setStyle("-fx-border-color:black; -fx-background-color: white;");
        if(!shapeSceneSet){
            feedBack.setText("Please Open arena by clicking show all tokimons first");
            feedBack.setStyle("-fx-border-color:black; -fx-background-color: #ff6666;;");
        }
        VBox shapeVbox = (VBox)gridPane.getChildren().get(index);
        ServerRequests.trainToki(idTrainee);

        Pane shapePane = (Pane)shapeVbox.getChildren().get(0);
        for(Node aShapeNode : shapePane.getChildren()) {
            Shape theShape = (Shape) aShapeNode;
            theShape.setStyle("-fx-stroke-width: " + (strength +2) + ";");

            System.out.println("shape changing");

        }
    }


}
