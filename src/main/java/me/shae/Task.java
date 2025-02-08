package me.shae;

import java.io.Serializable;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int nextId = 1;

    private final int id;
    private final String description;
    private boolean completed;

    public Task(final String description) {
        this.id = nextId++;
        this.description = description;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + this.getId() +
                ", description='" + this.getDescription() + '\'' +
                ", completed=" + this.isCompleted() +
                '}';
    }
}