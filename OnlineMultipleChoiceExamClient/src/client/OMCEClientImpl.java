package client;

import common.OMCEClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class OMCEClientImpl extends UnicastRemoteObject implements OMCEClient {

    private boolean isRegistered = false;
    private String answer = "";
    private Scanner scanner = new Scanner(System.in);
    private boolean examFinished = false;

    public OMCEClientImpl() throws RemoteException {}

    public void notifyStartExam() {
        System.out.println("The exam is going to start");
    }

    public void notifyRegisterStudent() {
        System.out.println("Student registered, waiting for the exam");
        isRegistered = true;
    }

    public void notifyRegisteredStudent() {
        System.out.println("This student is already registered");
        synchronized (this){
            this.notify();
        }
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getStudentId(){
        System.out.println("Enter your university ID");
        return scanner.nextLine();
    }

    public boolean isCorrectId(String id) {
        return id.matches("[A-Za-z0-9]+");
    }

    public void notifyQuiz(String quiz){
        synchronized (this) {
            System.out.println(quiz);
            this.notify();
        }
    }

    public String inputAnswer(){
        System.out.println("Enter your answer number or \"leave\" to leave the exam");
        return scanner.nextLine();
    }

    public void leaveSession() {
        String leave_key = "leave";
        while (!getAnswer().equals(leave_key)){
            System.out.println("Enter \"leave\" to leave the exam");
            setAnswer(scanner.nextLine());
        }
        System.exit(0);
    }

    public void notifyResult(String result){
        synchronized (this) {
            System.out.println("The exam session has finished.");
            System.out.println("Your result is: "+ result);
            this.notify();
        }
    }

    public void setAnswer(String answer) {
        this.answer = answer;

    }
    public String getAnswer() {
        return this.answer;
    }

    public boolean isExamFinished() {
        return examFinished;
    }

    public void setExamFinished(boolean examFinished) {
        this.examFinished = examFinished;
    }
}
