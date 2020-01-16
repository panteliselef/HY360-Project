package db;

import model.Child;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;

public class EmpDB {

    public static boolean alreadyExists(String fname, String lname) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT fname,lname FROM ")
                    .append(" employees WHERE fname='")
                    .append(fname).append("' and lname='")
                    .append(lname).append("';");


            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if (res.next()) {
                System.out.println(res.getString("fname")+"  "+res.getString("lname"));
                return true;
            } else return false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return true;
    }

    public static void addEmployee(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        if(alreadyExists(emp.getFname(),emp.getLname())) throw new ClassNotFoundException();

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" employees (fname, lname, started_at,"
                            + "address, phone, IBAN, bank_name,is_married,department_id)")
                    .append(" VALUES (")
                    .append("'").append(emp.getFname()).append("',")
                    .append("'").append(emp.getLname()).append("',")
                    .append("'").append(emp.getStartedAt()).append("',")
                    .append("'").append(emp.getAddress()).append("',")
                    .append("'").append(emp.getPhone()).append("',")
                    .append("'").append(emp.getIBAN()).append("',")
                    .append("'").append(emp.getBankName()).append("',")
                    .append("'").append(emp.isMarried()).append("',")
                    .append("'").append(emp.getDepartmentId()).append("');");

            String generatedColumns[] = {"emp_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                emp.setId(id);
            }
            System.out.println("#DB: The employee " + emp.getLname() + "  was successfully added in the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static void updateEmployee(Employee emp) throws ClassNotFoundException{
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE employees ")
                    .append("SET ")
                    .append("fname = '"+emp.getFname()+"', "+
                            "lname = '"+emp.getLname()+"', "+
                            "started_at = '"+emp.getStartedAt()+"', "+
                            "address = '"+emp.getAddress()+"', "+
                            "phone = '"+emp.getPhone()+"', "+
                            "IBAN = '"+emp.getIBAN()+"', "+
                            "bank_name = '"+emp.getBankName()+"', "+
                            "department_id = "+emp.getDepartmentId()+", "+
                            "is_married = '"+emp.isMarried())
                    .append("' WHERE emp_id = "+emp.getId()+";");




            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            ArrayList<Child> children = (ArrayList<Child>) emp.getChildren();
            System.out.println(children);
            for(int i = 0;i<emp.getChildren().size();i++) {
                insQuery.setLength(0);
                insQuery.append("UPDATE emp_children ")
                        .append("SET ")
                        .append("age = "+children.get(i).getAge())
                        .append(" WHERE child_id = "+children.get(i).getId()+" AND emp_id = "+emp.getId()+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            }



        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static Employee getEmployee(int id) throws  ClassNotFoundException{
        Statement stmt = null;
        Connection con = null;
        Employee emp;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            String fname="",lname="",address="",phone="",IBAN="",bank_name="",ismarried="";
            Date started_at=null;
            int dep_id = -1;
            insQuery.append("SELECT * from employees WHERE emp_id = "+id+";");


            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if(res.next()) {
                fname = res.getString("fname");
                lname = res.getString("lname");
                started_at = res.getDate("started_at");
                address = res.getString("address");
                phone = res.getString("phone");
                IBAN = res.getString("IBAN");
                bank_name = res.getString("bank_name");
                dep_id = res.getInt("department_id");
                ismarried = res.getString("is_married");



            }
            emp = new Employee(fname, lname, address, phone, IBAN, bank_name);
            emp.setStartedAt(started_at);
            emp.setDepartmentId(dep_id);
            emp.setIsMarried(ismarried);
            insQuery.setLength(0);
            emp.setChildren((ArrayList<Child>) ChildDB.getChildren(id));
            emp.setId(id);
            return emp;




        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return null;
    }

}
