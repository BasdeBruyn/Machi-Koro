package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller maakt het mogelijk om via RMI het bezienswaardigheid het Treinstation te kopen.
 * @author Bas, Bashar, Peter, Dennis
 */
public class KoopTreinstationController extends ClientController {

    public static boolean koopTreinstation(int spelerId){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            return spelControllerInterface.koopTreinstation(spelerId);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
