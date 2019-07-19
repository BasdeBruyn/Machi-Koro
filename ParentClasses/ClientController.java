package ParentClasses;

import interfaces.SpelModelInterface;
/**
 * Dit is een Parent controler die zijn eigenschappen doorgeeft aan alle andere controlers met het IP adres van de host.
 * @author Bas, Bashar, Peter, Dennis
 */
public class ClientController {
    protected static String serverAdres;
    protected static SpelModelInterface spelControllerInterface;
    public static void setServerAdres(String adres){
        serverAdres = adres;
    }
}
