package common;

import java.util.ArrayList;

public class Quiz {
    private int id;
    private String question;
    private ArrayList<String> choices;
    private int correctAnswer;
    private Integer selectedChoice;

    public Quiz(int id, String question, ArrayList<String> choices, int correctAnswer, Integer selectedChoice) {
        this.id = id;
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.selectedChoice = selectedChoice;
    }

    public int getCorrectAnswer() { return this.correctAnswer; }

    @Override
    public String toString() {
        String quiz =question + '\n';
        for(int i = 0;i<choices.size();i++){
            quiz += (i+1) + ") " + choices.get(i) + '\n';
        }
        return  quiz;
    }

    public Integer getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(Integer selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public int getId() {
        return id;
    }
}