import java.util.Scanner;

import task.model.ListObject;
import task.manager.CreateTask;

public class Main {
    public static void main(String[] args) {
        CreateTask task = new CreateTask();
        //only for test
        ListObject new_task = task.createTask();
        System.out.println(new_task.getTitle());
        System.out.println(new_task.getDescription());
        System.out.println(new_task.getStartTime());
        System.out.println(new_task.getEndTime());
    }
}
