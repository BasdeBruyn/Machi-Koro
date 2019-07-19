package Client;

import ClientControllers.SteelMuntenController;
import Server.SpelModel;
import SpelObjecten.Speler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Deze klassen laat het steel munten scherm zien indien het mogelijk is om munten te stelen,
 * dit kan gebeuren als de speler 6 dobbelt en een tv-station heeft
 * @author Bas, Bashar
 */
public class SteelMuntenView extends Pane {
    /**
     * javaFX code om het scherm op te maken en funtionaliteit te geven aan de knoppen.
     * @param model geeft het spel model mee zodat we de status van de spel objecten kunnen zien.
     * @param spelerId geeft het speler id mee van de speler die mag stelen.
     */
    public SteelMuntenView(SpelModel model, int spelerId){
        this.setId("SteelMuntenPane");
        this.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        GridPane gridPane = new GridPane();
        int row = 0;
        for(Speler speler : model.getSpelers()){
            if(!(speler.getSpelerId() == spelerId)){
                Label naamLabel = new Label(speler.getNaam());
                naamLabel.setTextFill(Color.WHITE);
                naamLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
                StackPane muntenPane = new StackPane();
                Circle muntCircle = new Circle(30);
                Label muntLabel = new Label(Integer.toString(speler.getAantalMunten()));
                muntLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
                muntCircle.setFill(Color.GOLD);
                muntCircle.setStroke(Color.GOLDENROD);
                muntCircle.setStrokeWidth(12);
                muntenPane.getChildren().addAll(muntCircle, muntLabel);
                Button spelerKnop = new Button();
                spelerKnop.setId("SteelMuntenButton");
                spelerKnop.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
                spelerKnop.setScaleX(0.7);
                spelerKnop.setScaleY(0.7);
                muntenPane.setTranslateX(50);
                spelerKnop.setOnAction(e ->
                    SteelMuntenController.steelMunten(speler.getSpelerId())
                );
                gridPane.add(naamLabel,0,row);
                gridPane.add(muntenPane,1,row);
                gridPane.add(spelerKnop,2,row);
                gridPane.setTranslateX(100);
                gridPane.setTranslateY(100);
                gridPane.setVgap(100);
                row++;
            }
        }
        this.getChildren().add(gridPane);
    }
}
