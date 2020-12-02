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
            OMCEServer server = new OMCEServerImpl();

            // Read the route of .csv file

            String csvPath;
            do{
                csvPath = server.getFilePath("Please, enter the absolute route of .csv exam file.");
            }while(!server.csvPathIsFile(csvPath));

            System.out.println("The exam is uploaded correctly");

            registry.bind("Hello",  server);

            while(true) {
                synchronized (server) {
                    String start_word = "start";
                    Interrupt interruptStart = new Interrupt(server, start_word);
                    //The tread starts reading for the key
                    interruptStart.start();
                    while(!interruptStart.isInterrupted()){
                        System.out.println("Students registered " + server.getNumStudents());
                        System.out.println("Write \""+ start_word +"\" to start the exam");
                        server.wait();
                        //server.wait can be notified from the interrupt key, or the remote object implemented
                    }

                    server.generateStudentExams(csvPath);
                    server.notifyStartExam();
                    System.out.println("Starting exam.");

                    String finish_word = "finish";
                    Interrupt interruptFinish = new Interrupt(server, finish_word);
                    interruptFinish.start();
                    System.out.println("Write \""+ finish_word +"\" to finish the exam");
                    while(!interruptFinish.isInterrupted()) { //sessio de examen TOTS els examens
                        //TODO: finalitzar la sessió quan tots els alumnes hagin acabat l'examen
                        server.send();
                        server.wait();
                    }
                    System.out.println("Exam session finished.");
                    server.sendResults();
                    do{
                        csvPath = server.getFilePath("Please, enter the absolute route of .csv results file.");
                    }while(!server.csvPathIsDirectory(csvPath));

                    server.createResultsFile(csvPath);//document
                    System.exit(0);
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
