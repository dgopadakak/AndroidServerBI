package Firm;

import java.util.ArrayList;

public class Round
{
    String name;
    ArrayList<Task> listOfTasks;

    public Round(String name, ArrayList<Task> listOfTasks)
    {
        this.name = name;
        this.listOfTasks = listOfTasks;
    }
}
