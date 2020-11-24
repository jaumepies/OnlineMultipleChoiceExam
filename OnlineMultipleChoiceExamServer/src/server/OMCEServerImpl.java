package server;

import common.OMCEClient;
import common.OMCEServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class OMCEServerImpl extends UnicastRemoteObject implements OMCEServer {
    public OMCEServerImpl() throws RemoteException{}

    private ArrayList<OMCEClient> clients = new ArrayList<>();

    public void register(OMCEClient client) {
        System.out.println("Registering client");
        this.clients.add(client);
    }

    public void notify_clients(){
        for (OMCEClient c:this.clients){
            try {
                System.out.println("calling the client");
                c.notifyHello();
            }catch(RemoteException e){
                System.out.println("error in call");
            }
        }
    }
}
