package common;


import java.util.ArrayList;

public class Exam {
    private ArrayList<Quiz> quizzes;
    private boolean isFinished;
    private String result = "0.0";

    public Exam(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
        this.isFinished = false;
    }

    public Quiz getNextQuiz() {
        for (Quiz q : quizzes) {
            if (q.getSelectedChoice() == null) {
                return q;
            }
        }
        return null;
    }

    public void setQuiz(Quiz quiz) {
        quizzes.set(quiz.getId(), quiz);
    }

    public String calculateResult() {
        int correct = 0;

        // For each quiz check if the correct answer matches with selected choice
        for (Quiz q : quizzes) {
            if (q.getSelectedChoice() != null && q.getSelectedChoice() == q.getCorrectAnswer()) {
                correct++;
            }
        }

        result = String.valueOf(((float) correct / quizzes.size()) * 10.0);

        return result;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getResult() {
        return result;
    }
}
