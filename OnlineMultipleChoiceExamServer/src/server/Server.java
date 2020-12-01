package server;

import common.Interrupt;
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

            // Read the route of .csv file
            String csvPath = obj.getFilePath("Please, enter the absolute route of .csv exam file.");
            // Create the exam
            //Exam exam = obj.createExam(csvFile);
            System.out.println("The exam is uploaded correctly");

            registry.bind("Hello",  obj);

            while(true) {
                synchronized (obj) {
                    String start_word = "start";
                    Interrupt interruptStart = new Interrupt(obj, start_word);
                    //The tread starts reading for the key
                    interruptStart.start();
                    while(!interruptStart.isInterrupted()){
                        System.out.println("Students registered " + obj.getNumStudents());
                        System.out.println("Write \""+ start_word +"\" to start the exam");
                        obj.wait();
                        //obj.wait can be notified from the interrupt key, or the remote object implemented
                    }

                    obj.generateStudentExams(csvPath);
                    obj.notifyStartExam();
                    System.out.println("Starting exam.");

                    String finish_word = "finish";
                    Interrupt interruptFinish = new Interrupt(obj, finish_word);
                    interruptFinish.start();
                    System.out.println("Write \""+ finish_word +"\" to finish the exam");
                    while(!interruptFinish.isInterrupted()) { //sessio de examen TOTS els examens
                        //TODO: finalitzar la sessió quan tots els alumnes hagin acabat l'examen
                        obj.send();
                        obj.wait();
                    }
                    System.out.println("Exam session finished.");
                    obj.sendResults();
                    csvPath = obj.getFilePath("Please, enter the absolute route of .csv results file.");
                    obj.createResults(csvPath);//document
                    System.exit(0);
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
