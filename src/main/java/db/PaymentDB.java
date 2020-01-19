package db;


import model.Employee;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PaymentDB {

    public static void addPayment() throws ClassNotFoundException{
        Statement stmt = null;
        Connection con = null;
        try{
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            ArrayList<Employee> employees = EmpDB.getEmployees();
            Employee emp;
            PreparedStatement stmtIns;
            ResultSet rs;
            int sal_id = -1;
            Date d,left;
            d = new Date(System.currentTimeMillis());


            insQuery.setLength(0);
            insQuery.append("SELECT * FROM payments;");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            boolean hasPayments =  rs.next();

            insQuery.setLength(0);
            insQuery.append("SELECT * FROM payments WHERE paid_at = '"+d.toString()+"';");
            System.out.println(insQuery);
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next() && hasPayments)return;
            insQuery.setLength(0);
            for(int i = 0;i<employees.size();i++){
                d = new Date(System.currentTimeMillis());
//                Calendar calendar = new GregorianCalendar();
//                calendar.setTime(d);
//                int curyear = calendar.get(Calendar.YEAR);
//                int curmonth = calendar.get(Calendar.MONTH) + 1;
//                int curday = calendar.get(Calendar.DAY_OF_MONTH);
                insQuery.setLength(0);
                emp = EmpDB.EmployeeFullInfo(employees.get(i).getId());
                left = emp.getLeft_at();
                if(left!=null){
//                    Calendar calendar1 = new GregorianCalendar();
//                    calendar1.setTime(left);
//                    int leftyear = calendar1.get(Calendar.YEAR);
//                    int leftmonth = calendar1.get(Calendar.MONTH) + 1;
//                    int leftday = calendar1.get(Calendar.DAY_OF_MONTH);
                    if(left.getTime()<d.getTime()){
                        continue;
//                        emp.setAfter_bonus_sal(0);
//                        System.out.println("UYASSSS");
                    }
                }else {
                    if(EmpDB.findEmployeeType(emp.getId()).equals("temp_admin") || (EmpDB.findEmployeeType(emp.getId()).equals("temp_teach"))){
                        Date end = new Date(emp.getEnds_at());
//                        Calendar calendar1 = new GregorianCalendar();
//                        calendar1.setTime(end);
//                        int leftyear = calendar1.get(Calendar.YEAR);
//                        int leftmonth = calendar1.get(Calendar.MONTH) + 1;
//                        int leftday = calendar1.get(Calendar.DAY_OF_MONTH);
                        Date promo = emp.getPromo_date();
                        if(end.getTime()<d.getTime() && promo == null){
                            emp.setAfter_bonus_sal(0);
                        }
                    }
                }
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+emp.getId()+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                if(rs.next()){
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("INSERT INTO payments(paid_at,ammount,emp_id) VALUES('"+d+"',"+
                        SalaryDB.getAfterBonusSal(emp.getId())+", "+emp.getId()+");");
                String generatedColumns[] = {"bill_id"};
                stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
                stmtIns.executeUpdate();

                rs = stmtIns.getGeneratedKeys();
                int bill_id = 0;
                if (rs.next()) {
                    int id = rs.getInt(1);
                    bill_id = id;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static Payment getPaymentInfo(String type,Date from,Date to) throws ClassNotFoundException{
        Payment info = null;
        ArrayList<Employee> employees = EmpDB.getEmployees();
        Employee emp;
        Date paid = null;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Double> sal = new ArrayList<>();
        ArrayList<Double> annual = new ArrayList<>();
        ArrayList<Double> family= new ArrayList<>();
        ArrayList<Double> research= new ArrayList<>();
        ArrayList<Double> library= new ArrayList<>();
        Statement stmt = null;
        PreparedStatement stmtIns;
        ResultSet rs;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            for (int i = 0; i < employees.size(); i++) {
                insQuery.setLength(0);
                if (EmpDB.findEmployeeType(employees.get(i).getId()).equals(type)) {
                    emp = EmpDB.EmployeeFullInfo(employees.get(i).getId());
                    insQuery.append("SELECT paid_at FROM payments WHERE emp_id = "+emp.getId()+";");
                    stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    rs = stmtIns.getResultSet();
                    if(rs.next()){
                        paid = rs.getDate("paid_at");
                    }
                    if (from.getTime() <= paid.getTime() && to.getTime()>= paid.getTime()) {
                        names.add(emp.getFname() + " " + emp.getFname());
                        sal.add(SalaryDB.getAfterBonusSal(emp.getId()));
                        annual.add(emp.getAnnual());
                        family.add(emp.getFamily());
                        research.add(emp.getResearch());
                        library.add(emp.getLibrary());
                    }
                }
            }
            info = new Payment(names,sal,annual,research,library,family);
            return info;
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return info;
    }
}
