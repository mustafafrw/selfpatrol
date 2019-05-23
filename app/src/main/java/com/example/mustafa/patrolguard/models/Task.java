package com.example.mustafa.patrolguard.models;

public class Task {
    private int id_task;
    private String task_name;
    private int status;
    private String completion_time;

    public Task() {
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompletion_time() {
        return completion_time;
    }

    public void setCompletion_time(String completion_time) {
        this.completion_time = completion_time;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id_task=" + id_task +
                ", task_name='" + task_name + '\'' +
                ", status=" + status +
                ", completion_time='" + completion_time + '\'' +
                '}';
    }
}
