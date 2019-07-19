package SpelObjecten;

import Kaarten.*;
import ParentClasses.Kaart;

import java.io.Serializable;
/**
 * zorgt er voor dat de winkel kaarten bevat.
 * @author Peter, Bas
 */
public class Winkel implements Serializable {

    private Kaart [] infoKaarten = new Kaart [15];

    public void initKaarten() {
        infoKaarten[0] = new Graanveld(6);
        infoKaarten[1] = new Veehouderij(6);
        infoKaarten[2] = new Bakkerij(6);
        infoKaarten[3] = new Cafe(6);
        infoKaarten[4] = new Supermarkt(6);
        infoKaarten[5] = new Bos(6);
        infoKaarten[6] = new Stadion(4);
        infoKaarten[7] = new TvStation(4);
        infoKaarten[8] = new Bedrijvencomplex(4);
        infoKaarten[9] = new Restaurant(6);
        infoKaarten[10] = new Kaasfabriek(6);
        infoKaarten[11] = new Meubelfabriek(6);
        infoKaarten[12] = new Mijn(6);
        infoKaarten[13] = new Appelboomgaard(6);
        infoKaarten[14] = new GroenteEnFruitmarkt(6);
    }

    public Kaart[] getInfoKaarten() {
        return infoKaarten;
    }
}
