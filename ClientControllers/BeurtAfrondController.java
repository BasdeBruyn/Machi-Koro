package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller zorgt er voor dat een buert afgerond kan worden en dat het juiste model via de RMI interface aangesproken kan worden.
 * @author Bas, Bashar, Peter, Dennis
 */
public class BeurtAfrondController extends ClientController {

    public static void rondBeurtAf(){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            spelControllerInterface.rondBeurtAf();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
