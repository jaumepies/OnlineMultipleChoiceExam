package server;

import common.Exam;
import common.OMCEServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
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

            // Read the route of .csv file
            String csvFile = obj.getFilePath();
            // Create the exam
            Exam exam = obj.createExam(csvFile);
            System.out.println("The exam is uploaded correctly");

            registry.bind("Hello",  obj);

            while(true) {
                synchronized (obj) {
                    ThreadStartExam thread = new ThreadStartExam(obj);
                    thread.start();
                    while (!thread.isStartedExam()){
                        System.out.println("Students registered " + obj.getNumStudents());
                        obj.wait();
                    }

                    obj.notifyStartExam();
                    obj.generateStudentExams(exam);
                    while(!thread.isFinishedExam()) {
                        //TODO: finalitzar la sessió quan tots els alumnes hagin acabat l'examen
                        obj.sendQuizzes();
                        obj.wait();
                    }
                    //obj.createResults();
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
