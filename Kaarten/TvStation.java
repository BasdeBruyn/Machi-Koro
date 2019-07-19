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
 * Deze klasse heeft de eigenschappen van de kaart TvStation
 * @author Bas, Peter, Dennis
 */
public class TvStation extends Kaart implements Serializable {

    private final Prioriteiten PRIORITEIT = Prioriteiten.PAARS;
    private final int KOSTEN = 7;
    private final int KAARTID = 7;

    private SpelModelInterface modelInterface;

    public TvStation(int aantalKaarten){
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
            modelInterface = (SpelModelInterface) Naming.lookup("//" + "127.0.0.1" + "/model");
            modelInterface.tvStationActivatie();
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
    /**
     * hiermee kan je de image van de kaart ophalen
     * @return de image van de kaart
     */
    @Override
    public ImageView kaartImage(){
        Image kaartImg = new Image(getClass().getResourceAsStream("../resources/Paars/TvStation.png"));
        return new ImageView(kaartImg);
    }
}
