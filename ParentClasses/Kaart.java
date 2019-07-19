package ParentClasses;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.rmi.RemoteException;
/**
 * dit is de parent klasse van de verschillende kaarten, hiering staan de methods en atributen die elke klasse nodig heeft.
 * @author Bas,Peter
 */
public abstract class Kaart implements Serializable {
    public enum Prioriteiten {GROEN, BLAUW, ROOD, PAARS}

    private int kosten;
    private int kaartId;
    private int aantalKaarten;
    private Prioriteiten prioriteit;

    public Kaart(int aantalKaarten){
        this.aantalKaarten = aantalKaarten;
    }

    protected void setBeginWaardes(int kosten, int kaartId, Prioriteiten prioriteit){
        this.kosten = kosten;
        this.kaartId = kaartId;
        this.prioriteit = prioriteit;
    }

    public boolean minEenKaart(){
        if(aantalKaarten <= 0){
            return false;
        } else {
            aantalKaarten -= 1;
            return true;
        }
    }

    public void plusEenKaart(){
        this.aantalKaarten = aantalKaarten + 1;
    }

    public int getKosten() {
        return kosten;
    }

    public int getKaartId() {
        return kaartId;
    }

    public int getAantalKaarten(){
        return aantalKaarten;
    }

    public Prioriteiten getPrioriteit() {
        return prioriteit;
    }

    public abstract void checkActivatie(int beurtSpelerID, int aantalOgen, int spelerId) throws RemoteException;

    public abstract ImageView kaartImage();
}
