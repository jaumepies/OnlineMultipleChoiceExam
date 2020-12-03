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
            OMCEServer server = (OMCEServer) registry.lookup("Hello");

            // Check to see if the exam has already started
            if (server.isExamStarted()) {
                System.out.println("The exam has already started.");
                System.exit(0);
            }

            String studentId;
            // As long as the student has not registered
            do{
                // As long as the studentId is not alphanumeric
                do{
                    studentId = client.getStudentId();
                }while(!client.isCorrectId(studentId));

                // Registers the student
                server.registerStudent(client, studentId);
            }while(!client.isRegistered());

            synchronized (client){
                // Wait until the start of the exam
                client.wait();

                // While the exam session has not finished
                while(!server.isStudentExamFinished(studentId)){
                    // Thread to get the answer from stdin and send it to the server
                    ThreadAnswer thread = new ThreadAnswer(server, client, studentId);
                    thread.start();

                    // Waiting for the next quiz or result
                    client.wait();
                }
                System.out.println("The exam session has finished.");
            }
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Exam session is not reachable. Try to reconnect in few minutes.");
            System.exit(0);
        }
    }
}
