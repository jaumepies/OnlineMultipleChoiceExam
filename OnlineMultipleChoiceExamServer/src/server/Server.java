package server;

import common.OMCEServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {
    //variable to change when start
    public static boolean interrupted = false;

    private static class Interrupt extends Thread {
        String interrupt_key = null;
        Object semaphore = null;

        //semaphore must be the syncronized object
        private Interrupt(Object semaphore, String interrupt_key) {
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run() {
            while (true) {
                //read the key
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                //System.out.println(x);
                if (x.equals(this.interrupt_key)) {
                    //if is the key we expect, change the variable, notify and return(finish thread)
                    synchronized (this.semaphore) {
                        interrupted = true;
                        this.semaphore.notify();
                        return;
                    }
                }
            }
        }
    }

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
            //Exam exam = obj.createExam(csvFile);
            System.out.println("The exam is uploaded correctly");

            registry.bind("Hello",  obj);

            while(true) {
                synchronized (obj) {
                    String start_word = "start";
                    Interrupt interruptStart = new Interrupt(obj, start_word);
                    //The tread starts reading for the key
                    interruptStart.start();
                    while(!interrupted){
                        System.out.println("Students registered " + obj.getNumStudents());
                        System.out.println("Write \""+ start_word +"\" to start the exam");
                        obj.wait();
                        //obj.wait can be notified from the interrupt key, or the remote object implemented
                    }

                    /*ThreadExam thread = new ThreadExam(obj);
                    thread.start();
                    while (!thread.isExamSessionStarted()){
                        System.out.println("Students registered " + obj.getNumStudents());
                        obj.wait();
                    }*/
                    obj.generateStudentExams(csvFile);
                    obj.notifyStartExam();
                    System.out.println("Starting exam.");

                    String finish_word = "finish";
                    Interrupt interruptFinish = new Interrupt(obj, finish_word);
                    interruptFinish.start();
                    interrupted = false;
                    System.out.println("Write \""+ finish_word +"\" to finish the exam");
                    while(!interrupted) { //sessio de examen TOTS els examens
                        //TODO: finalitzar la sessió quan tots els alumnes hagin acabat l'examen
                        obj.send();
                        obj.wait();
                    }
                    System.out.println("Exam session finished.");
                    obj.sendResults();
                    obj.wait();
                    obj.createResults();//document
                    //sortir del bucle
                }
            }
        }catch(Exception e){
            System.err.println("Server exception: " + e.toString()); e.printStackTrace();
        }
    }
}
