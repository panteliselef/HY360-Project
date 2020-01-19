package model;



public class SumOfSal {
    private String year;
    private double amount;

    public SumOfSal(String year,double amount){
        this.year = year;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getYear() {
        return year;
    }
}
