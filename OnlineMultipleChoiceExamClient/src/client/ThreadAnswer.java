package client;

import common.OMCEClient;
import common.OMCEServer;

import java.rmi.RemoteException;

/**
 * This class has been implemented to obtain the answer
 * passing it through stdin and then send it to the server
 */
public class ThreadAnswer extends Thread {

    private OMCEClient client;
    private String answer;

    public ThreadAnswer(OMCEClient client) {
        this.client = client;
    }

    public void run() {
        try{
            answer = client.inputAnswer();
            client.setAnswer(answer);
            synchronized (this.client) {
                this.client.notify();
            }
        }catch(RemoteException e){
            System.out.println("Exam session is not reachable");
            System.exit(0);
        }

    }


}