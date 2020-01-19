package model;

import java.util.ArrayList;

public class Payment {
    private String emp_names="";
    private double salary=0;
    private double annual=0;
    private double research=0;
    private double library=0;
    private double family=0;

    public Payment(String emp, double sal, double ann, double res, double lib, double fam){
        emp_names = emp;
        salary = sal;
        annual = ann;
        research = res;
        library = lib;
        family = fam;
    }

    public double getAnnual() {
        return annual;
    }

    public double getFamily() {
        return family;
    }

    public double getLibrary() {
        return library;
    }

    public double getResearch() {
        return research;
    }

    public double getSalary() {
        return salary;
    }

    public String getEmp_names() {
        return emp_names;
    }

    public void setResearch(double research) {
        this.research = research;
    }

    public void setLibrary(double library) {
        this.library = library;
    }

    public void setFamily(double family) {
        this.family = family;
    }

    public void setAnnual(double annual) {
        this.annual = annual;
    }

    public void setEmp_names(String emp_names) {
        this.emp_names = emp_names;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
