package client;

import common.Exam;
import common.OMCEClient;
import common.OMCEServer;

import java.rmi.RemoteException;

public class ThreadAnswer extends Thread {

    OMCEServer server;
    OMCEClient client;
    String studentId;

    public ThreadAnswer(OMCEServer server, OMCEClient client, String studentId) {
        this.server = server;
        this.client = client;
        this.studentId = studentId;
    }

    public void run() {
        try{
            String answer = client.getAnswer();
            server.sendAnswer(studentId, answer);
        }catch(RemoteException e){
            System.out.println("Student is not reachable");
        }

    }

}