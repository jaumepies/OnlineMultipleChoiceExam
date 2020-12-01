package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OMCEServer extends Remote {
    void registerStudent(OMCEClient student, String universityID) throws RemoteException;

    void notifyStartExam() throws RemoteException;

    String getFilePath(String message) throws RemoteException;

    int getNumStudents() throws RemoteException;

    boolean isExamStarted() throws RemoteException;

    boolean isStudentExamFinished(String studentId) throws RemoteException;

    void generateStudentExams(String csvPath) throws RemoteException;

    void sendQuizzes() throws RemoteException;

    void sendAnswer(String studentId, String answerNum) throws RemoteException;

    void sendQuiz() throws RemoteException;

    void send() throws RemoteException;

    void sendQuizTo(String studentId, OMCEClient student) throws RemoteException;

    void createResults(String csvPath) throws RemoteException;

    void sendResults() throws RemoteException;
}
