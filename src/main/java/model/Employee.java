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
    private String IBAN;
    private String bankName;
    private int departmentId;
    private ArrayList<Child> children = new ArrayList<>();

    public Employee(String fname, String lname, String address, String phone, String IBAN, String bankName) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.phone = phone;
        this.IBAN = IBAN;
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
        return IBAN;
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

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public void addChild(Child ch){
        children.add(ch);
    }

    public List<Child> getChildren() {
        return children;
    }
}
