package interfaces;

import Server.SpelModel;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends Remote{
	void spelerGejoined(String [] namen) throws RemoteException;
	void modelChanged(SpelModel model) throws RemoteException, FileNotFoundException;
	void ruilKaart(SpelModel model) throws  RemoteException;
	void steelMunten(SpelModel model) throws  RemoteException;
	void showWinnaar(String naam) throws RemoteException;
}
