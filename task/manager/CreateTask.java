package task.manager;
import task.model.ListObject;
import java.time.LocalDateTime;


import java.sql.Time;
import java.util.Scanner;

public class CreateTask {
private Scanner scanner = new Scanner(System.in);

    public ListObject createTask() {
        String title = createTitle();
        String description = createDescription();
        int priority = prioritize();
        LocalDateTime  startTime = LocalDateTime.now();
        LocalDateTime endTime = endTask();
        return new ListObject(title,description,priority,startTime,endTime);
    }

    private String createTitle(){
        System.out.println("Enter title: ");
        String title = scanner.nextLine();
        return title;
    }

    private String createDescription(){
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        return description;
    }
    private int prioritize() {
        int priority = getValidatedInt("Enter priority between 1 to 5: ", 1, 5);
        return priority;
    }

    private LocalDateTime endTask(){
        LocalDateTime startTime = LocalDateTime.now();
        int day = getValidatedInt("\nEnter for how many days: ",1,365);
        int hours = getValidatedInt("\nEnter for how many hours: ",0,24);
        int minutes =getValidatedInt("\nEnter for how many minutes: ",0,59);
        return startTime.plusDays(day).plusHours(hours).plusMinutes(minutes);
    }

    private int getValidatedInt(String prompt, int min, int max) {
        System.out.println(prompt);
        int value = scanner.nextInt();
        while (value < min || value > max) {
            System.out.println("Value out of range. Try again.");
            value = scanner.nextInt();
        }
        return value;
    }
}
