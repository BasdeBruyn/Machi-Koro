package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import ParentClasses.ClientController;
import Server.*;
import interfaces.SpelModelInterface;
import interfaces.ViewInterface;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
/**
 * in deze klasse zitten de verschillende JavaFX scenes van het menu.
 *
 * @author Maarten, Dennis, Bruno
 */
public class StartView extends Stage implements ViewInterface {

	private SpelModelInterface modelInterface;
	private String serverAddress = "127.0.0.1";
	private String naam;
	private String [] namen;
	private SpelController controller;
	private boolean hostStartGame = true;
	private int spelerId;


	public StartView() {
		this.setScene(startScene());
	}

    /**
     * startclient is het scherm dat je ziet zodra je als host een lobby aanmaakts of als speler een lobby joint.
     * @param serverAddress geeft het server IP mee.
     */
	private boolean startClient(String serverAddress){
		try {
			//maak het object remote
			UnicastRemoteObject.exportObject(this,0);
			// vraag het model op uit de registry
            modelInterface = (SpelModelInterface) Naming.lookup("//" + serverAddress + "/model");
			ClientController.setServerAdres(serverAddress);
			return true;
		} catch (NotBoundException | MalformedURLException | RemoteException nbe){
			return false;
		}
	}
    /**
     * Startscene is de scene die je ziet als je het spel opstart, deze scene heeft twee optie knoppen.
     * je kan joinen of starten. (joinen = speler, starten = hosten)
     * @return de scene om aan de primarystage toe te voegen.
     */
	private Scene startScene(){
		//introsong wordt afgespeeld

		StackPane stackPane  = new StackPane();
		stackPane.setId("paneBackground");
		stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Button startButton = new Button();
		startButton.setId("StartButton");
		startButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		startButton.setOnAction(e -> {
			playClick();
            //maak een server aan als je een nieuw spel aanmaakt
            controller = new SpelController();
            this.setScene(hostNaamScene());
		});
		startButton.setOnMouseEntered(e -> playPop());

		Button joinButton = new Button();
		joinButton.setId("JoinButton");
		joinButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		joinButton.setOnAction(e -> {
			playClick();
		    //je bent geen host
			hostStartGame = false;
			this.setScene(naamScene());
		});
		joinButton.setOnMouseEntered(e -> playPop());

		VBox startButtons = new VBox(20);
		startButtons.setId("StartVBox");
		startButtons.getChildren().addAll(startButton, joinButton);
		startButtons.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		stackPane.getChildren().addAll(startButtons);
		stackPane.setAlignment(Pos.CENTER_LEFT);

		return new Scene(stackPane, 1920, 1010);
	}
    /**
     * deze scene is de scene voor de host waar hij zijn naam kan invullen.
     * @return de scene zodat deze aan de primary stage gebonden kan worden.
     */
	private Scene hostNaamScene(){
		StackPane stackPane  = new StackPane();
		stackPane.setId("paneBackground");
		stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Image naamInvulImage = new Image(getClass().getResourceAsStream("/resources/Knoppen/NaamInvul_Text.png"));
		Label naamInvulLabel = new Label();
		naamInvulLabel.setGraphic(new ImageView(naamInvulImage));

		TextField naamInvullen = new TextField();
		naamInvullen.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Button verderButton = new Button();
		verderButton.setId("VerderButton");
		verderButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		verderButton.setOnAction(e -> {
			playClick();
			//checken naam lengte, als de naam te kort is dan melding
			if ( naamInvullen.getText().length() >= 1 && naamInvullen.getText().length() <= 6){
				//je naam is wat je in de textfield invulde
				naam = naamInvullen.getText();
				this.setScene(hostScene());
			} else {
				playWarning();
				Label warningLabel = new Label("De ingevulde naam moet minimaal 1, en maximaal 6 karakters lang zijn");
				warningLabel.setTextFill(Color.RED);
				warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
				warningLabel.setTranslateX(200);
				warningLabel.setTranslateY(-200);
				stackPane.getChildren().add(warningLabel);

				FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);

				ft.play();
			}
		});
		verderButton.setOnMouseEntered(e -> playPop());

