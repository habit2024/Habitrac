package app.habitrac.com.models;

import java.util.ArrayList;

public class AddStepsModel {
    String title;
    ArrayList<AddStepsChildModel> list;

    public AddStepsModel(String title, ArrayList<AddStepsChildModel> list) {
        this.title = title;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<AddStepsChildModel> getList() {
        return list;
    }

    public void setList(ArrayList<AddStepsChildModel> list) {
        this.list = list;
    }
}
