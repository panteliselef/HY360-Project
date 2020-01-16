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

    public int getId() {
        return id;
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
                ", IBAN='" + iban + '\'' +
                ", bankName='" + bankName + '\'' +
                ", departmentId=" + departmentId +
                ", children=" + children +
                ", un_children=" + un_children +
                ", salaryType=" + salaryType +
                '}';
    }
}
