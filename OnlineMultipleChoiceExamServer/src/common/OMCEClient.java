package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEClient extends Remote {
    void notifyHello() throws RemoteException;
}
