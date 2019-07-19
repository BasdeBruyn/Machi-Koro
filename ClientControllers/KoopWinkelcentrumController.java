package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller maakt het mogelijk om via RMI het bezienswaardigheid het Winkelcentrum te kopen.
 * @author Bas, Bashar, Peter, Dennis
 */
public class KoopWinkelcentrumController extends ClientController {

    public static boolean koopWinkelcentrum(int spelerId){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            return spelControllerInterface.koopWinkelcentrum(spelerId);
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
