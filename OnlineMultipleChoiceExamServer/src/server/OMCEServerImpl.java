package server;



import common.Exam;
import common.OMCEClient;
import common.OMCEServer;
import common.Quiz;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class OMCEServerImpl extends UnicastRemoteObject implements OMCEServer {

    private ArrayList<OMCEClient> students = new ArrayList<>();
    private HashMap<OMCEClient, String> studentIds = new HashMap<>();
    int answers = 0;

    public OMCEServerImpl() throws RemoteException{}

    public void registerStudent(OMCEClient student, String universityID) {
        synchronized (this) {
            System.out.println("Registering student");
            students.add(student);
            studentIds.put(student, universityID);
            answers++;
            this.notify();
        }
    }

    public void notify_clients(){
        for (OMCEClient s:this.students){
            try {
                System.out.println("calling the client");
                s.notifyHello();
            }catch(RemoteException e){
                System.out.println("error in call");
            }
        }
    }

    public void notifyStart(){
        List<OMCEClient> error_students = new ArrayList<>();
        for (OMCEClient s :students) {
            try{
                s.notifyStart();
            }catch(RemoteException e){
                System.out.println("Student is not reachable");
                error_students.add(s);
            }
        }
        for(OMCEClient s: error_students){
            this.students.remove(s);
        }
    }

    public Exam createExam(String csvFile){
        ExamGenerator generator = new ExamGenerator(csvFile);

        return generator.generateExam();
    }

    public int getNumStudents(){
        return students.size();
    }

    public void sendId(OMCEClient student, String universityID) {
        synchronized (this) {
            System.out.println(universityID);
            studentIds.put(student, universityID);
            this.notify();
        }
    }

    public int getAnswers(){ return  answers; }
}
