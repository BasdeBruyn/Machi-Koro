package Server;

import SpelObjecten.*;
import interfaces.SpelModelInterface;
import interfaces.ViewInterface;
import ParentClasses.Kaart;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
/**
 * Spel model bavat de methodes en atributen die nodig zijn om het spel te laten funnctioneren, het bevat dingen zoals: winkel, beurt, gooien, model updates,
 * kaarten, laden, playercount.
 * @author Bas, Dennis, Bruno
 */
public class SpelModel implements SpelModelInterface, Serializable{

    private ArrayList<ViewInterface> clients = new ArrayList<>();
    private String [] namen = {"","","",""};
    private int playerCount = 0;
    private Winkel winkel = new Winkel();
    private int beurt = 0;
    private boolean gegooid = false;
    private boolean eersteKeerGegooid = false;
    private boolean dubbelGegooid = false;
    private boolean beurtAfgerond = false;
    private int aantalOgen;
    private SpelModel updateModel;
    private boolean userPrompted = true;
    private KaartenFactory kaartenFactory = new KaartenFactory();
    private boolean geladen = false;
    private Worp worp = new Worp();
    private ArrayList<Integer> gejoindeIds = new ArrayList<>();
    private ArrayList<Speler> spelers = new ArrayList<>();
    /**
     * update/referesh alle panes/scenes op het moment dat deze methoden wordt gebruikt.
     */
    public synchronized void modelChanged(){
        updateModel = new ModelConverter(this);
        for(ViewInterface client : clients){
            try {
                client.modelChanged(updateModel);
            } catch (RemoteException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * met het opstarten van het spel zorgt deze methoden er voor dat de speler alle benodigde start atributen ontvangt.
     */
    public void initSpelModel(){
        winkel.initKaarten();
        for(int i = 0; i < clients.size(); i++){
            spelers.add(new Speler(i,namen[i]));
        }
        for(Speler speler : spelers){
            speler.startHand();
        }
    }

    /**
     * slaat het model op in modelSave.dat
     */
    public void slaSpelOp(){
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream("modelSave.dat");
            oos = new ObjectOutputStream(fos);
            updateModel = new ModelConverter(this);
            updateModel.setPlayerCount(0);
            oos.writeObject(updateModel);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notify ale spelers dat er een speler gejoined is
     */
    private synchronized void notifyClients() throws RemoteException {
        for (int i = 0; i < playerCount; i++) {
            clients.get(i).spelerGejoined(namen);
        }
    }

    /**
     * registreer de clients
     * @param client zijn RMI interface
     * @param naam van de speler
     * @return de speler id of geef de client mee dat hij niet kan registreren
     */
    public int registerClient(ViewInterface client, String naam) throws RemoteException {
        if(!geladen){
            //zodat er niet meer dan 4 spelers joinen
            if(playerCount >= 4){
                return -1;
            }
            //voeg de clients toe aan de client lijst
            clients.add(client);
            namen[playerCount] = naam;
            playerCount++;
            //meld alle clients dat er iemand gejoined is
            notifyClients();
            //return de nieuwe lijst met namen
            return clients.size() - 1;
        } else {
            return -2;
        }
    }
    /**
     * kijkt of de speler mag/kan kopen
     */
    public boolean kiesSpeler(ViewInterface client, int spelerId) throws RemoteException {
        for (Integer i : gejoindeIds) {
            if (i == spelerId) {
                return false;
            }
        }
        clients.set(spelerId, client);
        gejoindeIds.add(spelerId);
        playerCount++;
        client.spelerGejoined(namen);
        return true;
    }

    public boolean koopKaart(int spelerId, int kaartId) throws RemoteException {
        if(spelers.get(spelerId).getAantalMunten() < winkel.getInfoKaarten()[kaartId].getKosten()){
            return false;
        } else {
            if(!winkel.getInfoKaarten()[kaartId].minEenKaart()){
                return false;
            }
            if(spelers.get(spelerId).heeftPaarseKaart() && winkel.getInfoKaarten()[kaartId].getPrioriteit() == Kaart.Prioriteiten.PAARS){
                return false;
            }
            Kaart kaart = kaartenFactory.generateKaart(kaartId);
            spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten()- kaart.getKosten());
            spelers.get(spelerId).geefKaart(kaart);
            beurtAfgerond = true;
            return true;
        }
    }

    public boolean koopWinkelcentrum(int spelerId) throws RemoteException {
        if(spelers.get(spelerId).getAantalMunten() < 10) {
            return false;
        } else {
            spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten()- 10);
            spelers.get(spelerId).setWinkelcentrum(true);
            spelers.get(spelerId).setAantalBeziens(spelers.get(spelerId).getAantalBeziens() + 1);
            beurtAfgerond = true;
            return true;
        }
    }

    public boolean koopPretpark(int spelerId) throws RemoteException {
        if(spelers.get(spelerId).getAantalMunten() < 16){
            return false;
        } else {
            spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten()- 16);
            spelers.get(spelerId).setPretpark(true);
            spelers.get(spelerId).setAantalBeziens(spelers.get(spelerId).getAantalBeziens() + 1);
            beurtAfgerond = true;
            return true;
        }
    }

    public boolean koopRadioStation(int spelerId) throws RemoteException {
        if(spelers.get(spelerId).getAantalMunten() < 22){
            return false;
        } else {
            spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten()- 22);
            spelers.get(spelerId).setRadiostation(true);
            spelers.get(spelerId).setAantalBeziens(spelers.get(spelerId).getAantalBeziens() + 1);
            beurtAfgerond = true;
            return true;
        }
    }

