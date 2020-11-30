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
    boolean isExamStarted = false;
    boolean isExamFinished = false;
    boolean isStudentExamFinished = false;

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
        isExamStarted = true;
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
        //return "C:/Users/jaume/IdeaProjects/OnlineMultipleChoiceExam/OnlineMultipleChoiceExamServer/Exams/exam.csv";
    }

    public Exam createExam(String csvFile){
        ExamGenerator generator = new ExamGenerator(csvFile);

        return generator.generateExam();
    }

    public int getNumStudents(){
        return students.size();
    }

    public boolean isExamStarted(){
        return isExamStarted;
    }

    public boolean isExamFinished(){ return isExamFinished; }

    public boolean isStudentExamFinished(String studentId){
        Exam exam = studentExams.get(studentId);
        return exam.isFinished;
    }

    public void sendQuizzes(){
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            try{
                Exam exam = studentExams.get(s.getKey());
                Quiz nextQuiz = exam.getNextQuiz();
                if(nextQuiz!= null){
                    s.getValue().notifyQuiz(nextQuiz.toString());
                }else{
                    exam.isFinished = true;
                    //String result = exam.getResult(); TODO
                    String result = "10";
                    s.getValue().notifyResult(result);
                }
            }catch(RemoteException e){
                System.out.println("Student is not reachable");
            }
        }
    }

    public void sendAnswer(String studentId, String answerNum) {
        synchronized (this) {
            System.out.println(answerNum);
            Exam exam = studentExams.get(studentId);
            Quiz quiz = exam.getNextQuiz();
            quiz.SelectedChoice = Integer.parseInt(answerNum);
            exam.setQuiz(quiz);
            studentExams.put(studentId, exam);
            this.notify();

            try{
                Quiz nextQuiz = exam.getNextQuiz();
                if(nextQuiz!= null){
                    students.get(studentId).notifyQuiz(nextQuiz.toString());

                }else{
                    exam.isFinished = true;
                    //String result = exam.getResult(); TODO
                    String result = "10";
                    students.get(studentId).notifyResult(result);
                }
            }catch(RemoteException e){
                System.out.println("Student is not reachable");
            }


        }
    }

}
