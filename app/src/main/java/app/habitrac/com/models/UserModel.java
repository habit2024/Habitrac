package app.habitrac.com.models;

public class UserModel {
    String ID, name, email, password, goal,mlast,plast,username;
    int age;


    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password, String goal,String mlast,int age,String plast,String username ) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.goal = goal;
        this.mlast = mlast;
        this.plast = plast;
        this.age = age;
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    ////
    public String getMlast() {
        return mlast;
    }

    public void setMlast(String mlast) {
        this.mlast = mlast;
    }

    public String getPlast() {
        return plast;
    }

    public void setPlast(String plast) {
        this.plast = plast;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }



}
