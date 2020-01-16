package db;

import model.Employee;
import model.Salary;

import java.sql.*;

public class SalaryDB {

    private enum Bonus {
        FAMILY,
        ANNUAL,
        REASEARCH,
<<<<<<< HEAD
        LIBRARY
    }

    ;
=======
        LIBRARY,
        PERM_ADMIN,
        PERM_TEACH
    };
>>>>>>> d98b014c8ce9e299a5a5f5d263f0622b00eae901

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

<<<<<<< HEAD
            insQuery.append("SELECT " + tableAttribute + " FROM basic_salary_info;");
=======
            insQuery.append("SELECT "+tableAttribute+" FROM basic_salary_info WHERE id = 1;");
>>>>>>> d98b014c8ce9e299a5a5f5d263f0622b00eae901


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

    public static void updateSalary(Salary upd_salary) throws ClassNotFoundException{

        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE basic_salary_info ")
                    .append("SET ")
                    .append("perm_admin_salary = "+upd_salary.getPerm_admin_salary()+", "+
                            "perm_teach_salary = "+upd_salary.getPerm_teach_salary()+", "+
                            "annual_bonus = "+upd_salary.getAnnual_bonus()+", "+
                            "family_bonus = "+upd_salary.getFamily_bonus()+", "+
                            "research_bonus = "+upd_salary.getResearch_bonus()+", "+
                            "library_bonus = "+upd_salary.getLibrary_bonus())
                    .append(" WHERE id = 1;");




            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }


    private static Double getBasicSalary(Bonus salarytype) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            String salaryOption = "";
            if (salarytype == Bonus.PERM_ADMIN) {
                salaryOption = "perm_admin_salary";
            } else if (salarytype == Bonus.PERM_TEACH) {
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

    public static Salary getBasicSalary() throws ClassNotFoundException {
        Salary sal;

        double perm_admin = getBasicSalary(Bonus.PERM_ADMIN);
        double perm_teach = getBasicSalary(Bonus.PERM_TEACH);
        double annual_bonus = getBonus(Bonus.ANNUAL);
        double family_bonus = getBonus(Bonus.FAMILY);
        double research_bonus = getBonus(Bonus.REASEARCH);
        double library_bonus = getBonus(Bonus.LIBRARY);
        sal = new Salary(perm_admin,perm_teach,annual_bonus,family_bonus,research_bonus,library_bonus);
        return sal;

    }
}
