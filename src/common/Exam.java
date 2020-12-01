package common;


import java.util.ArrayList;

public class Exam {
    private ArrayList<Quiz> quizzes;
    public boolean isFinished;
    public String result = "0";

    public Exam(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
        this.isFinished = false;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public Quiz getNextQuiz(){
        for(Quiz q: quizzes){
            if(q.SelectedChoice == null){
                return q;
            }
        }
        return null;
    }

    public void setQuiz( Quiz quiz){
        quizzes.set(quiz.id, quiz);
    }

    public String getResult() {
        int correct = 0;

        for(Quiz q: quizzes){
            if(q.SelectedChoice != null && q.SelectedChoice == q.getCorrectAnswer()){
                correct++;
            }
        }

        result = String.valueOf(((float)correct / quizzes.size()) * 10);

        return result;
    }
}
