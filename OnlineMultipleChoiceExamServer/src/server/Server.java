package server;

import common.Exam;
import common.OMCEServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {
    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if(port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    public static void main(String[] args){
        try{
            Registry registry = startRegistry(null);
            OMCEServer obj = new OMCEServerImpl();

            System.out.println("Please, upload the absolute route of .csv exam file.");
            // Read the route of .csv file
            String csvFile = args[0];

            // Create the exam
            Exam exam = obj.createExam(csvFile);

            registry.bind("Hello",  obj);

            while(true) {
                synchronized (obj) {
                    boolean startExam = false;
                    startExam = obj.isStartedExam();
                    while (!startExam){
                        System.out.println("Students registered " + obj.getNumStudents());
                        obj.wait();
                    }
                    System.out.println("Starting exam.");
                    obj.wait();

                    obj.notifyStart();

                    while (obj.getAnswers() < obj.getNumStudents()) {
                        System.out.println("Recieved university ID");
                        obj.wait();
                    }
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
