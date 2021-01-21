package ca.victor.mytodolist.models;

public class Tasks {

    private int color;
    public long Id;
    public String name;
    public String description;
    public String important;
    public String completed;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public Tasks() {
    }

    public Tasks(String name, String desc, String important) {
        this.name = name;
        this.description = desc;
        this.important = important;
    }

    public Tasks(long id, String name, String desc, String important) {
        Id = id;
        this.name = name;
        this.description = desc;
        this.important = important;
    }

}
