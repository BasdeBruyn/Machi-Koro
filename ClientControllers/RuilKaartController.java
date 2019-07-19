package ClientControllers;

import ParentClasses.ClientController;
import interfaces.SpelModelInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
/**
 * Dit is de controller van het bedrijvencomplex die het bedrijvencomplex kan activeren en het ruilproces in gang zetten.
 * @author Bas, Bashar, Peter, Dennis
 */
public class RuilKaartController extends ClientController {

    public static void ruilKaart(int diefId, int kaartId1, int slachtofferId, int kaartId2){
        try {
            spelControllerInterface = (SpelModelInterface) Naming.lookup("//" + serverAdres + "/model");
            spelControllerInterface.bedrijvenComplexActivate(diefId, kaartId1, slachtofferId, kaartId2);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
