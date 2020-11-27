package server;

import common.OMCEServer;

import java.util.Scanner;

public class ThreadStartExam extends Thread{
    public boolean isStarted = false;
    public OMCEServer obj;

    public ThreadStartExam(OMCEServer obj){
        this.obj = obj;
    }

    public void run() {
        while (!isStarted) {
                Scanner keyboard = new Scanner(System.in);
                String line = keyboard.nextLine();
            synchronized (obj){
                isStarted = true;
                obj.notify();
            }
        }
    }

    public boolean isStartedExam() {
        return isStarted;
    }
}
