package db;

import model.Employee;

import java.sql.*;

public class SalaryDB {

    private enum Bonus {
        FAMILY,
        ANNUAL,
        REASEARCH,
        LIBRARY
    }

    ;

    private static Double getBonus(Bonus bonusType) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            String tableAttribute = "";

            switch (bonusType) {
                case ANNUAL:
                    tableAttribute = "annual_bonus";
                    break;
                case FAMILY:
                    tableAttribute = "family_bonus";
                    break;
                case LIBRARY:
                    tableAttribute = "library_bonus";
                    break;
                case REASEARCH:
                    tableAttribute = "research_bonus";
                    break;
                default:
                    tableAttribute = "*";
                    break;
            }

            insQuery.append("SELECT " + tableAttribute + " FROM basic_salary_info;");


            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if (res.next()) {
                return res.getDouble(tableAttribute);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return null;
    }

    private static Double getBasicSalary(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            String salaryOption = "";
            if (emp.getSalaryType().equals("perm_admin")) {
                salaryOption = "perm_admin_salary";
            } else if (emp.getSalaryType().equals("perm_teach")) {
                salaryOption = "perm_teach_salary";
            }

            insQuery.append("SELECT " + salaryOption + " FROM ")
                    .append(" basic_salary_info;");


            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if (res.next()) {
                return res.getDouble(salaryOption);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return null;
    }

    public static void addSalary(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            Double basicSalary;

            if (emp.getTempSalary() == null) {
                basicSalary = getBasicSalary(emp);
            } else {
                basicSalary = emp.getTempSalary();
            }
//            if (basicSalary == null)return;
            insQuery.append("INSERT INTO ")
                    .append(" salaries (b_salary, family_bonus)")
                    .append(" VALUES (")
                    .append("'").append(basicSalary).append("',")
                    .append("'").append(getBonus(Bonus.FAMILY)).append("');");

            String generatedColumns[] = {"sal_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            ResultSet rs = stmtIns.getGeneratedKeys();
            int sal_id = 0;
            if (rs.next()) {
                int id = rs.getInt(1);
                sal_id = id;
                System.out.println("Salary id: " + id);
            }

            insQuery.setLength(0);

//            insQuery.append("INSERT INTO")
//                    .append(" emp_salaries (emp_id, sal_id)").append(" VALUES (").append(emp.getId()).append(",").append(sal_id).append(");");

//            StringBuilder insQuery1 = new StringBuilder();
//            insQuery1.append("INSERT INTO ")
//                    .append(" emp_salaries (emp_id, sal_id)")
//                    .append(" VALUES (")
//                    .append("'").append(emp.getId()).append("',")
//                    .append("'").append(sal_id).append("');");


            insQuery.append("INSERT INTO ")
                    .append(" emp_salaries (emp_id, sal_id) ")
                    .append(" VALUES (")
                    .append(emp.getId()).append(",")
                    .append(sal_id).append(");");

            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            insQuery.setLength(0);
            if (emp.getSalaryType().equals("perm_admin")) {
                insQuery.append("INSERT INTO ")
                        .append(" perm_admin_salaries (sal_id,annual_bonus)")
                        .append(" VALUES (")
                        .append("'").append(sal_id).append("',")
                        .append("'").append(getBonus(Bonus.ANNUAL)).append("');");

            } else if (emp.getSalaryType().equals("perm_teach")) {
                insQuery.append("INSERT INTO ")
                        .append(" perm_teach_salaries (sal_id,annual_bonus,research_bonus)")
                        .append(" VALUES (")
                        .append("'").append(sal_id).append("',")
                        .append("'").append(getBonus(Bonus.ANNUAL)).append("',")
                        .append("'").append(getBonus(Bonus.REASEARCH)).append("');");
            } else if (emp.getSalaryType().equals("temp_admin")) {
                insQuery.append("INSERT INTO ")
                        .append(" temp_admin_salaries (sal_id,start_date,end_date)")
                        .append(" VALUES (")
                        .append(sal_id).append(", '")
                        .append(new Date(emp.getStarts_at())).append("' , '")
                        .append(new Date(emp.getEnds_at())).append("' );");

            } else if (emp.getSalaryType().equals("temp_teach")) {
                insQuery.append("INSERT INTO ")
                        .append(" temp_teach_salaries (sal_id,start_date,end_date,library_bonus)")
                        .append(" VALUES (")
                        .append(sal_id).append(", '")
                        .append(new Date(emp.getStarts_at())).append("' , '")
                        .append(new Date(emp.getEnds_at())).append("',")
                        .append(getBonus(Bonus.LIBRARY)).append(");");
            }

            System.out.println(insQuery);

            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();


            System.out.println("#DB: The salary for  " + emp.getLname() + "  was successfully added in the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }
}
