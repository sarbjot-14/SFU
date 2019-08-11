package sample.sub_windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.ShapeCreator;
import sample.model.Tokimon;

/**
 * Launches sub window displaying all the attributes of Tokimon
 * that was chosen
 */
public class MoreInfo {
    public static void displayMoreInfo(Tokimon toki){
        Label title = new Label(toki.name+ " \'s Information");
        Label ability = new Label("Ability Type: "+toki.ability);
        Label color = new Label("Color: "+toki.color);
        Label strength = new Label("Strength: "+String.valueOf(toki.strength));
        Label weight = new Label("Weight: " +String.valueOf(toki.weight));
        Label height = new Label("Height: "+String.valueOf(toki.height));
        Label health = new Label("Health: "+String.valueOf(toki.health));

        VBox tokimonInfoVBox = new VBox(title,ability,color, strength,weight,height,health);
        tokimonInfoVBox.setAlignment(Pos.CENTER);
        tokimonInfoVBox.setSpacing(20);


        Pane infoShape = ShapeCreator.create(toki);

        //infoShape.setAlignment(Pos.CENTER_RIGHT);
        HBox moreInfoHBox = new HBox(tokimonInfoVBox,infoShape);
        HBox.setMargin(infoShape, new Insets(80, 0, 0, 70));

        Scene rectScene = new Scene(moreInfoHBox, 300, 250);

        Stage secondStage = new Stage();
        secondStage.setTitle("Arena Display");
        secondStage.setScene(rectScene);
        secondStage.show();
    }
}
