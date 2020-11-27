package common;

import java.util.ArrayList;

public class Quiz {
    private int id;
    private String question;
    private ArrayList<String> Choices;
    private int CorrectAnswer;
    public Integer SelectedChoice;

    public Quiz(int id, String question, ArrayList<String> choices, int correctAnswer, Integer selectedChoice) {
        this.id = id;
        this.question = question;
        Choices = choices;
        CorrectAnswer = correctAnswer;
        SelectedChoice = selectedChoice;
    }

    @Override
    public String toString() {
        return  question + '\'' +
                ", Choices=" + Choices +
                '}';
    }


}