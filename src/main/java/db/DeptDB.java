package db;

import model.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeptDB {

    public static List<Department> getDepartments() throws ClassNotFoundException {
        List<Department> departments = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM departments;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Department dept = new Department();
                dept.setId(res.getInt("dep_id"));
                dept.setName(res.getString("name"));
                departments.add(dept);
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(DeptDB.class.getName()).log(Level.SEVERE, "");
        } finally {
            // close connection
            CS360DB.closeDBConnection(stmt, con);
        }

        return departments;
    }

    public static Department getDepartment(int depId) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;

        System.out.println("DEP: "+depId);
        Department dept = new Department();
        try {

            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM departments WHERE dep_id = "+depId+";");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next()) {
                dept.setId(res.getInt("dep_id"));
                dept.setName(res.getString("name"));
            }

            System.out.println(dept);

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(DeptDB.class.getName()).log(Level.SEVERE, "");
        } finally {
            // close connection
            CS360DB.closeDBConnection(stmt, con);
        }
        return dept;
    }

    public static void addDepartment(String name) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" departments (name)")
                    .append(" VALUES (")
                    .append("'").append(name).append("')");

            String generatedColumns[] = {"dep_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                // Update value of setID based on database
                int id = rs.getInt(1);
                System.out.println("#DB: Department added, name :" + name + " id: " + id);
//                user.setUserID(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }
}
