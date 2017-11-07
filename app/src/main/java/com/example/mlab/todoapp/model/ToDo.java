package com.example.mlab.todoapp.model;

/**
 * Created by mLab on 2017/11/06.
 */

public class ToDo {
    String id,title,description;

    public ToDo() {
    }

    public ToDo(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
