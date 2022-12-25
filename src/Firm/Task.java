package Firm;

public class Task
{
    String nameOfTask;
    String hint;
    int num;
    String numOfParticipants;
    String timeForSolve;
    int maxScore;
    int isComplicated;
    String condition;

    public Task(String nameOfTask, String hint, int num, String numOfParticipants, String timeForSolve, int maxScore, int isComplicated, String condition)
    {
        this.nameOfTask = nameOfTask;
        this.hint = hint;
        this.num = num;
        this.numOfParticipants = numOfParticipants;
        this.timeForSolve = timeForSolve;
        this.maxScore = maxScore;
        this.isComplicated = isComplicated;
        this.condition = condition;
    }
}
