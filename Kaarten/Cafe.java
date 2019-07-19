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
 * Deze klasse heeft de eigenschappen van de kaart Cafe
 * @author Bas, Peter, Dennis
 */
public class Cafe extends Kaart implements Serializable {

    private final Prioriteiten PRIORITEIT = Prioriteiten.ROOD;
    private final int KOSTEN = 2;
    private final int KAARTID = 3;

    private SpelModelInterface modelInterface;

    public Cafe(int aantalKaarten){
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
            if(aantalOgen == 3) {
                modelInterface = (SpelModelInterface) Naming.lookup("//" + "127.0.0.1" + "/model");
                if (modelInterface.heeftWinkelcentrum(spelerId)) {
                    modelInterface.steelMunten(spelerId, beurtSpelerID, 2 * getAantalKaarten());
                }
                else{
                    modelInterface.steelMunten(spelerId,beurtSpelerID,getAantalKaarten());
                }
            }
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
        Image kaartImg = new Image(getClass().getResourceAsStream("../resources/Rood/Cafe.png"));
        return new ImageView(kaartImg);
    }
}
