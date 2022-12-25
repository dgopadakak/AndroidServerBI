package Firm;

import java.util.ArrayList;
import java.util.Objects;

public class RoundOperator
{
    private int id;
    private ArrayList<Round> rounds = new ArrayList<>();

    public void addTask(String roundName, Task task)
    {
        boolean isNewGroupNeeded = true;
        for (Round round : rounds)
        {
            if (Objects.equals(round.name, roundName))
            {
                isNewGroupNeeded = false;
                round.listOfTasks.add(task);
                break;
            }
        }
        if (isNewGroupNeeded)
        {
            ArrayList<Task> tempArrayList = new ArrayList<>();
            tempArrayList.add(task);
            rounds.add(new Round(roundName, tempArrayList));
        }
    }

    public void delTask(int groupId, int examId)
    {
        rounds.get(groupId).listOfTasks.remove(examId);
    }

    public void editTask(int groupId, int examId, Task newTask)
    {
        rounds.get(groupId).listOfTasks.set(examId, newTask);
    }

    public ArrayList<Round> getRounds()
    {
        return rounds;
    }

    public void setRounds(ArrayList<Round> rounds)
    {
        this.rounds = rounds;
    }
}
