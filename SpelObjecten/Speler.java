package SpelObjecten;

import Kaarten.*;
import ParentClasses.Kaart;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * heeft alle informatie de een speler kan hebben, zijn spelerID, de opgegeven naam, het aantal munten, het aantal bezienswaardigheden
 * en de kaarten in de speler hand.
 */
public class Speler implements Serializable {
    final private int START_KAPITAAL_MUNTEN = 3;

    private int spelerId;
    private String naam;

    private int aantalMunten = START_KAPITAAL_MUNTEN;
    private int aantalBeziens = 0;
    private ArrayList<Kaart> spelerHand = new ArrayList<>();

    private boolean treinstation = false;
    private boolean winkelcentrum = false;
    private boolean pretpark = false;
    private boolean radiostation = false;

    public Speler(int spelerId, String naam) {
        super();
        this.spelerId = spelerId;
        this.naam = naam;
    }
    /**
     * zet de 2 begin kaarten in de speler hand.
     */
    public void startHand() {

        spelerHand.add(new Graanveld(1));
        spelerHand.add(new Bakkerij(1));

    }

    public int getAantalMunten() {
        return aantalMunten;
    }

    public String getNaam() {
        return naam;
    }
    /**
     * zorgt er voor dat je makelijk kaarten in een hand kan stoppen.
     * @param kaart de kaart die er in de hand moet komen.
     */
    public void geefKaart(Kaart kaart) {
        if (aantalKaart(kaart.getKaartId()) == 0) {
            spelerHand.add(kaart);
        } else {
            for (Kaart handKaart : spelerHand) {
                if (handKaart.getKaartId() == kaart.getKaartId()) {
                    handKaart.plusEenKaart();
                    return;
                }
            }
        }
    }
    /**
     * zorgt er voor dat je makelijk kaarten uit een hand kan halen.
     * @param kaart de kaart die uit de hand genomen moet worden.
     */
    public void neemKaart(Kaart kaart) {
        if (aantalKaart(kaart.getKaartId()) <= 1) {
            for (Kaart handKaart : spelerHand) {
                if (handKaart.getKaartId() == kaart.getKaartId()) {
                    spelerHand.remove(handKaart);
                    return;
                }
            }
        } else {
            for (Kaart handKaart : spelerHand) {
                if (handKaart.getKaartId() == kaart.getKaartId()) {
                    handKaart.minEenKaart();
                    return;
                }
            }
        }
    }
    /**
     * controleerd of de speler kaarten in zijn hand heeft die geactiveerd kunnen/mogen worden.
     * @param beurtSpelerId de speler die aan de beurt is.
     * @param aantalOgen het aantal de de dobbelsteen heeft gegooid.
     */
    public boolean checkActivatie(int beurtSpelerId, int aantalOgen) throws RemoteException {
        activeerKaartenInPriorteit(beurtSpelerId,aantalOgen);
        return checkPaarseKaart(beurtSpelerId,aantalOgen);
    }

    /**
     * geeft aan hoeveel van een soort kaart de speler heeft
     * @param kaartId
     * @return hoeveel kaarten de speler heeft met het opgegeven kaart id
     */
    public int aantalKaart(int kaartId) {
        for (Kaart kaart : spelerHand) {
            if (kaart.getKaartId() == kaartId) {
                return kaart.getAantalKaarten();
            }
        }
        return 0;
    }

    public boolean heefPretpark() {
        return pretpark;
    }

    public boolean heeftRadiostation() {
        return radiostation;
    }

    public boolean heeftTreinstation() {
        return treinstation;
    }

    public boolean heeftWinkelcentrum() {
        return winkelcentrum;
    }

    private void activeerKaartenInPriorteit(int beurtSpelerId, int aantalOgen) throws RemoteException {
        for (Kaart kaart : spelerHand) {
            if (beurtSpelerId != spelerId && kaart.getPrioriteit() == Kaart.Prioriteiten.ROOD) {
                kaart.checkActivatie(beurtSpelerId, aantalOgen, spelerId);
            }
        }
        for (Kaart kaart : spelerHand) {
            if (beurtSpelerId == spelerId && kaart.getPrioriteit() == Kaart.Prioriteiten.GROEN) {
                kaart.checkActivatie(beurtSpelerId, aantalOgen, spelerId);
            }
        }
        for (Kaart kaart : spelerHand) {
            if (kaart.getPrioriteit() == Kaart.Prioriteiten.BLAUW) {
                kaart.checkActivatie(beurtSpelerId, aantalOgen, spelerId);
            }
        }
    }

    private boolean checkPaarseKaart(int beurtSpelerId,int aantalOgen) throws RemoteException {
        for (Kaart kaart : spelerHand) {
            if (aantalOgen == 6 && kaart.getPrioriteit() == Kaart.Prioriteiten.PAARS && beurtSpelerId == spelerId) {
                kaart.checkActivatie(beurtSpelerId, aantalOgen, spelerId);
                if (kaart.getKaartId() != 6) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean heeftPaarseKaart(){
        for(Kaart kaart : spelerHand){
            if(kaart.getPrioriteit() == Kaart.Prioriteiten.PAARS){
                return true;
            }
        }
        return false;
    }
    public int getSpelerId() {
        return spelerId;
    }

    public void setSpelerId(int spelerId) {
        this.spelerId = spelerId;
    }

    public void setAantalMunten(int aantalMunten) {
        this.aantalMunten = aantalMunten;
    }

    public int getAantalBeziens() {
        return aantalBeziens;
    }

    public void setAantalBeziens(int aantalBeziens) {
        this.aantalBeziens = aantalBeziens;
    }

    public ArrayList<Kaart> getSpelerHand() {
        return spelerHand;
    }

    public void setTreinstation(boolean treinstation) {
        this.treinstation = treinstation;
    }

    public void setWinkelcentrum(boolean winkelcentrum) {
        this.winkelcentrum = winkelcentrum;
    }

    public void setPretpark(boolean pretpark) {
        this.pretpark = pretpark;
    }

    public void setRadiostation(boolean radiostation) {
        this.radiostation = radiostation;
    }
}