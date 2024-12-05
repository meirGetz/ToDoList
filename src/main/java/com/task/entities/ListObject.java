package com.task.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "list_objects")
public class ListObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskTable task;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @Min(1)
    @Max(5)
    private int m_priority;

    @NotBlank(message = "Pending")
    private String status = "Pending";

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ListObject() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPriority() {
        return m_priority;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        m_priority = priority;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime startTime, int days, int hours, int minutes) {
        this.endTime = startTime.plusDays(days).plusHours(hours).plusMinutes(minutes);
    }

    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public TaskTable getTask() {
        return task;
    }

    public void setTask(TaskTable task) {
        this.task = task;
    }
}
