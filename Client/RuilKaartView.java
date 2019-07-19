package Client;

import ClientControllers.RuilKaartController;
import ParentClasses.Kaart;
import Server.SpelModel;
import SpelObjecten.Speler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
/**
 * Deze klassen laat het ruil kaartScherm zien indien hetmogelijk is om kaarten te ruilen tussen twee spelers,
 * dit kan gebeuren als de speler 6 dobbelt en een bedrijvencomplex heeft
 *
 * @author Bas, Omid
 */
public class RuilKaartView extends Pane {

    private int kaartId1;
    private int diefId;
    private int kaartId2;
    private int slachtofferId;
    
    private ImageView clickedSelf = null;
    private ImageView clickedOther = null;
    /**
     * javaFX code om het scherm op te maken en funtionaliteit te geven aan de knoppen.
     * @param model geeft het spel model mee zodat we de status van de spel objecten kunnen zien.
     * @param spelerId geeft het speler id mee van de speler die mag ruilen.
     */
    public RuilKaartView(SpelModel model, int spelerId){
        this.setStyle("-fx-background-image: url('/resources/Ruilen_Background.png');-fx-background-repeat: stretch;");
        double breedteImage = 706/4.1;
        double hoogteImage = 1020/4.1;
        double breedtePane = 1640;
        double standoffY = 54;
        Button reset = new Button();
        reset.setScaleX(0.5);
        reset.setScaleY(0.5);
        reset.setId("ResetButton");
        reset.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
        Button ruil = new Button();
        ruil.setScaleX(0.7);
        ruil.setScaleY(0.7);
        ruil.setId("RuilButton");
        ruil.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
        VBox box = new VBox(ruil,reset);
        box.setTranslateX(1760);
        box.setTranslateY(50);
        this.getChildren().addAll(box);
        ArrayList<ArrayList<ImageView>> arrHanden = new ArrayList<>();

        reset.setOnAction(e -> {
            clickedSelf = null;
            clickedOther = null;
            diefId = 0;
            kaartId1 = 0;
            slachtofferId = 0;
            kaartId2 = 0;
            for(ArrayList<ImageView> hand : arrHanden){
                for(ImageView kaart : hand){
                    kaart.setScaleX(1);
                    kaart.setScaleY(1);
                }
            }
        });

        ruil.setOnAction(e -> {
            if(clickedSelf != null && clickedOther != null){
                RuilKaartController.ruilKaart(diefId,kaartId1,slachtofferId,kaartId2);
            }
        });

        //spelerhanden fx wordt aangemaakt
        for(Speler speler : model.getSpelers()) {
            ArrayList<ImageView> handKaarten = new ArrayList<>();
            ArrayList<Label> labels = new ArrayList<>();
            arrHanden.add(handKaarten);
            Pane handPane = new Pane();

            //Kaart images worde gekoppeld aan de kaarten van de speler
            for (Kaart kaart : speler.getSpelerHand()) {
                handKaarten.add(kaart.kaartImage());
                handKaarten.get(handKaarten.size() -1).setId(String.valueOf(kaart.getKaartId()));
                labels.add(new Label(kaart.getAantalKaarten() + "X"));
            }

            double standoffX = -60;
            double standardSize = 1;
            double sizeOnHover = 1.1;

            //voeg de kaarten toe aan een pane
            for (int i = 0; i < handKaarten.size(); i++) {
                handPane.getChildren().addAll(handKaarten.get(i),labels.get(i));
            }

            //initializeer de kaarten
            int index = 0;
            for (ImageView kaart : handKaarten) {
                labels.get(index).setTranslateX(standoffX + (breedteImage -50));
                labels.get(index).toFront();
                labels.get(index).setStyle("-fx-font-size: 30; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
                kaart.setX(standoffX);
                standoffX += (breedtePane - breedteImage) / (handKaarten.size() - 1);
                kaart.toBack();
                kaart.setFitHeight(hoogteImage);
                kaart.setFitWidth(breedteImage);
                //hover over de kaart
                kaart.setOnMouseEntered(e -> {
                    handPane.toFront();
                    kaart.setScaleX(sizeOnHover);
                    kaart.setScaleY(sizeOnHover);
                });
                //klik op de kaart
                kaart.setOnMouseClicked(e -> {
                    int iD = Integer.parseInt(kaart.getId());
                    if(speler.getSpelerId() == spelerId && clickedSelf == null && iD != 8 && iD != 7 && iD != 6){
                        clickedSelf = kaart;
                        diefId = speler.getSpelerId();
                        kaartId1 = Integer.parseInt(kaart.getId());
                        kaart.setScaleX(sizeOnHover);
                        kaart.setScaleY(sizeOnHover);
                    } else if (speler.getSpelerId() != spelerId && clickedOther == null && iD != 8 && iD != 7 && iD != 6){
                        clickedOther = kaart;
                        slachtofferId = speler.getSpelerId();
                        kaartId2 = Integer.parseInt(kaart.getId());
                        kaart.setScaleX(sizeOnHover);
                        kaart.setScaleY(sizeOnHover);
                    }
                });
                //verlaat de kaart
                kaart.setOnMouseExited(e -> {
                    if(!((speler.getSpelerId() == spelerId && clickedSelf == kaart) || (speler.getSpelerId() != spelerId && clickedOther == kaart))){
                        kaart.setTranslateY(0);
                        handPane.toBack();
                        kaart.setScaleX(standardSize);
                        kaart.setScaleY(standardSize);
                    }
                });
                index++;
            }

            //zorg dat de speler die moet ruilen onderaan staat
            if(speler.getSpelerId() == spelerId){
                handPane.setTranslateY(890);
            } else {
                handPane.setTranslateY(standoffY);
                standoffY += hoogteImage + 15;
            }

            Label naam = new Label(speler.getNaam());
            naam.setTranslateX(-190);
            naam.setStyle("-fx-font-size: 35; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
            handPane.getChildren().add(naam);

            //voeg de hand toe aan het scherm
            this.getChildren().add(handPane);
            handPane.setMaxSize(breedtePane, hoogteImage);
            handPane.setTranslateX(200);
        }
    }
}
