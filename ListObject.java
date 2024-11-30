import java.sql.Time;

public class ListObject {

    String m_title;
    String m_description;
    int m_priority;
    Time m_start_time;
    Time m_end_time;

    public ListObject(String title, String description,
                      int priority, Time start_time, Time end_time) {
        m_title = title;
        m_description = description;
        m_priority = priority;
        m_start_time = start_time;
        m_end_time = end_time;
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

    public Time getStartTime() {
        return m_start_time;
    }

    public Time getEndTime() {
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

    public void setStartTime(Time startTime) {
        m_start_time = startTime;
    }

    public void setEndTime(Time endTime) {
        m_end_time = endTime;
    }
}