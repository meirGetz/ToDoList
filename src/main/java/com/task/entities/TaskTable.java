package com.task.manager;

import com.task.model.ListObject;
import jakarta.persistence.*;

@Entity
@Table(name = "TaskTable")
public class TaskTable {

    @Id
    @Column(name = "task Id")
    private long id;

    @Embedded
    private ListObject listObject;

    public TaskTable(long id, ListObject listObject) {
        id = this.id;
        this.listObject = listObject;
    }
    public TaskTable() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ListObject getListObject() {
        return listObject;
    }
    public void setListObject(ListObject listObject) {
        this.listObject = listObject;
    }


}
