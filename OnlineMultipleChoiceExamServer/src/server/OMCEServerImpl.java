package server;



import common.Exam;
import common.OMCEClient;
import common.OMCEServer;
import common.Quiz;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class OMCEServerImpl extends UnicastRemoteObject implements OMCEServer {

    private HashMap<String, OMCEClient> students = new HashMap<>();
    private HashMap<String, Exam> studentExams = new HashMap<>();
    boolean isStartedExam = false;

    public OMCEServerImpl() throws RemoteException{}

    public void registerStudent(OMCEClient student, String universityID) {
        synchronized (this) {
            System.out.println("Registering student " + universityID);
            students.put(universityID, student);
            try{
                student.notifyRegisterStudent();
                this.notify();
            }catch (RemoteException e){
                System.out.println("Student is not reachable");
            }

        }
    }

    public void notifyStartExam(){
        isStartedExam = true;
        List<OMCEClient> error_students = new ArrayList<>();
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            try{
                s.getValue().notifyStartExam();
            }catch(RemoteException e){
                System.out.println("Student is not reachable");
                error_students.add(s.getValue());
            }
        }
        for(OMCEClient s: error_students){
            this.students.remove(s);
        }
    }

    public void generateStudentExams(Exam exam){
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            studentExams.put(s.getKey(), exam);
        }
    }

    public String getFilePath(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Please, enter the absolute route of .csv exam file.");
        //return keyboard.nextLine();
        String line = keyboard.nextLine();
        return "C:/Users/Ricard/Downloads/exam.csv";
    }

    public Exam createExam(String csvFile){
        ExamGenerator generator = new ExamGenerator(csvFile);

        return generator.generateExam();
    }

    public int getNumStudents(){
        return students.size();
    }

    public boolean isStartedExam(){
        return isStartedExam;
    }

    public void sendQuizzes(){

    }
}
