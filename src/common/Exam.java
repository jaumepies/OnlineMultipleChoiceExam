package common;


import java.util.ArrayList;

public class Exam {
    private ArrayList<Quiz> quizzes;
    public boolean isFinished;

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
            if(q.SelectedChoice == q.getCorrectAnswer()){
                correct++;
            }
        }

        float result = ((float)correct / quizzes.size()) * 10;

        return String.valueOf(result);
    }
}
