package com.task.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_table")
public class TaskTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<ListObject> listObjects;


    public TaskTable() {
    }

    public TaskTable( List<ListObject> listObjects) {
        this.listObjects = listObjects;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ListObject> getListObjects() {
        if (listObjects == null) {
            listObjects = new ArrayList<>();
        }
        return listObjects;
    }


    public void setListObjects(List<ListObject> listObjects) {
        this.listObjects = listObjects;
    }

    public void setStatus(String status) {
        listObjects.get(0).setStatus(status);
    }
}
