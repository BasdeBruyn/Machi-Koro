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
 * Deze klasse heeft de eigenschappen van de kaart Bos
 * @author Bas, Peter, Dennis
 */
public class Bos extends Kaart implements Serializable {

    private final Prioriteiten PRIORITEIT = Prioriteiten.BLAUW;
    private final int KOSTEN = 3;
    private final int KAARTID = 5;

    private SpelModelInterface modelInterface;

    public Bos(int aantalKaarten) {
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
            if (aantalOgen == 5) {
                modelInterface = (SpelModelInterface) Naming.lookup("//" + "127.0.0.1" + "/model");
                modelInterface.geefMunten(spelerId, getAantalKaarten());
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
        Image kaartImg = new Image(getClass().getResourceAsStream("../resources/Blauw/Bos.png"));
        return new ImageView(kaartImg);
    }
}
