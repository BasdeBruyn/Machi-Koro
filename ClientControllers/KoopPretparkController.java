package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller maakt het mogelijk om via RMI het bezienswaardigheid het Pretpark te kopen.
 * @author Bas, Bashar, Peter, Dennis
 */
public class KoopPretparkController extends ClientController {

    public static boolean koopPretpark(int spelerId){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            return spelControllerInterface.koopPretpark(spelerId);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
}
