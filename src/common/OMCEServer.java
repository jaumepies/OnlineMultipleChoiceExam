package common;

import server.ThreadStartExam;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEServer extends Remote {
    void registerStudent(OMCEClient student, String universityID) throws RemoteException;

    void notify_clients() throws RemoteException;

    void notifyStartExam() throws RemoteException;

    String getFilePath() throws RemoteException;

    Exam createExam(String csvFile) throws RemoteException;

    int getNumStudents() throws RemoteException;

    void sendId(OMCEClient student, String universityID) throws RemoteException;

    int getAnswers() throws RemoteException;
}
