package at.ac.univie.computersciencerunner;

public class Question {

    private String question;

    private String[] answers = new String[4];

    private int rightAnswerIndex;

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public void setRightAnswerIndex(int rightAnswerIndex) {
        this.rightAnswerIndex = rightAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getRightAnswerIndex() {
        return rightAnswerIndex;
    }
}
