package sample;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import sample.model.Tokimon;

/**
 * Responsible for taking toki attributes and creating image
 * of tokimon that protrays that tokimons attributes. Functions return
 * Panes so they can be added to display
 */
public class ShapeCreator {

    ShapeCreator(){
        //System.out.println("alrighty");
    }

    public static Pane create(Tokimon toki){
        //System.out.println(toki.ability);


        Pane theImage = null;
        System.out.println(toki.ability);
        if(toki.ability.equalsIgnoreCase("water")){
            System.out.println("entering water");
            theImage = createWaterType(toki);
        }
        else if(toki.ability.equalsIgnoreCase("fly")){

            theImage =createFlyType(toki);
        }
        else if(toki.ability.equalsIgnoreCase("fire")){
            System.out.println("entering fire");
            theImage =createFireType(toki);
        }
        else if(toki.ability.equalsIgnoreCase("electric")){
            theImage =createElectricType(toki);
        }
        else if(toki.ability.equalsIgnoreCase("freeze")){
            theImage = createFreezeType(toki);
        }
        for(Node aShapeNode : theImage.getChildren()){
            Shape theShape = (Shape)aShapeNode;
            theShape.setStyle("-fx-stroke-width: "+toki.strength+";");
            switch (toki.color) {
                case "blue":
                    theShape.setStroke(Color.BLUE);
                    break;
                case "green":
                    theShape.setStroke(Color.GREEN);
                    break;
                case "purple":
                    theShape.setStroke(Color.PURPLE);
                    break;
                case "yellow":
                    theShape.setStroke(Color.YELLOW);
                    break;
                case "red":
                    theShape.setStroke(Color.RED);
                    break;
                case "orange":
                    theShape.setStroke(Color.ORANGE);
                    break;
                default:
                    System.out.println("could not find color");
            }
        }

        return theImage;

    }

    private static Pane createWaterType(Tokimon toki){
        Ellipse thisShape = new Ellipse();
        thisShape.setRadiusY(toki.height);
        thisShape.setRadiusX((toki.weight));
        thisShape.setFill(Color.DEEPSKYBLUE);

        Arc arc = new Arc();
        arc.setCenterX(toki.weight + 10);
        arc.setCenterY(toki.height -35);
        arc.setRadiusX(toki.weight-5);
        arc.setRadiusY(toki.weight-5);
        arc.setStartAngle(45.0f);
        arc.setLength(270.0f);
        arc.setFill(Color.DEEPSKYBLUE);
        arc.setType(ArcType.ROUND);

        Pane theImage = new Pane(thisShape, arc);
        return theImage;

    }
    private static Pane createFireType(Tokimon toki){


        Arc arc = new Arc();
        arc.setCenterX(toki.weight + 5);
        arc.setCenterY(20.0f);
        arc.setRadiusX(toki.weight);
        arc.setRadiusY(toki.height);
        arc.setStartAngle(140.0f);
        arc.setLength(270.0f);
        arc.setFill(Color.ORANGERED);
        arc.setType(ArcType.ROUND);

        Pane theImage = new Pane( arc);
        return theImage;

    }
    private static Pane createFlyType(Tokimon toki){
        Ellipse thisShape = new Ellipse();
        thisShape.setCenterY(toki.height/2);
        thisShape.setCenterX(toki.weight );
        thisShape.setRadiusY(toki.height);
        thisShape.setRadiusX((toki.weight));
        thisShape.setFill(Color.LIGHTPINK);
        //thisShape.setStroke(Color.DEEPPINK);

        Arc leftWing = new Arc();
        leftWing.setCenterX(toki.weight - toki.weight -10);
        leftWing.setCenterY(toki.height/2);
        leftWing.setRadiusX(toki.weight/2);
        leftWing.setRadiusY(toki.weight/2);
        leftWing.setStartAngle(0.0f);
        leftWing.setLength(180.0f);
        leftWing.setFill(Color.LIGHTPINK);
        leftWing.setType(ArcType.ROUND);

        Arc rightWing = new Arc();
        rightWing.setCenterX((2*toki.weight) + 10 );
        rightWing.setCenterY(toki.height/2);
        rightWing.setRadiusX(toki.weight/2);
        rightWing.setRadiusY(toki.weight/2);
        rightWing.setStartAngle(0.0f);
        rightWing.setLength(180.0f);
        rightWing.setFill(Color.LIGHTPINK);
        rightWing.setType(ArcType.ROUND);

        Arc beak = new Arc();
        beak.setCenterX(toki.weight-5);
        beak.setCenterY(toki.height/2-4);
        beak.setRadiusX(5.0f);
        beak.setRadiusY(5.0f);
        beak.setStartAngle(45.0f);
        beak.setLength(45.0f);
        beak.setFill(Color.LIGHTPINK);
        beak.setType(ArcType.ROUND);


        Pane theImage = new Pane(thisShape, leftWing, rightWing,beak);
        return theImage;

    }
    private static Pane createElectricType(Tokimon toki){
        // create lines
        toki.weight = toki.weight-20;
        toki.height = toki.height -30;
        Polygon bolt1 = new Polygon(
                new double[]{
                        20.0, (60.0 +toki.height) , //bottom right
                        ( 50.0 + toki.weight), 0.0, //top right
                        0.0, (60.0 +toki.height) //bottom left


                }
        );
        bolt1.setFill(Color.YELLOW);

        Polygon bolt2 = new Polygon(
                new double[]{
                        (55.0 +toki.weight), 40.0, //top right
                        (35.0+toki.weight), 40 ,  //top left
                        0.0, 110.0 +toki.height //bottom  left


                }
        );
        bolt2.setFill(Color.YELLOW);




        Pane theImage = new Pane(bolt1, bolt2);
        return theImage;

    }
    private static Pane createFreezeType(Tokimon toki){
        Rectangle rect = new Rectangle(10,10,10,10);
        rect.setHeight(toki.height);
        rect.setWidth(toki.weight);

        rect.setFill(Color.LIGHTSLATEGREY);





        Pane theImage = new Pane(rect);
        return theImage;

    }
}
