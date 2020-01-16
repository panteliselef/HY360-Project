package model;

public class Salary {
    private double perm_admin_salary;
    private double perm_teach_salary;
    private double annual_bonus;
    private double family_bonus;
    private double research_bonus;
    private double library_bonus;


    public Salary(double perm_admin_salary,double perm_teach_salary,double annual_bonus,double family_bonus,double research_bonus,double library_bonus){
        this.annual_bonus = annual_bonus;
        this.family_bonus = family_bonus;
        this.library_bonus = library_bonus;
        this.perm_admin_salary = perm_admin_salary;
        this.perm_teach_salary = perm_teach_salary;
        this.research_bonus = research_bonus;

    }




    public double getAnnual_bonus() {
        return annual_bonus;
    }

    public double getFamily_bonus() {
        return family_bonus;
    }

    public double getLibrary_bonus() {
        return library_bonus;
    }

    public double getPerm_admin_salary() {
        return perm_admin_salary;
    }

    public double getPerm_teach_salary() {
        return perm_teach_salary;
    }

    public double getResearch_bonus() {
        return research_bonus;
    }

    public void setAnnual_bonus(double annual_bonus) {
        this.annual_bonus = annual_bonus;
    }

    public void setFamily_bonus(double family_bonus) {
        this.family_bonus = family_bonus;
    }

    public void setLibrary_bonus(double library_bonus) {
        this.library_bonus = library_bonus;
    }

    public void setPerm_admin_salary(double perm_admin_salary) {
        this.perm_admin_salary = perm_admin_salary;
    }

    public void setPerm_teach_salary(double perm_teach_salary) {
        this.perm_teach_salary = perm_teach_salary;
    }

    public void setResearch_bonus(double research_bonus) {
        this.research_bonus = research_bonus;
    }
}
