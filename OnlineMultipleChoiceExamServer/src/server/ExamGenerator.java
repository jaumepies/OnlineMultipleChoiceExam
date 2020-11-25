package server;

import common.Exam;
import common.Quiz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ExamGenerator {
    private String csvFile;
    private int idQuiz = 1;

    public ExamGenerator(String csvFile) {
        this.csvFile = csvFile;
    }

    public Exam generateExam(){
        String line;
        ArrayList<Quiz> quizzes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {
                Quiz quiz = getQuiz(line);
                quizzes.add(quiz);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Exam(quizzes);
    }

    private Quiz getQuiz(String line) {

        // Use semicolon as separator
        String cvsSplitBy = ";";
        String[] fragments = line.split(cvsSplitBy);
        String question = fragments[0];
        ArrayList<String> choices = new ArrayList<>(Arrays.asList(fragments).subList(1, fragments.length - 1));
        String lastFragment = fragments[fragments.length - 1];
        String correctAnswer = lastFragment.substring(0, lastFragment.length() - 1);

        Quiz quiz = new Quiz( idQuiz, question, choices, Integer.parseInt(correctAnswer), null);
        idQuiz += 1;

        return quiz;
    }
}
