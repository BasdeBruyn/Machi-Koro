package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SpelModelInterface extends Remote {
    int registerClient(ViewInterface client, String naam) throws RemoteException;
    boolean kiesSpeler(ViewInterface client, int spelerId) throws RemoteException;
    boolean koopKaart(int spelerId, int kaartId) throws RemoteException;
    boolean koopWinkelcentrum(int spelerId) throws RemoteException;
    boolean koopPretpark(int spelerId) throws RemoteException;
    boolean koopRadioStation(int spelerId) throws RemoteException;
    boolean koopTreinstation(int spelerId) throws RemoteException;
    void gooiDobbelsteen(boolean tweeStenen) throws RemoteException;
    void beëindigGooien()throws RemoteException;
    void rondBeurtAf() throws RemoteException;
    void geefMunten(int spelerId, int aantalMunten) throws RemoteException;
    void steelMunten(int diefID, int slachtofferID, int aantalMunten) throws RemoteException;
    void bedrijvenComplexActivate(int spelerId) throws RemoteException;
    void bedrijvenComplexActivate(int diefId, int kaartId1, int slachtofferId, int kaartId2) throws RemoteException;
    void tvStationActivatie(int slachtofferId) throws RemoteException;
    void tvStationActivatie() throws RemoteException;
    void stadionActivatie() throws RemoteException;
    int aantalKaart(int spelerID, int kaartID) throws RemoteException;
    int aantalSpelers() throws RemoteException;
    boolean heeftWinkelcentrum(int spelerID) throws RemoteException;
    String [] getOpgeslagenNamen() throws RemoteException;
}
