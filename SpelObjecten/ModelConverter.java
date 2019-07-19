package SpelObjecten;

import Server.SpelModel;
/**
 * Zet variabelen geleijk zodat RMI werkt.
 * @author Bas, Maarten
 */
public class ModelConverter extends SpelModel {
    public ModelConverter(SpelModel model){
        setClients(model.getClients());
        setNamen(model.getNamen());
        setPlayerCount(model.getPlayerCount());
        setWinkel(model.getWinkel());
        setSpelers(model.getSpelers());
        setGegooid(model.getGegooid());
        setEersteKeerGegooid(model.isEersteKeerGegooid());
        setDubbelGegooid(model.isDubbelGegooid());
        setBeurtAfgerond(model.getBeurtAfgerond());
        setAantalOgen(model.getAantalOgen());
        setBeurt(model.getBeurt());
        setGeladen(model.isGeladen());
    }
}
