package client;

import common.OMCEClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class OMCEClientImpl extends UnicastRemoteObject implements OMCEClient {

    public OMCEClientImpl() throws RemoteException {}

    public void notifyHello() {
        System.out.println("Student recieved \"hello world\" message from server");
    }

    public void notifyStart() throws RemoteException{
        System.out.println("The exam is going to start");

        synchronized (this) {
            this.notify();
        }
    }

    public String getId(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter your university ID");
        return keyboard.nextLine();
    }
}
