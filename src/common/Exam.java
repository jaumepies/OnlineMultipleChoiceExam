package common;


import java.util.ArrayList;

public class Exam {
    private ArrayList<Quiz> quizzes;

    public Exam(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public String getNextQuiz(){
        for(Quiz q: quizzes){
            if(q.SelectedChoice == null){
                return q.toString();
            }
        }
        return null;
    }
}
