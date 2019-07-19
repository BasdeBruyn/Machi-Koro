package SpelObjecten;

import java.io.Serializable;
/**
 * de dobbelsteen gemaakt met een math.random x 6
 * @author Omid
 */
public class Dobbelsteen implements Serializable {

    private int aantalOgen;

    public void gooiDobbelsteen(){
        aantalOgen= (int)(Math.random() * 6 + 1);
    }

    public int getAantalOgen() {
        return aantalOgen;
    }
}
