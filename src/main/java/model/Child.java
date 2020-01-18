package model;

public class Child {
    private int id;
    private int age;
    private int emp_id;

    public Child(int age, int emp_id){
        this.age = age;
        this.emp_id = emp_id;
    }


    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Age:"+this.age + " Child_id: "+this.id;
    }
}
