package com.task.DTO;


import java.time.LocalDateTime;

public class ListObjectRequest {
    private String title;
    private String description;
    private int priority;
    private String status = "Pending";
    private LocalDateTime startTime; // נשתמש במחרוזת לפשטות
    private int days;
    private int hours;
    private int minutes;
    private long user_id;

    public ListObjectRequest(String title, String description, int priority, String status, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.startTime = startTime;

    }
    public long getUserId() {
        return user_id;
    }
    public void setUserId(long user_id) {
        this.user_id = user_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
