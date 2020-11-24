package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEServer extends Remote {
    void register(OMCEClient client) throws RemoteException;

    void notify_clients() throws RemoteException;

    Exam createExam(String csvFile);
}
