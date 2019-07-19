package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Deze controller zorgt er voor dat via RMI de dobbelsteen gegooid kan worden.
 * @author Bas, Bashar, Peter, Dennis
 */
public class GooiDobbelsteenController extends ClientController {

    public static void gooiDobbelsteen(boolean tweeStenen){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            spelControllerInterface.gooiDobbelsteen(tweeStenen);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
