package client;

import common.OMCEClient;
import common.OMCEServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args){
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            OMCEClient client = new OMCEClientImpl();
            OMCEServer stub = (OMCEServer) registry.lookup("Hello");
            if (stub.isStartedExam()) {
                System.out.println("The exam has already started.");
                System.exit(0);
            }
            String id = client.getId();
            stub.registerStudent(client, id);
            synchronized(client){
                client.wait();//espera la quiz
                stub.sendAnswer(id, client.getAnswer());
                client.wait();
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("The exam session has not started yet. Try to reconnect in few minutes.");
            System.exit(0);
        }
    }
}
