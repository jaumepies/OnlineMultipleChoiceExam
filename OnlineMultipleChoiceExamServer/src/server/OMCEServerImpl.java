package server;



import common.Exam;
import common.OMCEClient;
import common.OMCEServer;
import common.Quiz;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OMCEServerImpl extends UnicastRemoteObject implements OMCEServer {

    private HashMap<String, OMCEClient> students = new HashMap<>();
    private HashMap<String, Exam> studentExams = new HashMap<>();
    private List<OMCEClient> error_students = new ArrayList<>();
    boolean isExamStarted = false;
    private String studentToNotify = "ALL";

    public OMCEServerImpl() throws RemoteException{}

    public void registerStudent(OMCEClient student, String universityID) {
        if(students.containsKey(universityID)){
            studentAlreadyRegistered(student);
        }else {
            registerNewStudent(student, universityID);
        }
    }

    private void registerNewStudent(OMCEClient student, String universityID) {
        synchronized (this) {
            System.out.println("Registering student " + universityID);
            students.put(universityID, student);
            try {
                student.notifyRegisterStudent();
                this.notify();
            } catch (RemoteException e) {
                System.out.println(universityID + " is not reachable to registering.");
                students.remove(universityID);
            }
        }
    }

    private void studentAlreadyRegistered(OMCEClient student) {
        try {
            student.notifyRegisteredStudent();
        }catch (RemoteException e) {
        }
    }

    public void notifyStartExam(){
        isExamStarted = true;
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            try{
                s.getValue().notifyStartExam();
            }catch(RemoteException e){
                System.out.println(s.getKey() + " is not reachable to starting the exam.");
                error_students.add(s.getValue());
            }
        }
        for(OMCEClient s: error_students){
            this.students.remove(s);
        }
    }

    public void generateStudentExams(String csvPath){
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            studentExams.put(s.getKey(), ExamGenerator.generateExam(csvPath));
        }
    }

    public String getFilePath(String message){
        Scanner keyboard = new Scanner(System.in);
        System.out.println(message);
        return keyboard.nextLine();
        //String line = keyboard.nextLine();
        //return "C:/Users/Ricard/Downloads/exam.csv";
        //return "C:/Users/jaume/IdeaProjects/OnlineMultipleChoiceExam/OnlineMultipleChoiceExamServer/Exams/exam.csv";
    }

    public boolean csvPathIsFile(String csvPath) {
        File file = new File(csvPath);
        return file.isFile();
    }

    public boolean csvPathIsDirectory(String csvPath) {
        File file = new File(csvPath);
        return file.isDirectory();
    }

    public int getNumStudents(){
        return students.size();
    }

    public boolean isExamStarted(){
        return isExamStarted;
    }

    public boolean isStudentExamFinished(String studentId){
        Exam exam = studentExams.get(studentId);
        return exam.isFinished;
    }

    public void send() {
        error_students = new ArrayList<>();
        if (studentToNotify.equals("ALL"))
            sendQuizzes();
        else
            sendQuiz();
        for(OMCEClient s: error_students){
            this.students.remove(s);
        }
    }

    private void sendQuizzes(){
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            sendQuizTo(s.getKey(), s.getValue());
        }
    }

    private void sendQuiz() {
        sendQuizTo(studentToNotify, students.get(studentToNotify));
    }

    private void sendQuizTo(String id, OMCEClient student) {
        try{
            Exam exam = studentExams.get(id);
            Quiz nextQuiz = exam.getNextQuiz();
            if(nextQuiz!= null){
                students.get(id).notifyQuiz(nextQuiz.toString());
            }else{
                exam.isFinished = true;
                String result = exam.getResult();
                student.notifyResult(result);
            }
        }catch(RemoteException e){
            System.out.println(id + " is not reachable to send quiz.");
            error_students.add(student);
        }
    }

    public void sendAnswer(String studentId, String answerNum) {
        synchronized (this) {
            //System.out.println(answerNum);
            Exam exam = studentExams.get(studentId);
            Quiz quiz = exam.getNextQuiz();
            quiz.selectedChoice = Integer.parseInt(answerNum);
            exam.setQuiz(quiz);
            studentExams.put(studentId, exam);
            studentToNotify = studentId;
            this.notify();
        }
    }

    public void sendResults(){
        error_students = new ArrayList<>();
        for (HashMap.Entry<String, OMCEClient> s : students.entrySet()) {
            try{
                Exam exam = studentExams.get(s.getKey());
                exam.isFinished = true;
                String result = exam.getResult();
                s.getValue().notifyResult(result);
            }catch(RemoteException e){
                System.out.println(s.getKey() + " is not reachable to send result.");
                error_students.add(s.getValue());
            }
        }
        for(OMCEClient s: error_students){
            this.students.remove(s);
        }
    }

    public void createResultsFile(String csvPath){
        ArrayList<String[]> studentGrades = new ArrayList<>();
        studentGrades.add(new String[]{"UniversityID","Grade"});
        for (HashMap.Entry<String, Exam> s : studentExams.entrySet()) {
            studentGrades.add(new String[]{s.getKey(),s.getValue().result});
        }
        csvPath += "/results.csv";
        File csvOutputFile = new File(csvPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            studentGrades.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(";") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
