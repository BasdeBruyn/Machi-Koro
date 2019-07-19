package Kaarten;

import ParentClasses.Kaart;
import interfaces.SpelModelInterface;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze klasse heeft de eigenschappen van de kaart Kaasfabriek
 * @author Bas, Peter, Dennis
 */
public class Kaasfabriek extends Kaart implements Serializable {

    private final Prioriteiten PRIORITEIT = Prioriteiten.GROEN;
    private final int KOSTEN = 5;
    private final int KAARTID = 10;

    private SpelModelInterface modelInterface;

    public Kaasfabriek(int aantalKaarten) {
        super(aantalKaarten);
        setBeginWaardes(KOSTEN,KAARTID,PRIORITEIT);
    }
    /**
     * kijkt of de kaart geactiveerd mag worden.
     * @param beurtSpelerID geeft aan van wie de kaart is.
     * @param aantalOgen geeft aan hoeveel ogen de dobbelsteen heeft gegooid.
     * @param spelerId geeft de speler mee voor wie het effect bedoeld is.
     */
    @Override
    public void checkActivatie(int beurtSpelerID, int aantalOgen, int spelerId) {
        try {
            if (aantalOgen == 7) {
                modelInterface = (SpelModelInterface) Naming.lookup("//" + "127.0.0.1" + "/model");
                int aantalVee = modelInterface.aantalKaart(spelerId, 1);
                modelInterface.geefMunten(spelerId, aantalVee * 3 * getAantalKaarten());
            }
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * hiermee kan je de image van de kaart ophalen
     * @return de image van de kaart
     */
    @Override
    public ImageView kaartImage(){
        Image kaartImg = new Image(getClass().getResourceAsStream("../resources/Groen/Kaasfabriek.png"));
        return new ImageView(kaartImg);
    }
}
