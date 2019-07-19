package SpelObjecten;

import java.io.Serializable;
/**
 * de klasse worp spreekt de dobbelsteen aan om te gaan dobbelen, hij kan 1 of 2 dobbelstenen gooien.
 * @author Bas, Omid, Bashar
 */
public class Worp implements Serializable {

    private int gooiEen = 0;
    private int gooiTwee = 0;

    public void gooiEenDobbelsteen(){
        Dobbelsteen dobbelsteen = new Dobbelsteen();
        dobbelsteen.gooiDobbelsteen();
        gooiEen =  dobbelsteen.getAantalOgen();
        gooiTwee = 0;
    }

    public void gooiTweeDobbelstenen(){
        Dobbelsteen dobbelsteen1 = new Dobbelsteen();
        Dobbelsteen dobbelsteen2 = new Dobbelsteen();
        dobbelsteen1.gooiDobbelsteen();
        dobbelsteen2.gooiDobbelsteen();
        gooiEen = dobbelsteen1.getAantalOgen();
        gooiTwee = dobbelsteen2.getAantalOgen();
    }

    public int aantalOgen(){
        return gooiEen + gooiTwee;
    }

    public boolean dubbelGegooid(){
        return gooiEen == gooiTwee;
    }
}
