package com.task.note.DTO;


import java.time.LocalDateTime;

public class NotesRequest {
    private String title;
    private String description;
    private LocalDateTime sendTime; // נשתמש במחרוזת לפשטות
    private long user_id;
    private long task_id;

    public NotesRequest(String title, String description, LocalDateTime sendTime) {
        this.title = title;
        this.description = description;
        this.sendTime = sendTime;
    }

    public long getTask_id() {
        return task_id;
    }
    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
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

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime startTime) {
        this.sendTime = sendTime;
    }

}
