package com.task.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Embeddable
public class ListObject {

    @Id
    @Column(name = "id")  // שם העמודה במסד הנתונים
    private Long m_id;  // המפתח הראשי של הטבלה

    @Column(name = "title")
    private String m_title;

    @Column(name = "description")
    private String m_description;

    @Column(name = "priority")
    private int m_priority;

    @Column(name = "start_time")
    private LocalDateTime m_start_time;

    @Column(name = "end_time")
    private LocalDateTime m_end_time;

    // קונסטרוקטור ברירת מחדל
    public ListObject() {
    }

    // קונסטרוקטור עם פרמטרים
    public ListObject(String title, String description,
                      int priority, LocalDateTime start_time, LocalDateTime end_time, Long id) {
        m_title = title;
        m_description = description;
        m_priority = priority;
        m_start_time = start_time;
        m_end_time = end_time;
        m_id = id;
    }

    // גטרים וסטרים
    public String getTitle() {
        return m_title;
    }

    public String getDescription() {
        return m_description;
    }

    public int getPriority() {
        return m_priority;
    }

    public LocalDateTime getStartTime() {
        return m_start_time;
    }

    public LocalDateTime getEndTime() {
        return m_end_time;
    }

    public Long getId() {
        return m_id;
    }

    public void setTitle(String title) {
        m_title = title;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public void setPriority(int priority) {
        m_priority = priority;
    }

    public void setStartTime(LocalDateTime startTime) {
        m_start_time = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        m_end_time = endTime;
    }
}