		VBox startButtons = new VBox(20);
		startButtons.setId("StartVBox");
		startButtons.setMaxWidth(550);
		startButtons.getChildren().addAll(naamInvulLabel, naamInvullen, verderButton);
		startButtons.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		stackPane.getChildren().add(startButtons);
		stackPane.setAlignment(Pos.CENTER_LEFT);

		return new Scene(stackPane, 1920, 1010);
	}

    /**
     * dit is kiesGeleadenSpeler, deze scene laad spelers hun vorige plaats innemen
     * @return de scene voor het kiezen van een speler
     */
	private Scene kiesGeladenSpeler() throws RemoteException {
	    String [] opgeslagenNamen = modelInterface.getOpgeslagenNamen();
	    Label label = new Label("Kies jouw naam:");
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
        label.setTranslateX(20);
        label.setTranslateY(20);
        GridPane gridPane = new GridPane();
        Pane pane = new Pane(label,gridPane);
        int row = 0;
        for(int i = 0; i < modelInterface.aantalSpelers();i++){
            Label naamLabel = new Label(opgeslagenNamen[i]);
            naamLabel.setTextFill(Color.WHITE);
            naamLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 60));
            Button knop = new Button();
            knop.setScaleX(0.8);
            knop.setScaleY(0.8);
            knop.setId("KiesSpeler");
            knop.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
            gridPane.add(naamLabel,0,row);
            gridPane.add(knop,1,row);
            gridPane.setVgap(80);
            gridPane.setTranslateX(100);
            gridPane.setTranslateY(150);
            int id = i;
            int rij = row;
            knop.setOnAction(e -> {
                try {
                	spelerId = id;
                    if(!modelInterface.kiesSpeler(this, id)){
                        Label warningLabel = new Label("Deze speler is al gekozen");
                        warningLabel.setTextFill(Color.RED);
                        warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
                        warningLabel.setTranslateX(0);
                        warningLabel.setTranslateY(0);
                        gridPane.add(warningLabel,2,rij);

                        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
                        ft.setFromValue(1.0);
                        ft.setToValue(0.0);

                        ft.play();
                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            });
            row++;
        }
        pane.setId("KiesSpelerPane");
        pane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/bordStyle.css").toExternalForm());
        Scene scene = new Scene(pane,1920,1010);
        return scene;
    }
    /**
     * deze scene is de naascene voor de speler, de speler kan hier zijn naam invullen en
     * het IP-address van de host.
     * @return de scene zodat deze aan de primary stage gebonden kan worden.
     */
	private Scene naamScene(){
		StackPane stackPane  = new StackPane();
		stackPane.setId("paneBackground");
		stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Image naamInvulImage = new Image(getClass().getResourceAsStream("/resources/Knoppen/NaamInvul_Text.png"));
		Label naamInvulLabel = new Label();
		naamInvulLabel.setGraphic(new ImageView(naamInvulImage));

		TextField naamInvullen = new TextField();
		naamInvullen.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Image ipImage = new Image(getClass().getResourceAsStream("/resources/Knoppen/IpInvul_Text.png"));
		Label ipLabel = new Label();
		ipLabel.setGraphic(new ImageView(ipImage));

		TextField ipInvullen = new TextField();
		ipInvullen.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Button verderButton = new Button();
		verderButton.setId("VerderButton");
		verderButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		verderButton.setOnAction(e -> {
			playClick();
			//checken naam lengte, als de naam te kort is dan melding
			if  (naamInvullen.getText().length() >= 1 && naamInvullen.getText().length() <= 6){

				//je naam is de naam die je in de textfield invulde
				naam = naamInvullen.getText();
				//het ipaddress is de ipaddress die je in de textfield invulde
				serverAddress = ipInvullen.getText();
				//start de client
				if(startClient(serverAddress)) {
					try {
						spelerId = modelInterface.registerClient(this, naam);
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
					if( spelerId >= 0) {
						this.setScene(new Scene(lobbyScene(), 1920, 1010));
					} else if (spelerId == -1){
						playWarning();
						Label warningLabel = new Label("De lobby is vol. Begin een nieuw spel of probeer het later nogmaals");
						warningLabel.setTextFill(Color.RED);
						warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
						warningLabel.setTranslateX(200);
						warningLabel.setTranslateY(-270);
						stackPane.getChildren().add(warningLabel);

						FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
						ft.setFromValue(1.0);
						ft.setToValue(0.0);

						ft.play();
					} else if(spelerId == -2){
						try {
							this.setScene(kiesGeladenSpeler());
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					playWarning();
					Label warningLabel = new Label("Het ingevulde ip-adres is verkeert of er is nog geen spel aangemaakt");
					warningLabel.setTextFill(Color.RED);
					warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 40));
					warningLabel.setTranslateX(200);
					warningLabel.setTranslateY(-400);
					stackPane.getChildren().add(warningLabel);

					FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
					ft.setFromValue(1.0);
					ft.setToValue(0.0);

					ft.play();
				}
                //registreer de client


			} else {
				playWarning();
				Label warningLabel = new Label("De ingevulde naam moet minimaal 1, en maximaal 6 karakters lang zijn");
				warningLabel.setTextFill(Color.RED);
				warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
				warningLabel.setTranslateX(200);
				warningLabel.setTranslateY(-270);
				stackPane.getChildren().add(warningLabel);

				FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);

				ft.play();
			}
		});
		verderButton.setOnMouseEntered(e -> playPop());


		VBox startButtons = new VBox(20);
		startButtons.setId("StartVBox");
		startButtons.setMaxWidth(550);
		startButtons.getChildren().addAll(naamInvulLabel, naamInvullen, ipLabel, ipInvullen, verderButton);
		startButtons.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		stackPane.getChildren().addAll(startButtons);
		stackPane.setAlignment(Pos.CENTER_LEFT);

		return new Scene(stackPane, 1920, 1010);
	}
    /**
     * deze scene is de hostscene, deze scene is voor de host om te bepalen of hij een nieuw spel
     * wilt opstarten of verder te gaan met een opgeslagen spel.
     * @return de scene zodat deze aan de primary stage gebonden kan worden.
     */
	private Scene hostScene(){
		StackPane stackPane  = new StackPane();
		stackPane.setId("paneBackground");
		stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Button nieuwSpelButton = new Button();
		nieuwSpelButton.setId("NieuwSpelButton");
		nieuwSpelButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		nieuwSpelButton.setOnAction(e -> {
			startClient(serverAddress);
			try {
				modelInterface.registerClient(this,naam);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			playClick();
			this.setScene(new Scene(lobbyScene(), 1920, 1010));
		});
		nieuwSpelButton.setOnMouseEntered(e -> playPop());

		Button spelLadenButton = new Button();
		spelLadenButton.setId("SpelLadenButton");
		spelLadenButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		spelLadenButton.setOnAction(e -> {
			playClick();
			if(controller.laadSpel()){
				try {
					//start de client
					startClient(serverAddress);
					this.setScene(kiesGeladenSpeler());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else {
				playWarning();
				Label warningLabel = new Label("Er is geen spel opgeslagen of het is corrupt");
				warningLabel.setTextFill(Color.RED);
				warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 50));
				warningLabel.setTranslateX(100);
				warningLabel.setTranslateY(-250);
				stackPane.getChildren().add(warningLabel);

				FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);

				ft.play();
			}
		});
		spelLadenButton.setOnMouseEntered(e -> playPop());

		VBox startButtons = new VBox(20);
		startButtons.setId("StartVBox");
		startButtons.getChildren().addAll(nieuwSpelButton, spelLadenButton);
		startButtons.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		stackPane.getChildren().addAll(startButtons);
		stackPane.setAlignment(Pos.CENTER_LEFT);

		return new Scene(stackPane, 1920, 1010);
	}
    /**
     * deze scene is de lobbyscene, deze scene regelt de lobby, elke speler die mee wilt doen zal in deze lobby terecht komen.
     * de scene update op het moment dat er een nieuwe speler binnenkomt.
     * @return de scene zodat deze aan de primary stage gebonden kan worden.
     */
	private StackPane lobbyScene(){
		StackPane stackPane  = new StackPane();
		stackPane.setId("LobbyBackground");
		stackPane.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Label spelerEen = new Label(namen[0]);
		spelerEen.setId("LobbyLabel");
		spelerEen.setTextFill(Color.WHITE);
		spelerEen.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		spelerEen.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Label spelerTwee = new Label(namen[1]);
		spelerTwee.setId("LobbyLabel");
		spelerTwee.setTextFill(Color.WHITE);
		spelerTwee.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		spelerTwee.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Label spelerDrie = new Label(namen[2]);
		spelerDrie.setId("LobbyLabel");
		spelerDrie.setTextFill(Color.WHITE);
		spelerDrie.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		spelerDrie.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		Label spelerVier = new Label(namen[3]);
		spelerVier.setId("LobbyLabel");
		spelerVier.setTextFill(Color.WHITE);
		spelerVier.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
		spelerVier.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		VBox spelerNamen = new VBox(95);
		spelerNamen.getChildren().addAll(spelerEen, spelerTwee, spelerDrie, spelerVier);
		spelerNamen.setPadding(new Insets(225, 0, 0, 150));

		Button startLobbyButton = new Button();
		startLobbyButton.setId("StartLobbyButton");
		startLobbyButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		startLobbyButton.setOnAction(e -> {
			playClick();
			if (controller.aantalSpelers() > 1) {
				Thread thread = new Thread(controller);
				thread.start();
			}else {
				playWarning();
				Label warningLabel = new Label("Er zijn te weinig spelers om het spel te starten");
				warningLabel.setTextFill(Color.RED);
				warningLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 25));
				warningLabel.setTranslateX(100);
				warningLabel.setTranslateY(-460);
				stackPane.getChildren().add(warningLabel);

				FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(6000), warningLabel);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);

				ft.play();
			}
        });
		startLobbyButton.setOnMouseEntered(e -> playPop());

		Button exitLobbyButton = new Button();
		exitLobbyButton.setId("ExitLobbyButton");
		exitLobbyButton.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());
		exitLobbyButton.setOnAction(e -> System.exit(0));
		exitLobbyButton.setOnMouseEntered(e -> playPop());

		HBox lobbyButtons = new HBox(20);
		lobbyButtons.setId("LobbyHBox");
		if (hostStartGame){
			lobbyButtons.getChildren().addAll(exitLobbyButton, startLobbyButton);
		} else {
			lobbyButtons.getChildren().addAll(exitLobbyButton);
		}
		lobbyButtons.getStylesheets().addAll(this.getClass().getResource("/resources/Knoppen/style.css").toExternalForm());

		stackPane.getChildren().addAll(spelerNamen, lobbyButtons);
		stackPane.setAlignment(Pos.CENTER_LEFT);

		return stackPane;
	}
    /**
     * spelergejoined updated de lobbyscene op het moment dat er een nieuwe speler in komt.
     * @param namen namen van spelers
     */
	public void spelerGejoined(String [] namen){
	    //vernieuw de namen
		this.namen = namen;
		//start de scene opnieuw om de namen te updaten
		this.getScene().setRoot(lobbyScene());
	}

    /**
     * elke keer dat het model veranderd wordt het scherm herladen met het nieuwe model
     * @param model is het vernieuwde model
     */
	public void modelChanged(SpelModel model) {
	    this.getScene().setRoot(new SpelView(model,spelerId).mainScene());
		//this.getScene().setRoot(new RuilKaartView(model,spelerId));
    }

    /**
     * als een speler moet ruilen zal deze methode het scherm tonen om te ruilen
     * @param model is het huidig model
     */
    public void ruilKaart(SpelModel model){
        this.getScene().setRoot(new RuilKaartView(model,spelerId));
	}

    /**
     * als een speler moet stelen zal deze methode het scherm tonen om munten te stelen
     * @param model is het huidig model
     */
	public void steelMunten(SpelModel model){
	    this.getScene().setRoot(new SteelMuntenView(model,spelerId));
    }

    /**
     * als een speler gewonnen heeft laat deze methode het eindspel zien
     * @param naam van de winnaar
     */
    public void showWinnaar(String naam){
		this.getScene().setRoot(new WinnerView(naam));
	}
    
	private void playPop(){
		AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonPopHigh.wav").toString());
		audio.play();
	}
    
	private void playClick(){
		AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/ButtonClicked.wav").toString());
		audio.play();
	}
    
	private void playWarning(){
		AudioClip audio = new AudioClip(this.getClass().getResource("/resources/Sound/Warning.wav").toString());
		audio.play();
	}
}




