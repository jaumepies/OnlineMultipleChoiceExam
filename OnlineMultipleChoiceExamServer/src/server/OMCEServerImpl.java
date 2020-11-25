package server;



import common.Exam;
import common.OMCEClient;
import common.OMCEServer;
import common.Quiz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class OMCEServerImpl extends UnicastRemoteObject implements OMCEServer {

    private ArrayList<OMCEClient> clients = new ArrayList<>();

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

    public Exam createExam(String csvFile){
        ExamGenerator generator = new ExamGenerator(csvFile);

        return generator.generateExam();
    }

    public int getNumStudents(){
        return clients.size();
    }
}
