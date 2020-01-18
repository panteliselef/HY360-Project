package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Employee {

    private int id;
    private String fname;
    private String lname;
    private Date startedAt;
    private String address;
    private String phone;
    private String iban;
    private String bankName;
    private int departmentId;
    private ArrayList<Child> children = new ArrayList<>();
    private ArrayList<String> un_children = new ArrayList<>();
    private String salaryType;
    private String isMarried;
    private Double tempSalary;
    private Long starts_at;
    private Long ends_at;
    private double annual=0,family=0,research=0,library=0,b_sal=0,after_bonus_sal = 0;
    private String type;

    public Employee(){};

    public Employee(String fname, String lname, String address, String phone, String IBAN, String bankName) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.iban = IBAN;
        this.bankName = bankName;
        Date d = new Date(System.currentTimeMillis());
        startedAt = d;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAfter_bonus_sal() {
        return after_bonus_sal;
    }

    public void setAfter_bonus_sal(double after_bonus_sal) {
        this.after_bonus_sal = after_bonus_sal;
    }

    public double getB_sal() {
        return b_sal;
    }

    public void setB_sal(double b_sal) {
        this.b_sal = b_sal;
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

    public void setAnnual(double annual) {
        this.annual = annual;
    }

    public void setFamily(double family) {
        this.family = family;
    }

    public void setLibrary(double library) {
        this.library = library;
    }

    public void setResearch(double research) {
        this.research = research;
    }

    public int getId() {
        return id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEnds_at(Long ends_at) {
        this.ends_at = ends_at;
    }

    public void setStarts_at(Long starts_at) {
        this.starts_at = starts_at;
    }

    public void setTempSalary(Double tempSalary) {
        this.tempSalary = tempSalary;
    }


    public Date getStartedAt() {
        return startedAt;
    }

    public String getAddress() {
        return address;
    }

    public String getBankName() {
        return bankName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getFname() {
        return fname;
    }

    public String getIBAN() {
        return iban;
    }

    public String getLname() {
        return lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void addChild(Child ch){
        children.add(ch);
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public List<Child> getChildren() {
        return children;
    }

    public ArrayList<String> getUn_children() {
        return un_children;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public String isMarried() {
        return isMarried;
    }

    public Long getStarts_at() {
        return starts_at;
    }

    public Long getEnds_at() {
        return ends_at;
    }

    public Double getTempSalary() {
        return tempSalary;
    }

    public void setUn_children(ArrayList<String> un_children) {
        this.un_children = un_children;
    }
    public void setIsMarried(String isMarried) {
        this.isMarried = isMarried;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", startedAt=" + startedAt +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", iban='" + iban + '\'' +
                ", bankName='" + bankName + '\'' +
                ", departmentId=" + departmentId +
                ", children=" + children +
                ", un_children=" + un_children +
                ", salaryType='" + salaryType + '\'' +
                ", isMarried='" + isMarried + '\'' +
                ", tempSalary=" + tempSalary +
                ", starts_at=" + starts_at +
                ", ends_at=" + ends_at +
                ", annual=" + annual +
                ", family=" + family +
                ", research=" + research +
                ", library=" + library +
                ", b_sal=" + b_sal +
                ", after_bonus_sal=" + after_bonus_sal +
                ", type='" + type + '\'' +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "Employee{" +
//                "id=" + id +
//                ", fname='" + fname + '\'' +
//                ", lname='" + lname + '\'' +
//                ", startedAt=" + startedAt +
//                ", address='" + address + '\'' +
//                ", phone='" + phone + '\'' +
//                ", IBAN='" + iban + '\'' +
//                ", bankName='" + bankName + '\'' +
//                ", departmentId=" + departmentId +
//                ", children=" + children +
//                ", un_children=" + un_children +
//                ", salaryType=" + salaryType +
//                '}';
//    }
}
