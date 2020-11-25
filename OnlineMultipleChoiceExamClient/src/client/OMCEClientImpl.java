package client;

import common.OMCEClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OMCEClientImpl extends UnicastRemoteObject implements OMCEClient {
    public OMCEClientImpl() throws RemoteException {}

    public void notifyHello() {
        System.out.println("Student recieved \"hello world\" message from server");
    }
}
