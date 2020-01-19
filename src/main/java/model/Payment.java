package model;

import java.util.ArrayList;

public class Payment {
    private ArrayList<String> emp_names;
    private ArrayList<Double> salary;
    private ArrayList<Double> annual;
    private ArrayList<Double> research;
    private ArrayList<Double> library;
    private ArrayList<Double> family;

    public Payment(ArrayList<String> emp, ArrayList<Double> sal, ArrayList<Double> ann, ArrayList<Double> res, ArrayList<Double> lib, ArrayList<Double> fam){
        emp_names = emp;
        salary = sal;
        annual = ann;
        research = res;
        library = lib;
        family = fam;
    }

    public ArrayList<Double> getAnnual() {
        return annual;
    }

    public ArrayList<Double> getFamily() {
        return family;
    }

    public ArrayList<Double> getLibrary() {
        return library;
    }

    public ArrayList<Double> getResearch() {
        return research;
    }

    public ArrayList<Double> getSalary() {
        return salary;
    }

    public ArrayList<String> getEmp_names() {
        return emp_names;
    }

    public void setResearch(ArrayList<Double> research) {
        this.research = research;
    }

    public void setLibrary(ArrayList<Double> library) {
        this.library = library;
    }

    public void setFamily(ArrayList<Double> family) {
        this.family = family;
    }

    public void setAnnual(ArrayList<Double> annual) {
        this.annual = annual;
    }

    public void setEmp_names(ArrayList<String> emp_names) {
        this.emp_names = emp_names;
    }

    public void setSalary(ArrayList<Double> salary) {
        this.salary = salary;
    }
}
