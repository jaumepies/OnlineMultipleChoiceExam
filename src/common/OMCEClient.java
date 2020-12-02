package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEClient extends Remote {
    void notifyStartExam() throws RemoteException;

    void notifyRegisterStudent() throws RemoteException;

    boolean isRegistered() throws RemoteException;

    String getId() throws RemoteException;

    void notifyQuiz(String quiz) throws RemoteException;

    String getAnswer() throws RemoteException;

    void notifyResult(String result) throws RemoteException;

    void notifyRegisteredStudent() throws RemoteException;

    boolean isCorrectId(String id) throws RemoteException;
}
