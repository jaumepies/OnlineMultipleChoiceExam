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

    private ArrayList<OMCEClient> clients = new ArrayList<>();
    private HashMap<OMCEClient, String> studentIds = new HashMap<>();
    int answers = 0;

    public OMCEServerImpl() throws RemoteException{}

    public void registerStudent(OMCEClient client) {
        synchronized (this) {
            System.out.println("Registering client");
            clients.add(client);
            this.notify();
        }
    }

    public void notify_clients(){
        for (OMCEClient c:this.clients){
            try {
                System.out.println("calling the client");
                c.notifyHello();
            }catch(RemoteException e){
                System.out.println("error in call");
            }
        }
    }

    public void notifyStart(){
        List<OMCEClient> error_students = new ArrayList<>();
        for (OMCEClient c :clients) {
            try{
                c.notifyStart();
            }catch(RemoteException e){
                System.out.println("Student is not reachable");
                error_students.add(c);
            }
        }
        for(OMCEClient c: error_students){
            this.clients.remove(c);
        }
    }

    public Exam createExam(String csvFile){
        ExamGenerator generator = new ExamGenerator(csvFile);

        return generator.generateExam();
    }

    public int getNumStudents(){
        return clients.size();
    }

    public void sendId(OMCEClient student, String universityID) {
        synchronized (this) {
            System.out.println(universityID);
            studentIds.put(student, universityID);
            answers++;
            this.notify();
        }
    }

    public int getAnswers(){ return  answers; }

    public boolean isStartedExam() {

        new Thread(){
            boolean isStarted = false;
            @Override
            public void run() {
                super.run();
                Scanner keyboard = new Scanner(System.in);
                String line = keyboard.nextLine();
                isStarted = true;

                String responseLine;
                while(!isInterrupted())
                    while ((responseLine = keyboard.nextLine()) != null) {
                        System.out.println(responseLine);
                        if (responseLine.contains("Bye")) break;
                    }
            }
        }
        return isStarted;
    }
}
