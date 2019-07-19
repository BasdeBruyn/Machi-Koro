package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller maakt het mogelijk om via RMI kaarten te kopen.
 * @author Bas, Bashar, Peter, Dennis
 */
public class KoopKaartController extends ClientController {

    public static boolean koopKaart(int spelerId, int kaartId){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            return spelControllerInterface.koopKaart(spelerId,kaartId);
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
