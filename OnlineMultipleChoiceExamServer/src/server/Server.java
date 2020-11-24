package server;

import common.Exam;
import common.OMCEServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            //Crear el examen
            //lectura dle fitxer csv
            System.out.println("Please upload file .csv exam.");
            String csvFile = "./Exams/exam.csv";
            Exam examen = obj.createExam(csvFile);
            //Registry registry = LocateRegistry.getRegistry();
            registry.bind("Hello",  obj);
            while(true) {
                Thread.sleep(5000);
                System.out.println("Server will notify all registered clients");
                obj.notify_clients();
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
