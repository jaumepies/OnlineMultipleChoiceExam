package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ExamGenerator {
    private static int idQuiz;
    private static String cvsSplitBy = ";";

    /**
     * Reads from the file and for each line of it creates a quiz.
     * Once all the quizzes have been created, it returns an exam with his quizzes
     */
    public static Exam generateExam(String csvFile) {
        idQuiz = 0;
        ArrayList<Quiz> quizzes = new ArrayList<>();
        String[] content = new String[4];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            content = getFirstLine(br.readLine()); // this will read the first line
            String line;
            while ((line = br.readLine()) != null) {
                Quiz quiz = getQuiz(line);
                quizzes.add(quiz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Exam exam = new Exam(quizzes);
        setContent(content, exam);
        return exam;
    }

    private static void setContent(String[] content, Exam exam) {
        exam.setDescription(content[0]);
        exam.setDate(content[1]);
        exam.setTime(content[2]);
        exam.setLocation(content[3]);
    }

    private static String[] getFirstLine(String line) {
        return line.split(cvsSplitBy);
    }

    private static Quiz getQuiz(String line) {
        // Use semicolon as separator
        String[] fragments = line.split(cvsSplitBy);
        String question = fragments[0];
        //It is stored in a ArrayList from the second item to the penultimate
        ArrayList<String> choices = new ArrayList<>(Arrays.asList(fragments).subList(1, fragments.length - 1));
        // Ignores de "." at the end of the line
        String lastFragment = fragments[fragments.length - 1];
        String correctAnswer = lastFragment.substring(0, lastFragment.length() - 1);

        Quiz quiz = new Quiz(idQuiz, question, choices, Integer.parseInt(correctAnswer), null);
        idQuiz += 1;

        return quiz;
    }
}
