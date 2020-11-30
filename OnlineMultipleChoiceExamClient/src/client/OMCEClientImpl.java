package client;

import common.OMCEClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class OMCEClientImpl extends UnicastRemoteObject implements OMCEClient {

    public OMCEClientImpl() throws RemoteException {}

    public void notifyStartExam() {
        System.out.println("The exam is going to start");
        synchronized (this) {
            this.notify();
        }
    }

    public void notifyRegisterStudent() {
        System.out.println("Student registered, waiting for the exam");
    }

    public String getId(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter your university ID");
        return keyboard.nextLine();
    }

    public void notifyQuiz(String quiz){
        System.out.println(quiz);
        synchronized (this) {
            this.notify();
        }
    }

    public String getAnswer(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter your answer number:");
        return keyboard.nextLine();
    }

    public void notifyResult(String result){
        System.out.println("The result is: "+ result);
    }
}
