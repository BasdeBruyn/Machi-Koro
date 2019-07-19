package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Dit is de controller van het tv-station die het tv-station kan activeren en het ruilproces in gang zetten.
 * @author Bas, Bashar, Peter, Dennis
 */
public class SteelMuntenController extends ClientController {
    public static void steelMunten(int slachtofferId){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            spelControllerInterface.tvStationActivatie(slachtofferId);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
