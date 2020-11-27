package common;

import server.ThreadStartExam;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEServer extends Remote {
    void registerStudent(OMCEClient student, String universityID) throws RemoteException;

    void notifyStartExam() throws RemoteException;

    String getFilePath() throws RemoteException;

    Exam createExam(String csvFile) throws RemoteException;

    int getNumStudents() throws RemoteException;

    boolean isStartedExam() throws RemoteException;

    void generateStudentExams(Exam exam) throws RemoteException;

    void sendQuizzes() throws RemoteException;

    void sendAnswer(String studentId, String answerNum) throws RemoteException;


}
