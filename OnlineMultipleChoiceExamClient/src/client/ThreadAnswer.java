package client;

import common.OMCEClient;
import common.OMCEServer;

import java.rmi.RemoteException;

/**
 * This class has been implemented to obtain the answer
 * passing it through stdin and then send it to the server
 */
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
            System.out.println("Exam session is not reachable");
            System.exit(0);
        }

    }

}