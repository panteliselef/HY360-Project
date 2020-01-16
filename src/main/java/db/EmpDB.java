package db;

import model.Child;
import model.Employee;

import java.sql.*;

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
}
