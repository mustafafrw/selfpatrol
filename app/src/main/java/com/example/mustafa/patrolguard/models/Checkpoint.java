package com.example.mustafa.patrolguard.models;

import java.util.List;

public class Checkpoint {
    private int id;
    private String checkpoint_name;
    private String area_name;
    private List<Task> tasks;
    private int expanded;

    public Checkpoint() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCheckpoint_name() {
        return checkpoint_name;
    }

    public void setCheckpoint_name(String checkpoint_name) {
        this.checkpoint_name = checkpoint_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> taskList) {
        this.tasks = taskList;
    }

    public int getExpanded() {
        return expanded;
    }

    public void setExpanded(int expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "id=" + id +
                ", checkpoint_name='" + checkpoint_name + '\'' +
                ", area_name='" + area_name + '\'' +
                ", taskList=" + tasks.toString() +
                '}';
    }
}
