package SpelObjecten;

import Kaarten.*;
import ParentClasses.Kaart;

import java.io.Serializable;
/**
 * Hier worden alle kaarten aangemaakt en wordt vastgemaakt aan de array.
 * @author Peter, Bas
 */
public class KaartenFactory implements Serializable {
    private Kaart [] kaarten = new Kaart[15];
    
    public KaartenFactory(){
        kaarten[0] = new Graanveld(1);
        kaarten[1] = new Veehouderij(1);
        kaarten[2] = new Bakkerij(1);
        kaarten[3] = new Cafe(1);
        kaarten[4] = new Supermarkt(1);
        kaarten[5] = new Bos(1);
        kaarten[6] = new Stadion(1);
        kaarten[7] = new TvStation(1);
        kaarten[8] = new Bedrijvencomplex(1);
        kaarten[9] = new Restaurant(1);
        kaarten[10] = new Kaasfabriek(1);
        kaarten[11] = new Meubelfabriek(1);
        kaarten[12] = new Mijn(1);
        kaarten[13] = new Appelboomgaard(1);
        kaarten[14] = new GroenteEnFruitmarkt(1);
    }
    
    public Kaart generateKaart(int kaartId){
        return kaarten[kaartId];
    }
    
    
}
