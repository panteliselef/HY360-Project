package model;

public class Department {

    private String name;
    private int id;
    public Department(String name){
        this.name = name;
    }
    public Department(){}


    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
