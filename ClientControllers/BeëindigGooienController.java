package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller zorgt er voor dat een speler kan stoppen met gooien als hij nog een keer zou mogen gooien
 * @author Bas, Bashar, Peter, Dennis
 */
public class BeëindigGooienController extends ClientController {

    public static void beëindigGooien(){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            spelControllerInterface.beëindigGooien();
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
