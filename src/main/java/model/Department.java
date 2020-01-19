package model;

public class Department {

    private String name;
    private int id;
    public Department(String name){
        this.name = name;
    }
    public Department(int id){
        this.id = id;
    }
    public Department(String name, int id){
        this.id = id;
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

    @Override
    public String toString() {
        return "Department{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
