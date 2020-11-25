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
            stub.registerStudent(client);
            System.out.println("Student registered, waiting for the exam");
        } catch (Exception e) {
            System.err.println("Student exception: " + e.toString()); e.printStackTrace();
        }
    }
}
