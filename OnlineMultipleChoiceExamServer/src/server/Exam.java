package server;


import java.util.ArrayList;

public class Exam {

    private Long examId;
    private ArrayList<Quiz> quizzes;
    private boolean finished = false;
    private String result = "0.0";


    private String description;
    private String date;
    private String time;
    private String location;



    public Exam(String description, String date, String time, String location) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public Exam() {

    }

    public Exam(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
        this.finished = false;
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
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public ArrayList<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(ArrayList<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}