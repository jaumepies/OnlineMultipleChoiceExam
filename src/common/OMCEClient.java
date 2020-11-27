package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEClient extends Remote {
    void notifyStartExam() throws RemoteException;

    void notifyRegisterStudent() throws RemoteException;

    String getId() throws RemoteException;
}
