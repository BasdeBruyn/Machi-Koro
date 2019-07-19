package Server;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
/**
 * de spelController controleerd de fundamentele spel onderdelen zoals: beurt, winnaar en het laden van het spel.
 * @author Bas, Maarten
 */
public class SpelController implements Runnable, Remote {

    private SpelModel model = new SpelModel();
    private boolean winnaar = false;
    private boolean geladen = false;

    public SpelController(){
        try {
            UnicastRemoteObject.exportObject(model,0);
            LocateRegistry.createRegistry(1099);
            Naming.rebind("model",model);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * controleerd of de speler aan de beurt is en zorgt er voor dat er gegooid en gekocht kan worden.
     */
    private void beurt() throws RemoteException{
        while (!winnaar){

            model.modelChanged();

            //Gooien
            while(!model.getGegooid()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Activeer kaarten
            model.checkKaartenActivatie(model.getBeurt());

            while(model.isUserPrompted()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            model.modelChanged();

            //spelers[beurt].koopKaart(kaartId);
            while(!model.getBeurtAfgerond()){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(model.checkWinnaar()){
                break;
            }

            model.modelChanged();

            model.setGegooid(false);
            model.setEersteKeerGegooid(false);
            model.setBeurtAfgerond(false);
            model.setUserPrompted(true);

            model.setBeurt(model.getBeurt() + 1);

            if (model.getBeurt() == model.getSpelers().size()){
                model.setBeurt(0);
            }

            model.slaSpelOp();
        }
    }

    /**
     * start de beurt en als het spel niet geladen is initializeert hij het modet
     */
    @Override
    public void run() {
        if(!geladen)
            model.initSpelModel();
        try {
            beurt();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * laad het spel van: modelsave.dat
     */
    public boolean laadSpel(){
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            File file = new File("modelSave.dat");
            if(file.exists()){
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                model = (SpelModel) ois.readObject();

                fis.close();
                ois.close();
                UnicastRemoteObject.exportObject(model,0);
                Naming.rebind("model",model);
                geladen = true;
                model.setGeladen(true);
                return true;
            }
        } catch (IOException | ClassNotFoundException e) {
        }
        return false;
    }

    public int aantalSpelers(){
        return model.getPlayerCount();
    }
}
