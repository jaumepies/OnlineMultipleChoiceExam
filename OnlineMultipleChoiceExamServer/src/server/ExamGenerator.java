package server;

import common.Exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExamGenerator {
    private String csvFile;

    public ExamGenerator(String csvFile) {
        this.csvFile = csvFile;
    }

    /*public Exam generateExam(){
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                System.out.println("Country [code= " + country[4] + " , name=" + country[5] + "]");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Exam();
    }*/
}
