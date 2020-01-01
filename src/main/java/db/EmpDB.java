package db;

import model.Employee;

import java.sql.*;

public class EmpDB {

    public static void addEmployee(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" employees (fname, lname, started_at,"
                            + "address, phone, IBAN, bank_name,department_id)")
                    .append(" VALUES (")
                    .append("'").append(emp.getFname()).append("',")
                    .append("'").append(emp.getLname()).append("',")
                    .append("'").append(emp.getStartedAt()).append("',")
                    .append("'").append(emp.getAddress()).append("',")
                    .append("'").append(emp.getPhone()).append("',")
                    .append("'").append(emp.getIBAN()).append("',")
                    .append("'").append(emp.getBankName()).append("',")
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
            CS360DB.closeDBConnection(stmt,con);
        }
    }
}
