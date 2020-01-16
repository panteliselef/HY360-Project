package db;

import model.Child;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChildDB {

    public static void addChild(Child ch) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" emp_children (age, emp_id)")
                    .append(" VALUES (")
                    .append("'").append(ch.getAge()).append("',")
                    .append("'").append(ch.getEmp_id()).append("');");


            String generatedColumns[] = {"child_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                ch.setId(id);
            }
            System.out.println("#DB: The child with age " + ch.getAge() + "  was successfully added in the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static List<Child> getChildren(Employee em) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        ArrayList<Child> children = new ArrayList<>();

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM emp_children WHERE emp_id = " + em.getId() + ";");

            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if (res.next()) {
                Child c = new Child(res.getInt("age"), res.getInt("emp_id"));
                c.setId(res.getInt("child_id"));
                children.add(c);

            }
            System.out.println("#DB: The children for emp_id " + em.getId() + "  was successfully recovered from the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return children;
    }

    public static List<Child> getChildren(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        ArrayList<Child> children = new ArrayList<>();

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            stmt.execute("SELECT * FROM emp_children WHERE emp_id = " + id + ";");

            ResultSet res = stmt.getResultSet();
            while (res.next()) {
                Child c = new Child(res.getInt("age"), res.getInt("emp_id"));
                c.setId(res.getInt("child_id"));
                children.add(c);
            }
            System.out.println("#DB: The children for emp_id " + id + "  was successfully recovered from the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return children;
    }
}
