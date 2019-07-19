package Client;

import ClientControllers.*;
import ParentClasses.Kaart;
import Server.SpelModel;
import SpelObjecten.Speler;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
/**
 * in deze klasse zitten de verschillende JavaFX scenes van het spel.
 *
 * @author Maarten, Peter, Dennis, Bas, Omid, Bashar, Bruno
 */
public class SpelView {

    private SpelModel spelModel;
    private int spelerId;
    private StackPane stackPane  = new StackPane();
    private ImageView handleidingImageView = handleidingImage();
    private VBox settings;
    private ArrayList<VBox> bezienswaardigheidKaarten = new ArrayList<> ();
    private boolean kaartKopen = false;
    private boolean beziensKopen = false;
    private boolean muteSound = false;

    /**
     * @param model geeft het spelmodel mee.
     * @param spelerId  geeft de spelerID mee van de speler die op deze client speelt.
     */
    public SpelView(SpelModel model,int spelerId) {
        spelModel = model;
        this.spelerId = spelerId;
    }
    /**
     * De hoofdscene, dit is het speelveld, met alle kaarten en knoppen.
     * @return het returns de pane zodat de scene geupdate kan worden.
     */
    public StackPane mainScene() {
        settings = settingsMenu();
        stackPane.setId("BordBackground");
        stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        for(Speler speler : spelModel.getSpelers()){
            stackPane.getChildren().add(spelerHand(speler.getSpelerId()));
            stackPane.getChildren().add(spelerBox(speler.getSpelerId()));
            bezienswaardigheidKaarten.add(bezienswaardighedenVBox(speler.getSpelerId()));
        }

        //winkel
        Pane winkelPane = winkelPane();
        stackPane.getChildren().add(winkelPane);

        //kopen kaart, einde beurt knop, dobbelstenen en settings knoppen
        stackPane.getChildren().add(settingsKnop());
        if(spelModel.getBeurt() == spelerId){
            if(!spelModel.getGegooid()){
                stackPane.getChildren().add(dobbelstenenKnop());
            } else if (!spelModel.getBeurtAfgerond()){
                stackPane.getChildren().add(koopKaartEindeBeurtKnop());
            }
        }
        if(spelModel.isDubbelGegooid()){
            Label dubbelGegooid = new Label("dubbel gegooid");
            dubbelGegooid.setStyle("-fx-font-size: 40; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
            dubbelGegooid.setTranslateX(285 - 200);
            dubbelGegooid.setTranslateY(285);
            stackPane.getChildren().add(dubbelGegooid);
        }
        Pane aantalOgenAchtergrond = new Pane();
        aantalOgenAchtergrond.setId("AantalOgenAchtergrond");
        aantalOgenAchtergrond.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
        aantalOgenAchtergrond.toBack();
        aantalOgenAchtergrond.setTranslateX(185);
        aantalOgenAchtergrond.setTranslateY(135);
        aantalOgenAchtergrond.setMaxSize(395,149);
        Label aantalOgen = new Label(String.valueOf(spelModel.getAantalOgen()));
        aantalOgen.toFront();
        aantalOgen.setTranslateY(285);
        aantalOgen.setTranslateX(285);
        aantalOgen.setStyle("-fx-font-size: 80; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
        stackPane.getChildren().addAll(aantalOgenAchtergrond,aantalOgen);

        return stackPane;
    }
    /**
     * Deze pane is gemaakt voor de hand van de speler, alle kaarten die de speler op dat moment in
     * zijn bezit heeft zullen in deze pane getoond worden.
     * Ook zit hier in de berekening die de hand dynamic houdt, hij bereken hoe groot de pane is
     * en hoeveel kaarten hij heeft en berekent hoeveel ruimte elke kaart krijgt.
     * @param spelerId de spelerID van wie de hand is.
     * @return de pane zodat hij toegevoegd can worden.
     */
    private Pane spelerHand(int spelerId) {
        double breedteImage = 706/4;
        double hoogteImage = 1020/4;
        double breedtePane = 0;
        double clickTranslation = 0;
        ArrayList<ImageView> handKaarten = new ArrayList<>();
        ArrayList<Label> aantalKaartLabels = new ArrayList<>();
        Pane handPane = new Pane();

        int spelerPos;
        if(spelerId - this.spelerId <0){
            spelerPos = spelerId - this.spelerId + spelModel.getSpelers().size();
        } else{
            spelerPos = spelerId - this.spelerId;
        }
        switch(spelerPos){
            case 0 : handPane.setTranslateY(495);
                     breedtePane = 888;
                     clickTranslation = 186;
                     break;
            case 1 : handPane.setRotate(180);
                     handPane.setTranslateY(-491);
                     breedtePane = 888;
                     clickTranslation = 186;
                     break;
            case 2 : handPane.setRotate(90);
                     handPane.setTranslateX(-940);
                     breedtePane = 828;
                     clickTranslation = 168;
                     break;
            case 3 : handPane.setRotate(-90);
                     handPane.setTranslateX(940);
                     breedtePane = 828;
                     clickTranslation = 168;
                     break;
        }
        
        //Kaart images worde gekoppeld aan de kaarten van de speler
        for(Kaart kaart : spelModel.getSpelers().get(spelerId).getSpelerHand()){
            handKaarten.add(kaart.kaartImage());
            aantalKaartLabels.add(new Label(String.valueOf(kaart.getAantalKaarten()) + "X"));
        }
        
        double standoff = 0;
        double standardSize = 1;
        double sizeOnHover = 1.1;
        double sizeOnClick = 1.5;

        for (int i = 0; i < handKaarten.size(); i++){
            handPane.getChildren().addAll(handKaarten.get(i),aantalKaartLabels.get(i));
        }
        int index = 0;
        for(ImageView kaart : handKaarten){
            int i = index;
            kaart.setX(standoff);
            aantalKaartLabels.get(index).setTranslateX(standoff + (breedteImage - 50));
            aantalKaartLabels.get(index).toFront();
            aantalKaartLabels.get(index).setStyle("-fx-font-size: 30; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
            double xStandoff = aantalKaartLabels.get(index).getTranslateX();
            standoff += (breedtePane - breedteImage) / (handKaarten.size() - 1);
            kaart.toBack();
            kaart.setFitHeight(hoogteImage);
            kaart.setFitWidth(breedteImage);
            //hover over de kaart
            kaart.setOnMouseEntered(e -> {
                playPopLow();
                handPane.toFront();
                kaart.toFront();
                kaart.setScaleX(sizeOnHover);
                kaart.setScaleY(sizeOnHover);
                aantalKaartLabels.get(i).toFront();
                aantalKaartLabels.get(i).setScaleX(sizeOnHover);
                aantalKaartLabels.get(i).setScaleY(sizeOnHover);
                aantalKaartLabels.get(i).setTranslateX( xStandoff + 7);
                aantalKaartLabels.get(i).setTranslateY(- 10);
            });
            //klik op de kaart
            double clickTranslate = clickTranslation;
            kaart.setOnMouseClicked(e -> {
                kaart.setScaleX(sizeOnClick);
                kaart.setScaleY(sizeOnClick);
                kaart.setTranslateY(-clickTranslate);
                aantalKaartLabels.get(i).setScaleX(sizeOnClick);
                aantalKaartLabels.get(i).setScaleY(sizeOnClick);
                aantalKaartLabels.get(i).setTranslateX( xStandoff + 32);
                aantalKaartLabels.get(i).setTranslateY(- 55 -clickTranslate);
            });
            //verlaat de kaart
            kaart.setOnMouseExited(e -> {
                kaart.setTranslateY(0);
                handPane.toBack();
                //zet de kaarten weer op volgorde
                for(ImageView imageView : handKaarten){
                    imageView.toBack();
                }
                kaart.setScaleX(standardSize);
                kaart.setScaleY(standardSize);
                aantalKaartLabels.get(i).setScaleX(standardSize);
                aantalKaartLabels.get(i).setScaleY(standardSize);
                aantalKaartLabels.get(i).setTranslateX( xStandoff);
                aantalKaartLabels.get(i).setTranslateY(0);
            });
            index++;
        }
        
        handPane.setMaxSize(breedtePane,hoogteImage);

        return handPane;

    }
    /**
     * De winkel pane is de pane die alle kaarten/knoppen bevat die in het midden van het spelveld staan.
     * @return de pane zodat deze aan het spel boord toegevoegd kan worden.
     */
    private Pane winkelPane(){
        double breedtePane = 1500;
        double breedteImage = 706/3.7;
        double hoogteImage = 1020/3.7;

        Pane winkelPane = new Pane();
        
        Kaart [] kaarten = {
            spelModel.getWinkel().getInfoKaarten()[0],
            spelModel.getWinkel().getInfoKaarten()[1],
            spelModel.getWinkel().getInfoKaarten()[5],
            spelModel.getWinkel().getInfoKaarten()[12],
            spelModel.getWinkel().getInfoKaarten()[13],
            spelModel.getWinkel().getInfoKaarten()[2],
            spelModel.getWinkel().getInfoKaarten()[4],
            spelModel.getWinkel().getInfoKaarten()[10],
            spelModel.getWinkel().getInfoKaarten()[3],
            spelModel.getWinkel().getInfoKaarten()[9],
            spelModel.getWinkel().getInfoKaarten()[7],
            spelModel.getWinkel().getInfoKaarten()[6],
            spelModel.getWinkel().getInfoKaarten()[8],
            spelModel.getWinkel().getInfoKaarten()[11],
            spelModel.getWinkel().getInfoKaarten()[14]
        };

        ImageView [] winkelImageArr = new ImageView[kaarten.length];
        Label [] labels = new Label[kaarten.length];
        for(int i = 0; i < kaarten.length;i++){
            winkelImageArr[i] = kaarten[i].kaartImage();
            winkelImageArr[i].setId(String.valueOf(kaarten[i].getKaartId()));
            labels[i] = new Label(spelModel.getWinkel().getInfoKaarten()[Integer.parseInt(winkelImageArr[i].getId())].getAantalKaarten() + "X");
            if(kaarten[i].getAantalKaarten() == 0){
                winkelImageArr[i].setVisible(false);
                labels[i].setVisible(false);
            }
        }

        double standardSize = 1;
        double sizeOnHover = 1.1;
        double sizeOnClick = 1.5;

        //array winkel kaarten, de volgorde hier past de volgorde in het bord aan.
        for(ImageView imageView : winkelImageArr){
            winkelPane.getChildren().add(imageView);
        }
        int coordinateX = -36;
        int coordinateY = -25;
        for (int i = 0; i<winkelImageArr.length;i++){
            winkelImageArr[i].setFitHeight(hoogteImage);
            winkelImageArr[i].setFitWidth(breedteImage);
            winkelImageArr[i].toBack();
            labels[i].setStyle("-fx-font-size: 30; -fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-text-alignment: center");
            if (i<1) {
                winkelImageArr[i].setX(174);
                winkelImageArr[i].setY(274);
                labels[i].setTranslateX(174 + (breedteImage - 50));
                labels[i].toFront();
                coordinateX = coordinateX -185;
            }
            if (i==8){
                coordinateY = coordinateY + 280;
                coordinateX = coordinateX - 1560;
            }
            coordinateX = coordinateX + 195;
            winkelImageArr[i].setX(coordinateX);
            winkelImageArr[i].setY(coordinateY);
            labels[i].setTranslateX(coordinateX + (breedteImage - 50));
            labels[i].setTranslateY(coordinateY);
            labels[i].toFront();
            winkelPane.getChildren().add(labels[i]);
        }
        //padding van de winkel pane.
        winkelPane.setMaxSize(breedtePane,600);

        int index = 0;
        for(ImageView imageView : winkelImageArr){
            int i = index;
            double labelxPos = labels[i].getTranslateX();
            double labelyPos = labels[i].getTranslateY();
            imageView.setOnMouseEntered(e -> {
                playPopLow();
                imageView.toFront();
                labels[i].toFront();
                labels[i].setScaleX(sizeOnHover);
                labels[i].setScaleY(sizeOnHover);
                labels[i].setTranslateX(labelxPos + 7);
                labels[i].setTranslateY(labelyPos - 10);
                winkelPane.toFront();
                imageView.setScaleX(sizeOnHover);
                imageView.setScaleY(sizeOnHover);
            });

            imageView.setOnMouseClicked(e -> {
                if(kaartKopen && spelModel.getGegooid()){
                    kaartKopen = false;
                    if(!KoopKaartController.koopKaart(spelerId, Integer.parseInt(imageView.getId()))){
                        showNotification("Je kan deze kaart niet kopen", -60, 200);
                    } else {
                        playKassa();
                    }
                }
                imageView.toFront();
                imageView.setScaleX(sizeOnClick);
                imageView.setScaleY(sizeOnClick);
                labels[i].toFront();
                labels[i].setScaleX(sizeOnClick);
                labels[i].setScaleY(sizeOnClick);
                labels[i].setTranslateX(labelxPos + 32);
                labels[i].setTranslateY(labelyPos - 55);
            });

            imageView.setOnMouseExited(e -> {
                for(ImageView imageViews : winkelImageArr){
                    imageViews.toBack();
                }
                winkelPane.toBack();
                imageView.setScaleX(standardSize);
                imageView.setScaleY(standardSize);
                labels[i].setTranslateX(labelxPos);
                labels[i].setTranslateY(labelyPos);
                labels[i].setScaleX(standardSize);
                labels[i].setScaleY(standardSize);
            });
            index++;
        }
        return winkelPane;
    }
    /**
     * Spelerbox is het driehoekje,muntje en naam op het spelveld.
     * spelebox zet de correcte naam, munt en driehoek op de juiste plek.
     * @param spelerId geeft de spelerID mee van de speler.
     * @return de Vbox zodat deze aan het spel boord toegevoed kan worden.
     */
    private VBox spelerBox(int spelerId){

        //Naam
        Label naamLabel = new Label(spelModel.getSpelers().get(spelerId).getNaam());
        naamLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        naamLabel.setTextFill(Color.GOLD);

        //initializeren benodigde objecten voor bezienswaardigheden knoppen
        StackPane bezienswaardighedenPane = new StackPane();
        bezienswaardighedenPane.setId("BeziensPane");
        bezienswaardighedenPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        //bezienswaardighedendriehoek
        Image beziensDriehoek = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/BeziensDriehoek.png"));
        ImageView beziensDriehoekView = new ImageView(beziensDriehoek);
        beziensDriehoekView.setFitHeight(100);
        beziensDriehoekView.setFitWidth(100);

        //text settings
        Label beziensLabel = new Label(Integer.toString(spelModel.getSpelers().get(spelerId).getAantalBeziens()) + "/4");
        beziensLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
        beziensLabel.setTextFill(Color.BLACK);
        beziensLabel.setPadding(new Insets(38, 0,0,0));
        beziensLabel.setOnMouseEntered(e -> playPop());
        beziensLabel.setOnMouseClicked(e -> {
            playClick();
            stackPane.getChildren().add(bezienswaardigheidKaarten.get(spelerId));
        });
        bezienswaardighedenPane.getChildren().addAll(beziensDriehoekView,beziensLabel);

        //Munten
        StackPane muntenPane = new StackPane();
        Circle muntCircle = new Circle(30);
        Label muntLabel = new Label(Integer.toString(spelModel.getSpelers().get(spelerId).getAantalMunten()));
        muntLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
        muntCircle.setFill(Color.GOLD);
        muntCircle.setStroke(Color.GOLDENROD);
        muntCircle.setStrokeWidth(12);
        muntenPane.getChildren().addAll(muntCircle, muntLabel);

        //HorizonBox voor munten en bezienswaardigheden
        HBox muntBeziens = new HBox();
        muntBeziens.getChildren().addAll(muntenPane, bezienswaardighedenPane);

        //VBox totaal
        VBox totaal = new VBox(5);
        totaal.getChildren().addAll(naamLabel,muntBeziens);
        totaal.setMaxSize(200,100);
        int spelerPos;
        if(spelerId - this.spelerId < 0){
            spelerPos = spelerId - this.spelerId + spelModel.getSpelers().size();
        } else{
            spelerPos = spelerId - this.spelerId;
        }
        switch(spelerPos){
            case 0 :
                totaal.setTranslateY(420);
                totaal.setTranslateX(-530);
                break;
            case 1 :
                totaal.setTranslateY(-425);
                totaal.setTranslateX(560);
                break;
            case 2 :
                totaal.setTranslateY(-425);
                totaal.setTranslateX(-700);
                break;
            case 3 :
                totaal.setTranslateY(420);
                totaal.setTranslateX(720);
                break;
        }

        return totaal;
    }
    /**
     * koopKaartEindeBeurtKnop zet de twee knoppen: koopKaart en EindeBeurt op de juiste plek en geeft functionaliteit.
     * @return de HBox zodat deze aan het spel Boord toegevoegd kan worden.
     */
    private HBox koopKaartEindeBeurtKnop(){
        HBox kaartBeurt = new HBox(20);

        Button koopKaartKnop = new Button();
        koopKaartKnop.setId("KoopKaart");
        koopKaartKnop.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        Button eindBeurtKnop = new Button();
        eindBeurtKnop.setId("EindeBeurt");
        eindBeurtKnop.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        kaartBeurt.getChildren().addAll(koopKaartKnop, eindBeurtKnop);
        kaartBeurt.setTranslateX(-450);
        kaartBeurt.setTranslateY(285);
        kaartBeurt.setMaxSize(100,75);
        
        koopKaartKnop.setOnAction(e -> {
            playClick();
            kaartKopen = true;
        });
        koopKaartKnop.setOnMouseEntered(e -> playPop());
        
        eindBeurtKnop.setOnAction(e -> {
            playClick();
            BeurtAfrondController.rondBeurtAf();
        });
        eindBeurtKnop.setOnMouseEntered(e -> playPop());

        return kaartBeurt;
    }
    /**
     *  dobbelstenenKnop zet de twee knoppen, 1 of 2 dobbelstenen verticaal van elkaar.
     * @return de VBox zodat deze aan het spel boort toegevoegd kunnen worden.
     */
    private VBox dobbelstenenKnop(){
        VBox dobbelstenen  = new VBox(-2);
        dobbelstenen.setMinSize(50,100);

        Image dobbelsteenEen = new Image(getClass().getResourceAsStream("/resources/Knoppen/EenDobbelsteen_Button.png"));
        ImageView dobbelEen = new ImageView(dobbelsteenEen);

        dobbelstenen.getChildren().add(dobbelEen);
        dobbelstenen.setTranslateX(620);
        dobbelstenen.setTranslateY(10);
        dobbelstenen.setMaxSize(50, 100);
        dobbelEen.setOnMouseClicked(e -> {
            if(!spelModel.getGegooid()){
                playEenDobbelsteen();
                GooiDobbelsteenController.gooiDobbelsteen(false);
            }
        });

        if(spelModel.getSpelers().get(spelerId).heeftTreinstation()){
            Image dobbelsteenTwee = new Image(getClass().getResourceAsStream("/resources/Knoppen/TweeDobbelstenen_Button.png"));
            ImageView dobbelTwee = new ImageView(dobbelsteenTwee);

            dobbelTwee.setOnMouseClicked(e -> {
                if(!spelModel.getGegooid()){
                    GooiDobbelsteenController.gooiDobbelsteen(true);
                    playTweeDobbelstenen();
                }
            });

            dobbelTwee.setOnMouseEntered(e -> playPop());

            dobbelstenen.getChildren().add(dobbelTwee);
        }

        if(spelModel.isEersteKeerGegooid() && (spelModel.getSpelers().get(spelerId).heeftRadiostation() || spelModel.getSpelers().get(spelerId).heefPretpark())){
            Button beëindigGooien = new Button();
            beëindigGooien.setId("BeeindigGooien");
            beëindigGooien.setScaleX(0.9);
            beëindigGooien.setScaleY(0.9);
            beëindigGooien.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
            beëindigGooien.setTranslateX(-10);
            beëindigGooien.setTranslateY(-10);
            beëindigGooien.setOnAction(e ->
                BeëindigGooienController.beëindigGooien()
            );
            dobbelstenen.getChildren().add(beëindigGooien);
        }
        dobbelEen.setOnMouseEntered(e -> playPop());

        return dobbelstenen;
    }
    /**
     * setttingsKnop zet de settingsknop en de handleiding knop in een HBox met de correcte opmaak.
     * @return de HBox zodat deze aan het spel boort toegevoegd kunnen worden.
     */
    private HBox settingsKnop(){
        HBox settingsBox = new HBox(20);

        Image handleidingImg = new Image(getClass().getResourceAsStream("/resources/Knoppen/Spelregels_Button.png"));
        ImageView handleidingIV = new ImageView(handleidingImg);
        handleidingIV.setOnMouseClicked(e -> {
            playClick();
            stackPane.getChildren().add(handleidingImageView);
        });
        handleidingIV.setOnMouseEntered(e -> playPop());

        Image settingsImg = new Image(getClass().getResourceAsStream("/resources/Knoppen/Settings_Button.png"));
        ImageView settingsIV = new ImageView(settingsImg);
        settingsIV.setOnMouseClicked(e -> {
            playClick();
            stackPane.getChildren().add(settings);

        });
        settingsIV.setOnMouseEntered(e -> playPop());

        settingsBox.setMaxSize(50, 25);
        settingsBox.setTranslateX(850);
        settingsBox.setTranslateY(-470);
        settingsBox.getChildren().addAll(handleidingIV, settingsIV);

        return settingsBox;
    }
    /**
     * de image die wordt getoond als je de handleiding opvraagd.
     * @return de handleiding image.
     */
    private ImageView handleidingImage(){

        Image handleidingIMG = new Image("/resources/HandLeiding.png");
        ImageView handleidingIV = new ImageView(handleidingIMG);

        handleidingIV.setOnMouseClicked(e -> {
            playPopLow();
            stackPane.getChildren().remove(handleidingImageView);
        });

        return handleidingIV;
    }

    /**
     * Geeft de bezienswaardigheden scherm weer op het moment dat je deze opvraagd.
     * @return de bezienswaardigheden scherm.
     */
    private VBox bezienswaardighedenVBox(int spelerId){
        VBox totaal = new VBox(50);
        totaal.setId("VBoxBeziens");
        totaal.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        double height = 500;
        double width = 350;

        //toevoegen bezienswaardigheden images
        Image treinstationFalse = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/TreinstationNietGekocht.png"));
        Image winkelcentrumFalse = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/WinkelcentrumNietGekocht.png"));
        Image pretparkFalse = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/PretparkNietGekocht.png"));
        Image radiostationFalse = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/RadiostationNietGekocht.png"));
        Image treinstationTrue = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/Treinstation.png"));
        Image winkelcentrumTrue = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/Winkelcentrum.png"));
        Image pretparkTrue = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/Pretpark.png"));
        Image radiostationTrue = new Image(getClass().getResourceAsStream("/resources/Bezienswaardigheden/Radiostation.png"));

        ImageView treinstation;
        ImageView winkelcentrum;
        ImageView pretpark;
        ImageView radiostation;

        //checken of kbezienswaaridgheden zijn gekocht (1 if statement per kaart)
        if (spelModel.getSpelers().get(spelerId).heeftTreinstation()){
            treinstation = new ImageView(treinstationTrue);
        } else {
            treinstation = new ImageView(treinstationFalse);
        }

        if (spelModel.getSpelers().get(spelerId).heeftWinkelcentrum()){
            winkelcentrum = new ImageView(winkelcentrumTrue);
        } else {
            winkelcentrum = new ImageView(winkelcentrumFalse);
        }

        if (spelModel.getSpelers().get(spelerId).heefPretpark()){
            pretpark = new ImageView(pretparkTrue);
        } else {
            pretpark = new ImageView(pretparkFalse);
        }

        if (spelModel.getSpelers().get(spelerId).heeftRadiostation()){
            radiostation = new ImageView(radiostationTrue);
        } else {
            radiostation = new ImageView(radiostationFalse);
        }

        treinstation.setFitHeight(height);
        treinstation.setFitWidth(width);
        winkelcentrum.setFitHeight(height);
        winkelcentrum.setFitWidth(width);
        pretpark.setFitHeight(height);
        pretpark.setFitWidth(width);
        radiostation.setFitHeight(height);
        radiostation.setFitWidth(width);

        double sizeOnClicked = 1.1;
        double sizeOnExit = 1.0;

        treinstation.setOnMouseClicked(e -> {

            if (beziensKopen) {
                beziensKopen = false;
                if (!KoopTreinstationController.koopTreinstation(spelerId)){
                    showNotification("Je hebt te weinig munten om deze kaart te kopen", 100, -460);
                } else {
                    playKassa();
                }
                treinstation.setScaleX(sizeOnExit);
                treinstation.setScaleY(sizeOnExit);
            }
        });
        treinstation.setOnMouseEntered(e -> playPop());

        winkelcentrum.setOnMouseClicked(e -> {
            if (beziensKopen) {
                beziensKopen = false;
                if (!KoopWinkelcentrumController.koopWinkelcentrum(spelerId)){
                    showNotification("Je hebt te weinig munten om deze kaart te kopen", 100, -460);
                } else {
                    playKassa();
                }
                winkelcentrum.setScaleX(sizeOnExit);
                winkelcentrum.setScaleY(sizeOnExit);
            }
        });
        winkelcentrum.setOnMouseEntered(e -> playPop());

        pretpark.setOnMouseClicked(e -> {
            if (beziensKopen ) {
                beziensKopen = false;
                if (!KoopPretparkController.koopPretpark(spelerId)) {
                    showNotification("Je hebt te weinig munten om deze kaart te kopen", 100, -460);
                } else {
                    playKassa();
                }
                pretpark.setScaleX(sizeOnExit);
                pretpark.setScaleY(sizeOnExit);
            }
        });
        pretpark.setOnMouseEntered(e -> playPop());

        radiostation.setOnMouseClicked(e -> {
            if (beziensKopen) {
                beziensKopen = false;
                if (!KoopRadiostationController.koopRadiostation(spelerId)) {
                    showNotification("Je hebt te weinig munten om deze kaart te kopen", 100, -460);
                } else {
                    playKassa();
                }
                radiostation.setScaleX(sizeOnExit);
                radiostation.setScaleY(sizeOnExit);
            }

        });
        radiostation.setOnMouseEntered(e -> playPop());

        //toevoegen bezienswaardigheden aan HBox
        HBox kaarten = new HBox(40);
        kaarten.getChildren().addAll(treinstation, winkelcentrum, pretpark, radiostation);

        //buttons initializatie en toevoegen aan HBox
        Button terug = new Button();
        terug.setId("TerugKnop");
        terug.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        terug.setOnMouseClicked(e -> {
            playClick();
            stackPane.getChildren().remove(bezienswaardigheidKaarten.get(spelerId));

            winkelcentrum.setScaleX(sizeOnExit);
            winkelcentrum.setScaleY(sizeOnExit);

            pretpark.setScaleX(sizeOnExit);
            pretpark.setScaleY(sizeOnExit);

            radiostation.setScaleX(sizeOnExit);
            radiostation.setScaleY(sizeOnExit);

            treinstation.setScaleX(sizeOnExit);
            treinstation.setScaleY(sizeOnExit);
        });
        terug.setOnMouseEntered(e -> playPop());

        Button kopen = new Button();
        kopen.setId("KopenKnop");
        kopen.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        kopen.setOnMouseClicked(e -> {
            playClick();
            beziensKopen = true ;

            if (!spelModel.getSpelers().get(spelerId).heeftTreinstation()) {
                treinstation.setScaleX(sizeOnClicked);
                treinstation.setScaleY(sizeOnClicked);
            }

            if (!spelModel.getSpelers().get(spelerId).heeftWinkelcentrum()) {
                winkelcentrum.setScaleX(sizeOnClicked);
                winkelcentrum.setScaleY(sizeOnClicked);
            }

            if (!spelModel.getSpelers().get(spelerId).heefPretpark()) {
                pretpark.setScaleX(sizeOnClicked);
                pretpark.setScaleY(sizeOnClicked);
            }

            if (!spelModel.getSpelers().get(spelerId).heeftRadiostation()) {
                radiostation.setScaleX(sizeOnClicked);
                radiostation.setScaleY(sizeOnClicked);
            }
        });
        kopen.setOnMouseEntered(e -> playPop());

        HBox knoppen = new HBox(100);

        knoppen.getChildren().addAll(terug);
        if(spelModel.getBeurt() == this.spelerId && spelModel.getBeurt() == spelerId) {
            if (spelModel.getGegooid()) {
                knoppen.getChildren().add(kopen);
            }
        }

        knoppen.setPadding(new Insets(0, 0, 0, 200));


        totaal.getChildren().addAll(kaarten, knoppen);
        totaal.setPadding(new Insets(126, 0, 0, 227));



        return totaal;
    }

    /**
     * dit is het settings menu waar je het geluid kan aan of uitzetten en het spel verlaten
     * @return geeft het settings menu
     */
    private VBox settingsMenu(){

        VBox totaal = new VBox(40);
        totaal.setId("VBoxBeziens");
        totaal.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        Button terugButton = new Button();
        terugButton.setId("TerugKnop");
        terugButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        terugButton.setOnMouseClicked(e -> {
            playClick();
            stackPane.getChildren().remove(settings);
        });
        terugButton.setOnMouseEntered(e -> playPop());

        Button muteButton = new Button();
        muteButton.setId("MuteButton");
        muteButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        muteButton.setOnMouseClicked(e -> {
            playClick();
            muteSound = !muteSound;
        });
        muteButton.setOnMouseEntered(e -> playPop());

        Button exit = new Button();
        exit.setId("ExitLobbyButton");
        exit.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
        exit.setOnMouseClicked(e -> {
            playClick();
            System.exit(0);
        });
        exit.setOnMouseEntered(e -> playPop());

        totaal.getChildren().addAll(terugButton, muteButton, exit);
        totaal.setAlignment(Pos.CENTER);

        return totaal;
    }

    /**
     *
     * @param warning de waarschuwing
     * @param x de x positie
     * @param y de y positie
     */
    private void showNotification(String warning, int x, int y) {
        playWarning();
        Label warningLabel = new Label(warning);
        warningLabel.setTextFill(Color.RED);
        warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
        warningLabel.setTranslateX(x);
        warningLabel.setTranslateY(y);
        stackPane.getChildren().add(warningLabel);

        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ft.play();
    }

    
    private void playPop(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonPopHigh.wav").toString());
            audio.play();
        }
    }
    
    private void playPopLow(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonPopLow.wav").toString());
            audio.play();
        }
    }
    
    private void playClick(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonClicked.wav").toString());
            audio.play();
        }
    }
    
    private void playKassa(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/BoughtCard.wav").toString());
            audio.play();
        }
    }
    
    private void playEenDobbelsteen(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/RollingOneDice.wav").toString());
            audio.play();
        }
    }
    
    private void playTweeDobbelstenen(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/RollingTwoDice.wav").toString());
            audio.play();
        }
    }
    
    private void playWarning(){
        if (!muteSound) {
            AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/Warning.wav").toString());
            audio.play();
        }
    }
}