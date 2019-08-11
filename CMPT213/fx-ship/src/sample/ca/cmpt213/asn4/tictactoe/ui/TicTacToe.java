package sample.ca.cmpt213.asn4.tictactoe.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import sample.ca.cmpt213.asn4.tictactoe.game.GridState;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for creating the user interface objects and
 * launching the display
 */
public class TicTacToe extends Application {
   
    Label winnerIs = new Label("");
    GridState currentState = new GridState();
    GridPane ticTacGrid;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage)
    {
        //create a tic tac toe grid with GridPane
        ticTacGrid = new GridPane();
        ticTacGrid.setHgap(20);
        ticTacGrid.setVgap(20);
        ticTacGrid.setPadding(new Insets(10, 30, 10, 30));
        ticTacGrid.setAlignment(Pos.CENTER);

        Image imgB = new Image("file:img/blank.png");
        // ImageView Control
        ImageView imgViewB = new ImageView(imgB);
        for(int i = 0 ; i < 4 ; i++) {
            for (int j = 0; j < 4; j++) {
                imgB = new Image("file:img/blank.png");
                // ImageView Control
                imgViewB = new ImageView(imgB);
                imgViewB.setFitHeight(50);
                imgViewB.setFitWidth(50);

                //add event handler to each image
                //set each image as blank image
                imgViewB.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        ImageView source = (ImageView) e.getSource();
                        Integer colIndex = GridPane.getColumnIndex(source);
                        Integer rowIndex = GridPane.getRowIndex(source);

                        //find what row and col player clicked
                        int row = rowIndex.intValue();
                        int col = colIndex.intValue();
                        String playerTurn = currentState.playerTurn;
                        if(currentState.isValidMove(row, col) && !currentState.isGameOver){
                            if (playerTurn == "x") {
                                ((ImageView) e.getSource()).setImage(new Image("file:img/x.png"));
                            } else {
                                ((ImageView) e.getSource()).setImage(new Image("file:img/o.jpeg"));

                            }
                            currentState.setMove(row, col);
                            if(currentState.isTie){
                                winnerIs.setText( "Tie Game");
                            }

                            else if(currentState.checkWin()){
                                winnerIs.setText( playerTurn+ " has won!");
                            }


                        }


                    }

                });
                ticTacGrid.add(imgViewB,i, j);
            }
        }

        winnerIs.setFont(new Font("Arial", 30));
        //button for resetting
        Button myButton = new Button("New Game");

        myButton.setOnAction(new ButtonClickHandler());

        VBox vbox = new VBox(3);
        ((VBox) vbox).getChildren().addAll(ticTacGrid, winnerIs, myButton);
        vbox.setAlignment(Pos.CENTER);

        Scene ticTacScene = new Scene(vbox, 400, 600);


        primaryStage.setScene(ticTacScene);

        // Show the window.
        primaryStage.show();
    }
    class ButtonClickHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            currentState.clearGrid();
            resetGridToBlank();

        }
    }

    public void resetGridToBlank(){
        List<Node> allImages = new ArrayList<>();
        allImages = ticTacGrid.getChildren();
        for(Node anImage : allImages){
            ((ImageView)anImage).setImage(new Image("file:img/blank.png"));
        }
        winnerIs.setText("Start");



    }

}
