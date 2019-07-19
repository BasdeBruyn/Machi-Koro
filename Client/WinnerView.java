package Client;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Deze klassen laat zien wie er gewonnen heeft en geeft de mogenlijkheid om het spel te verlaten.
 * @author Bas, Maarten
 */
public class WinnerView extends Pane {

    Pane pane = new Pane();
    /**
     * javaFX code om het scherm op te maken en funtionaliteit te geven aan de knoppen.
     * @param naam van de speler die gewonnen heeft
     */
    public WinnerView(String naam) {
        pane.setId("WinnaarPane");
        pane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());

        Image transBackground = new Image(getClass().getResourceAsStream("/resources/Knoppen/Menu_Background.png"));
        ImageView background = new ImageView(transBackground);

        Label label = new Label(naam + " heeft het spel gewonnen!");
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
        playWinner();

        Button exit = new Button();
        exit.setId("ExitLobbyButton");
        exit.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
        exit.setOnMouseClicked(e -> {
            playClick();
            System.exit(0);
        });
        exit.setOnMouseEntered(e -> playPop());

        VBox vbox = new VBox(100);
        vbox.getChildren().addAll(label, exit);

        pane.setMaxSize(1920, 1010);
        pane.getChildren().add(background);
        pane.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.setTranslateX(100);
        vbox.setTranslateY(300);
        this.getChildren().add(pane);

    }
    
    private void playClick() {
        AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonClicked.wav").toString());
        audio.play();
    }
    
    private void playPop() {
        AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonPopHigh.wav").toString());
        audio.play();
    }
    
    private void playWinner(){
        AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/WinnerSound.wav").toString());
        audio.play();

    }

}
