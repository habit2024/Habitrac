package app.habitrac.com.models;

import java.util.ArrayList;

public class RoutineModel {
    String ID, name, context;
    int minutes;
    ArrayList<String> days;
    CompletedDaysModel daysCompleted;
    ArrayList<AddStepsChildModel> steps;
    long reminder;

    public RoutineModel() {
    }

    public RoutineModel(String ID, String name, String context, int minutes, ArrayList<String> days, CompletedDaysModel daysCompleted, ArrayList<AddStepsChildModel> steps) {
        this.ID = ID;
        this.name = name;
        this.context = context;
        this.minutes = minutes;
        this.days = days;
        this.daysCompleted = daysCompleted;
        this.steps = steps;
    }

    public RoutineModel(String ID, String name, String context, int minutes, ArrayList<String> days, CompletedDaysModel daysCompleted, ArrayList<AddStepsChildModel> steps, long reminder) {
        this.ID = ID;
        this.name = name;
        this.context = context;
        this.minutes = minutes;
        this.days = days;
        this.daysCompleted = daysCompleted;
        this.steps = steps;
        this.reminder = reminder;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public ArrayList<AddStepsChildModel> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<AddStepsChildModel> steps) {
        this.steps = steps;
    }

    public CompletedDaysModel getDaysCompleted() {
        return daysCompleted;
    }

    public void setDaysCompleted(CompletedDaysModel daysCompleted) {
        this.daysCompleted = daysCompleted;
    }

    public long getReminder() {
        return reminder;
    }

    public void setReminder(long reminder) {
        this.reminder = reminder;
    }

    public String getNombreCombinadoParaFirebase() {
        String cantidadPasos = getSteps().size() + " pasos"; // Combinar cantidad y unidad
        return getName() + " (" + cantidadPasos + ")";
    }

}
