package com.task.model;

import java.time.LocalDateTime;

public class ListObject {

    String m_title;
    String m_description;
    int m_priority;
    LocalDateTime m_start_time;
    LocalDateTime m_end_time;
    float m_id;

    public ListObject(String title, String description,
                      int priority, LocalDateTime start_time, LocalDateTime end_time, float id) {
        m_title = title;
        m_description = description;
        m_priority = priority;
        m_start_time = start_time;
        m_end_time = end_time;
        m_id = id;
    }

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

    public float getId() {return m_id;}

}