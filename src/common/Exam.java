package common;


import java.util.ArrayList;

public class Exam {
    private int id;
    private ArrayList<Quiz> quizzes;

    public Exam(int id, ArrayList<Quiz> quizzes) {
        this.id = id;
        this.quizzes = quizzes;
    }
}
