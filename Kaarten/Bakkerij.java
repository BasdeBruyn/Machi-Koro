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
 * Deze klasse heeft de eigenschappen van de kaart Bakkerij
 * @author Bas, Peter, Dennis
 */
public class Bakkerij extends Kaart implements Serializable {

    private final Prioriteiten PRIORITEIT = Prioriteiten.GROEN;
    private final int KOSTEN = 1;
    private final int KAARTID = 2;

    private SpelModelInterface modelInterface;

    public Bakkerij(int aantalKaarten){
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
    public void checkActivatie(int beurtSpelerID, int aantalOgen, int spelerId){
        try {
            if(aantalOgen == 2 || aantalOgen == 3){
                modelInterface = (SpelModelInterface) Naming.lookup("//" + "127.0.0.1" + "/model");
                if(modelInterface.heeftWinkelcentrum(spelerId)){
                    modelInterface.geefMunten(spelerId,2 * getAantalKaarten());
                }
                else {
                    modelInterface.geefMunten(spelerId, getAantalKaarten());
                }
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
        Image kaartImg = new Image(getClass().getResourceAsStream("../resources/Groen/Bakkerij.png"));
        return new ImageView(kaartImg);
    }

}
