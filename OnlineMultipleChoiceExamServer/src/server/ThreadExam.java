package server;

import common.OMCEServer;

import java.util.Scanner;

public class ThreadExam extends Thread{
    public boolean isStarted = false;
    public OMCEServer obj;

    public ThreadExam(OMCEServer obj){
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

    public boolean isExamSessionStarted() {
        return isStarted;
    }

    public boolean isExamSessionFinished() {
        return false;
    }
}