    public boolean koopTreinstation(int spelerId) throws RemoteException {
        if(spelers.get(spelerId).getAantalMunten() < 4){
            return false;
        } else {
            spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten()- 4);
            spelers.get(spelerId).setTreinstation(true);
            spelers.get(spelerId).setAantalBeziens(spelers.get(spelerId).getAantalBeziens() + 1);
            beurtAfgerond = true;
            return true;
        }
    }

    /**
     * check of er een winnaar is
     * @return of er een winnaar is
     */
    public boolean checkWinnaar() throws RemoteException {
        if (spelers.get(beurt).getAantalBeziens() == 4){
            for(ViewInterface client : clients){
                client.showWinnaar(spelers.get(beurt).getNaam());
            }
            return true;
        }
        return false;
    }
    /**
     * spreekt de worp klasse aan om een/twee dobbelstenen te gooien.
     * @param tweeStenen of de speler 1 of 2 stenen wilt gooien.
     */
    public void gooiDobbelsteen(boolean tweeStenen) throws RemoteException {
        if(tweeStenen){
            dubbelGegooid = false;
            worp.gooiTweeDobbelstenen();
            aantalOgen = worp.aantalOgen();
            if(spelers.get(beurt).heefPretpark() && worp.dubbelGegooid()){
                dubbelGegooid = true;
                checkKaartenActivatie(beurt);
                modelChanged();
            } else if(spelers.get(beurt).heeftRadiostation() && !eersteKeerGegooid){
                eersteKeerGegooid =true;
                modelChanged();
            } else {
                gegooid = true;
            }
        } else {
            dubbelGegooid = false;
            worp.gooiEenDobbelsteen();
            aantalOgen = worp.aantalOgen();
            if(spelers.get(beurt).heeftRadiostation() && !eersteKeerGegooid){
                eersteKeerGegooid =true;
                modelChanged();
            } else {
                gegooid = true;
            }

        }
    }

    public void beëindigGooien() throws RemoteException{
        gegooid = true;
    }

    public void rondBeurtAf() throws RemoteException{
        beurtAfgerond = true;
    }

    public void geefMunten(int spelerId, int aantalMunten) throws RemoteException {
        spelers.get(spelerId).setAantalMunten(spelers.get(spelerId).getAantalMunten() + aantalMunten);
    }

    public void steelMunten(int diefId, int slachtofferId, int aantalMunten) throws RemoteException {
        aantalMunten = Math.min(spelers.get(slachtofferId).getAantalMunten(), aantalMunten);
        spelers.get(slachtofferId).setAantalMunten(spelers.get(slachtofferId).getAantalMunten() - aantalMunten);
        spelers.get(diefId).setAantalMunten(spelers.get(diefId).getAantalMunten() + aantalMunten);
    }

    /**
     * wordt aangeroepen als een bedrijven complex geactiveerd wordt
     * @param spelerId de speler die moet ruilen
     */
    public void bedrijvenComplexActivate(int spelerId) throws RemoteException {
        updateModel = new ModelConverter(this);
        clients.get(spelerId).ruilKaart(updateModel);
    }

    /**
     * word door de speler aangeroepen die wilt ruilen
     * @param diefId van de speler die ruilt
     * @param kaartId1 van de kaart van degene die ruilt
     * @param slachtofferId van de speler met wie geruilt wordt
     * @param kaartId2 van de kaart van degene met wie geruilt wordt
     */
    public void bedrijvenComplexActivate(int diefId, int kaartId1, int slachtofferId, int kaartId2) throws RemoteException {
        spelers.get(diefId).neemKaart(winkel.getInfoKaarten()[kaartId1]);
        spelers.get(diefId).geefKaart(winkel.getInfoKaarten()[kaartId2]);
        spelers.get(slachtofferId).neemKaart(winkel.getInfoKaarten()[kaartId2]);
        spelers.get(slachtofferId).geefKaart(winkel.getInfoKaarten()[kaartId1]);
        userPrompted = false;
    }
    /**
     * wordt aangeroepen als een tv-station geactiveerd wordt
     */
    public void tvStationActivatie() throws RemoteException{
        updateModel = new ModelConverter(this);
        clients.get(beurt).steelMunten(updateModel);
    }
    /**
     * wordt aangeroepen door de speler die wilt stelen
     * @param slachtofferId van de speler die bestolen wordt
     */
    public void tvStationActivatie(int slachtofferId) throws RemoteException {
        steelMunten(beurt,slachtofferId,5);
        userPrompted = false;
    }
    /**
     * wordt aangeroepen als een stadion geactiveerd wordt
     */
    public void stadionActivatie() throws RemoteException {
        for (Speler speler : spelers) {
            if (speler.getSpelerId() != beurt) {
                steelMunten(beurt,speler.getSpelerId(),2);
            }
        }
    }

    public int aantalKaart(int spelerId, int kaartId) throws RemoteException {
        return spelers.get(spelerId).aantalKaart(kaartId);
    }

    public int aantalSpelers() throws RemoteException {
        return spelers.size();
    }

    public boolean heeftWinkelcentrum(int spelerId) throws RemoteException {
        return spelers.get(spelerId).heeftWinkelcentrum();
    }

    public void checkKaartenActivatie(int spelerBeurtId) throws RemoteException {
        boolean prompt = false;
        for(Speler speler : spelers){
            if(speler.checkActivatie(spelerBeurtId, aantalOgen)){
                prompt = true;
            }
        }
        userPrompted = prompt;
    }

    //getters en setters
    public ArrayList<ViewInterface> getClients() {
        return clients;
    }

    protected void setClients(ArrayList<ViewInterface> clients) {
        this.clients = clients;
    }

    public String[] getNamen() {
        return namen;
    }

    protected void setNamen(String[] namen) {
        this.namen = namen;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    protected void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public Winkel getWinkel() {
        return winkel;
    }

    protected void setWinkel(Winkel winkel) {
        this.winkel = winkel;
    }

    public int getBeurt() {
        return beurt;
    }

    protected void setBeurt(int beurt) {
        this.beurt = beurt;
    }

    public boolean getGegooid() {
        return gegooid;
    }

    protected void setGegooid(boolean gegooid) {
        this.gegooid = gegooid;
    }

    public boolean isEersteKeerGegooid() {
        return eersteKeerGegooid;
    }

    protected void setEersteKeerGegooid(boolean eersteKeerGegooid) {
        this.eersteKeerGegooid = eersteKeerGegooid;
    }

    public boolean getBeurtAfgerond() {
        return beurtAfgerond;
    }

    protected void setBeurtAfgerond(boolean beurtAfgerond) {
        this.beurtAfgerond = beurtAfgerond;
    }

    public int getAantalOgen() {
        return aantalOgen;
    }

    public void setAantalOgen(int aantalOgen) {
        this.aantalOgen = aantalOgen;
    }

    public ArrayList<Speler> getSpelers() {
        return spelers;
    }

    protected void setSpelers(ArrayList<Speler> spelers) {
        this.spelers = spelers;
    }

    public boolean isUserPrompted() {
        return userPrompted;
    }

    public void setUserPrompted(boolean userPrompted) {
        this.userPrompted = userPrompted;
    }

    public boolean isGeladen() {
        return geladen;
    }

    protected void setGeladen(boolean geladen) {
        this.geladen = geladen;
    }

    public String [] getOpgeslagenNamen() {
        return namen;
    }

    public void setDubbelGegooid(boolean dubbelGegooid) {
        this.dubbelGegooid = dubbelGegooid;
    }

    public boolean isDubbelGegooid() {
        return dubbelGegooid;
    }
}
