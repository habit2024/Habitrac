package app.habitrac.com.models;

public class StepsLocalModel {
    String ID, name, time;
    boolean completed;

    public StepsLocalModel() {
    }


    public StepsLocalModel(String ID, String name, String time, boolean completed) {
        this.ID = ID;
        this.name = name;
        this.time = time;
        this.completed = completed;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
