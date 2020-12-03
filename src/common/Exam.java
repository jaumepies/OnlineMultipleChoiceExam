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

    public Quiz getNextQuiz(){
        for(Quiz q: quizzes){
            if(q.selectedChoice == null){
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

        // For each quiz check if the correct answer matches with selected choice
        for(Quiz q: quizzes){
            if(q.selectedChoice != null && q.selectedChoice == q.getCorrectAnswer()){
                correct++;
            }
        }

        result = String.valueOf(((float)correct / quizzes.size()) * 10);

        return result;
    }
}
