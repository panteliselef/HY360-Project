package db;

import model.Child;
import model.Employee;
import model.Salary;
import model.SumOfSal;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class SalaryDB {

    private enum Bonus {
        FAMILY,
        ANNUAL,
        REASEARCH,
        LIBRARY,
        PERM_ADMIN,
        PERM_TEACH
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
            insQuery.append("SELECT " + tableAttribute + " FROM basic_salary_info WHERE id = 1;");

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
                    .append("'").append(calculateFamilyBonus(emp.getId())).append("');");

            String generatedColumns[] = {"sal_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            ResultSet rs = stmtIns.getGeneratedKeys();
            int sal_id = 0;
            if (rs.next()) {
                int id = rs.getInt(1);
                sal_id = id;
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
                        .append("'").append(0).append("');");

            } else if (emp.getSalaryType().equals("perm_teach")) {
                insQuery.append("INSERT INTO ")
                        .append(" perm_teach_salaries (sal_id,annual_bonus,research_bonus)")
                        .append(" VALUES (")
                        .append("'").append(sal_id).append("',")
                        .append("'").append(0).append("',")
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



            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            insQuery.setLength(0);
            insQuery.append("UPDATE salaries ")
                    .append( " SET ")
                    .append(" after_bonus_sal = "+getAfterBonusSal(emp.getId())+" WHERE sal_id = "+sal_id+";");


            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            System.out.println("#DB: The salary for  " + emp.getLname() + "  was successfully added in the database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static void updateAfterBonusSal(int emp_id,double sal) throws ClassNotFoundException{
         Employee emp = EmpDB.EmployeeFullInfo(emp_id);
        Statement stmt = null;
        Connection con = null;
        PreparedStatement stmtIns;
        ResultSet rs;
        int sal_id = -1;
        StringBuilder insQuery = new StringBuilder();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+emp_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                sal_id = rs.getInt("sal_id");
            }
            insQuery.setLength(0);
            insQuery.append("UPDATE salaries SET b_salary = "+sal+" WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static void updateFamilyBonus(int id,double bonus) throws ClassNotFoundException{
        Employee emp = EmpDB.EmployeeFullInfo(id);
        Statement stmt = null;
        Connection con = null;
        PreparedStatement stmtIns;
        ResultSet rs;
        int sal_id = -1;
        StringBuilder insQuery = new StringBuilder();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                sal_id = rs.getInt("sal_id");
            }
            insQuery.setLength(0);
            insQuery.append("UPDATE salaries SET family_bonus = "+bonus+" WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static void updateSalary(Salary upd_salary) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE basic_salary_info ")
                    .append("SET ")
                    .append("perm_admin_salary = " + upd_salary.getPerm_admin_salary() + ", " +
                            "perm_teach_salary = " + upd_salary.getPerm_teach_salary() + ", " +
                            "annual_bonus = " + upd_salary.getAnnual_bonus() + ", " +
                            "family_bonus = " + upd_salary.getFamily_bonus() + ", " +
                            "research_bonus = " + upd_salary.getResearch_bonus() + ", " +
                            "library_bonus = " + upd_salary.getLibrary_bonus())
                    .append(" WHERE id = 1;");


            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            EmpDB.updateSalaryAndBonus();
        } catch (SQLException e) {
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
                System.out.println(res.getDouble(salaryOption));
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
        sal = new Salary(perm_admin, perm_teach, annual_bonus, family_bonus, research_bonus, library_bonus);
        return sal;

    }


    public static double MMMSalaryForTypeOfEmployee(String type_of_emp, String type_of_sal) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        double ret = 0;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            if (type_of_emp.equals("temp_admin")) {
                if (type_of_sal.equals("max")) {
                    insQuery.append("SELECT MAX(salaries.after_bonus_sal) AS max_sal FROM salaries INNER JOIN temp_admin_salaries" +
                            " ON salaries.sal_id = temp_admin_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("max_sal");
                        return ret;
                    }
                } else if (type_of_sal.equals("min")) {
                    insQuery.append("SELECT MIN(salaries.after_bonus_sal) AS min_sal FROM salaries INNER JOIN temp_admin_salaries" +
                            " ON salaries.sal_id = temp_admin_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("min_sal");
                        return ret;
                    }
                } else {
                    insQuery.append("SELECT AVG(salaries.after_bonus_sal) AS avg_sal FROM salaries INNER JOIN temp_admin_salaries" +
                            " ON salaries.sal_id = temp_admin_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("avg_sal");
                        return ret;
                    }
                }
            } else if (type_of_emp.equals("temp_teach")) {
                if (type_of_sal.equals("max")) {
                    insQuery.append("SELECT MAX(salaries.after_bonus_sal) AS max_sal FROM salaries INNER JOIN temp_teach_salaries" +
                            " ON salaries.sal_id = temp_teach_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("max_sal");
                        return ret;
                    }
                } else if (type_of_sal.equals("min")) {
                    insQuery.append("SELECT MIN(salaries.after_bonus_sal) AS min_sal FROM salaries INNER JOIN temp_teach_salaries" +
                            " ON salaries.sal_id = temp_teach_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("min_sal");
                        return ret;
                    }
                } else {
                    insQuery.append("SELECT AVG(salaries.after_bonus_sal) AS avg_sal FROM salaries INNER JOIN temp_teach_salaries" +
                            " ON salaries.sal_id = temp_teach_salaries.sal_id WHERE promotion_date IS NULL;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("avg_sal");
                        return ret;
                    }
                }
            } else if (type_of_emp.equals("perm_admin")) {
                if (type_of_sal.equals("max")) {
                    insQuery.append("SELECT MAX(salaries.after_bonus_sal) AS max_sal FROM salaries INNER JOIN perm_admin_salaries" +
                            " ON salaries.sal_id = perm_admin_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("max_sal");
                        return ret;
                    }
                } else if (type_of_sal.equals("min")) {
                    insQuery.append("SELECT MIN(salaries.after_bonus_sal) AS min_sal FROM salaries INNER JOIN perm_admin_salaries" +
                            " ON salaries.sal_id = perm_admin_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("min_sal");
                        return ret;
                    }
                } else {
                    insQuery.append("SELECT AVG(salaries.after_bonus_sal) AS avg_sal FROM salaries INNER JOIN perm_admin_salaries" +
                            " ON salaries.sal_id = perm_admin_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("avg_sal");
                        return ret;
                    }
                }
            } else {
                if (type_of_sal.equals("max")) {
                    insQuery.append("SELECT MAX(salaries.after_bonus_sal) AS max_sal FROM salaries INNER JOIN perm_teach_salaries" +
                            " ON salaries.sal_id = perm_teach_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("max_sal");
                        return ret;
                    }
                } else if (type_of_sal.equals("min")) {
                    insQuery.append("SELECT MIN(salaries.after_bonus_sal) AS min_sal FROM salaries INNER JOIN perm_teach_salaries" +
                            " ON salaries.sal_id = perm_teach_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("min_sal");
                        return ret;
                    }
                } else {
                    insQuery.append("SELECT AVG(salaries.after_bonus_sal) AS avg_sal FROM salaries INNER JOIN perm_teach_salaries" +
                            " ON salaries.sal_id = perm_teach_salaries.sal_id;");
                    PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    ResultSet rs = stmtIns.getResultSet();
                    if (rs.next()) {
                        ret = rs.getDouble("avg_sal");
                        return ret;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return ret;
    }

    public static HashMap<Integer,Double> getAvgIncrease(String category) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        Employee emp;
        ArrayList<Integer> years = new ArrayList<>();

        HashMap<Integer,Double> infoPerYear = new HashMap<>();

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT DISTINCT YEAR(paid_at) as year from payments GROUP BY paid_at;");
            stmt.executeQuery(insQuery.toString());
            ResultSet res = stmt.getResultSet();

            while (res.next()){
                years.add(res.getInt("year"));
            }

            String query = "";



            for (int year: years
                 ) {


                if(category.equals("family_bonus")){
                    query = "SELECT ((m.fam-s.fam) / s.fam *100) as percent FROM\n" +
                            "(SELECT (Sum(family_bonus)) as fam from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join salaries on salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) < "+year+") s,\n" +
                            "(SELECT (Sum(family_bonus)) as fam from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join salaries on salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) = "+year+") m;";

                }else if(category.equals("payments")){
                    query = "SELECT ((m.sum - s.sum)/s.sum*100) as percent from \n" +
                            "(SELECT (Sum(ammount)) as sum from payments  WHERE YEAR(paid_at) < "+year+") s,\n" +
                            "(SELECT (Sum(ammount)) as sum from payments WHERE YEAR(paid_at) = "+year+") m;";

                }else if(category.equals("annual_bonus")){
                    query = "SELECT ((m.annual-s.annual) / s.annual *100) as percent FROM\n" +
                            "(SELECT (Sum(annual_bonus)) as annual from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join (SELECT annual_bonus,sal_id from perm_admin_salaries UNION SELECT annual_bonus,sal_id from  perm_teach_salaries ) as m on m.sal_id = emp_salaries.sal_id WHERE YEAR (paid_at) < "+year+") s,\n" +
                            "(SELECT (Sum(annual_bonus)) as annual from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join (SELECT annual_bonus,sal_id from perm_admin_salaries UNION SELECT annual_bonus,sal_id from  perm_teach_salaries ) as m on m.sal_id = emp_salaries.sal_id WHERE YEAR (paid_at) = "+year+") m;";
                }else if(category.equals("research_bonus")){
                    query = "SELECT ((m.research-s.research) / s.research *100) as percent FROM\n" +
                            "(SELECT (Sum(research_bonus)) as research from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join perm_teach_salaries on perm_teach_salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) < "+year+") s,\n" +
                            "(SELECT (Sum(research_bonus)) as research from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join perm_teach_salaries on perm_teach_salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) = "+year+") m;";
                }else if(category.equals("library_bonus")){
                    query = "SELECT ((m.library-s.library) / s.library *100) as percent FROM\n" +
                            "(SELECT (Sum(library_bonus)) as library from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join temp_teach_salaries on temp_teach_salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) < "+year+") s,\n" +
                            "(SELECT (Sum(library_bonus)) as library from payments Inner JOIN emp_salaries on emp_salaries.emp_id = payments.emp_id INNER join temp_teach_salaries on temp_teach_salaries.sal_id = emp_salaries.sal_id WHERE YEAR(paid_at) = "+year+") m;";
                }

                insQuery.setLength(0);
                insQuery.append(query);
                stmt.executeQuery(insQuery.toString());
                res = stmt.getResultSet();

                if(res.next()){
//                    System.out.println(category+" "+res.getDouble("percent"));
                    infoPerYear.put(year,res.getDouble("percent"));
//                    years.add(res.getInt("year"));
                }else {
                    infoPerYear.put(year, (double) 0);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }

        return infoPerYear;


    }

    public static double calculateFamilyBonus(int id) throws ClassNotFoundException {
        double family_bonus = 0;
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            Employee emp = EmpDB.getEmployee(id);
            ArrayList<Child> ch = (ArrayList<Child>) ChildDB.getChildren(id);
            ArrayList<Child>teen_ch = new ArrayList<>();
            for(int i = 0;i<ch.size();i++){
                if(ch.get(i).getAge()<18){
                    teen_ch.add(ch.get(i));
                }
            }
            double basic_fambonus = getBonus(Bonus.FAMILY);
            String ismarried = EmpDB.getEmployee(id).isMarried();
            if (ismarried.equals("yes")) {
                family_bonus = (teen_ch.size() + 1) * basic_fambonus;
            } else {
                family_bonus = teen_ch.size() * basic_fambonus;
            }
//            System.out.println("FAMILY BONUS IS " + family_bonus);
            return family_bonus;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }

        return family_bonus;
    }

    public static double getAfterBonusSal(int id) throws ClassNotFoundException {
        double ret = 0;
        Employee emp = EmpDB.EmployeeFullInfo(id);
        double fam_bonus = calculateFamilyBonus(id);


//        System.out.println(emp);
        System.out.println(emp.getB_sal());
        ret = emp.getB_sal() + emp.getAnnual() * emp.getB_sal()/ 100 + emp.getB_sal() * fam_bonus /100 + emp.getResearch() + emp.getLibrary();
        System.out.println("AFTER BONUS IS " + ret);
        return ret;
    }


    public static ArrayList<Double> getAfterBonusSal() throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        ArrayList<Double> ret = new ArrayList<>();
        double fam_bonus;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            ArrayList<Employee> employees = EmpDB.getEmployees();
            Employee emp;
            for (int i = 0; i < employees.size(); i++) {
                emp = EmpDB.EmployeeFullInfo(employees.get(i).getId());
                fam_bonus = calculateFamilyBonus(emp.getId());
//                ret = emp.getB_sal() + emp.getAnnual() * emp.getB_sal() + emp.getB_sal() * fam_bonus + emp.getResearch() + emp.getLibrary();
                ret.add(emp.getB_sal() + emp.getAnnual() * emp.getB_sal() + emp.getB_sal() * fam_bonus + emp.getResearch() + emp.getLibrary());
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return ret;
    }

//    public static double calculateSumOfSal(String type_of_emp) throws ClassNotFoundException {
//        Statement stmt = null;
//        Connection con = null;
//        double ret = 0;
//        ArrayList<Employee> employees = EmpDB.getEmployees();
//        StringBuilder insQuery = new StringBuilder();
//        PreparedStatement stmtIns;
//        ResultSet rs;
//
//        try {
//            con = CS360DB.getConnection();
//            stmt = con.createStatement();
////            for (int i = 0; i < employees.size(); i++) {
////                if (EmpDB.findEmployeeType(employees.get(i).getId()).equals(type_of_emp)) {
////                    ret += SalaryDB.getAfterBonusSal(employees.get(i).getId());
////                }
////            }
//            if(type_of_emp.equals("perm_admin")) {
//                insQuery.append("SELECT SUM(after_bonus_sal) SumQuantity FROM salaries INNER JOIN perm_admin_salaries "+
//                        "WHERE salaries.sal_id = perm_admin_salaries.sal_id;");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                System.out.println(insQuery);
//                rs = stmtIns.getResultSet();
//                if(rs.next()){
//                    ret = rs.getDouble("SumQuantity");
//                }
//            }else if(type_of_emp.equals("perm_teach")){
//                insQuery.append("SELECT SUM(after_bonus_sal) SumQuantity FROM salaries INNER JOIN perm_teach_salaries "+
//                        "WHERE salaries.sal_id = perm_teach_salaries.sal_id;");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                System.out.println(insQuery);
//                rs = stmtIns.getResultSet();
//                if(rs.next()){
//                    ret = rs.getDouble("SumQuantity");
//                }
//            }
//            else if(type_of_emp.equals("temp_admin")){
//                    insQuery.append("SELECT SUM(after_bonus_sal) SumQuantity FROM salaries INNER JOIN temp_admin_salaries "+
//                            "WHERE salaries.sal_id = temp_admin_salaries.sal_id;");
//                    stmtIns = con.prepareStatement(insQuery.toString());
//                    stmtIns.executeQuery();
//                    System.out.println(insQuery);
//                    rs = stmtIns.getResultSet();
//                    if(rs.next()){
//                        ret = rs.getDouble("SumQuantity");
//                    }
//                }else {
//                    insQuery.append("SELECT SUM(after_bonus_sal) SumQuantity FROM salaries INNER JOIN temp_teach_salaries "+
//                            "ON salaries.sal_id = temp_teach_salaries.sal_id;");
//                    System.out.println(insQuery);
//                    stmtIns = con.prepareStatement(insQuery.toString());
//                    stmtIns.executeQuery();
//                    rs = stmtIns.getResultSet();
//                    if(rs.next()){
//                        ret = rs.getDouble("SumQuantity");
//                    }
//
//            }
//            return ret;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            CS360DB.closeDBConnection(stmt, con);
//        }
//        return ret;
//    }

    public static ArrayList<SumOfSal> calculateSumOfSal(String type_of_emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        double ret = 0;

        ArrayList<SumOfSal> arr = new ArrayList<>();
        StringBuilder insQuery = new StringBuilder();
        PreparedStatement stmtIns;
        ResultSet rs;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
//            for (int i = 0; i < employees.size(); i++) {
//                if (EmpDB.findEmployeeType(employees.get(i).getId()).equals(type_of_emp)) {
//                    ret += SalaryDB.getAfterBonusSal(employees.get(i).getId());
//                }
//            }
            if(type_of_emp.equals("perm_admin")) {
//                insQuery.append("SELECT YEAR(paid_at) as year, SUM(ammount) as amount FROM  emp_salaries INNER JOIN  perm_admin_salaries ON emp_salaries.sal_id = perm_admin_salaries.sal_id Inner JOIN payments ON payments.emp_id = emp_salaries.emp_id GROUP BY YEAR(paid_at);");
                insQuery.append("SELECT YEAR(paid_at) as year,SUM(ammount) as amount FROM emp_salaries JOIN perm_admin_salaries ON emp_salaries.sal_id = perm_admin_salaries.sal_id JOIN payments ON payments.emp_id = emp_salaries.emp_id LEFT JOIN temp_admin_salaries on temp_admin_salaries.sal_id = emp_salaries.sal_id WHERE promotion_date<=paid_at OR promotion_date is NULL GROUP BY YEAR(paid_at);");
            }else if(type_of_emp.equals("perm_teach")){
                insQuery.append("SELECT YEAR(paid_at) as year,SUM(ammount) as amount FROM emp_salaries JOIN perm_teach_salaries ON emp_salaries.sal_id = perm_teach_salaries.sal_id JOIN payments ON payments.emp_id = emp_salaries.emp_id LEFT JOIN temp_teach_salaries on temp_teach_salaries.sal_id = emp_salaries.sal_id WHERE promotion_date<=paid_at OR promotion_date is NULL GROUP BY YEAR(paid_at);");
            }
            else if(type_of_emp.equals("temp_admin")){
//                insQuery.append("SELECT YEAR(paid_at) as year, SUM(ammount) as amount FROM  emp_salaries INNER JOIN  temp_admin_salaries ON emp_salaries.sal_id = temp_admin_salaries.sal_id Inner JOIN payments ON payments.emp_id = emp_salaries.emp_id GROUP BY YEAR(paid_at);");
                insQuery.append("SELECT YEAR(paid_at) as year, SUM(ammount) as amount  FROM  emp_salaries INNER JOIN  temp_admin_salaries ON emp_salaries.sal_id = temp_admin_salaries.sal_id Inner JOIN payments ON payments.emp_id = emp_salaries.emp_id WHERE promotion_date < paid_at or promotion_date IS NULL GROUP BY YEAR(paid_at);");
            }else {
//                insQuery.append("SELECT YEAR(paid_at) as year, SUM(ammount) as amount FROM  emp_salaries INNER JOIN  temp_teach_salaries ON emp_salaries.sal_id = temp_teach_salaries.sal_id Inner JOIN payments ON payments.emp_id = emp_salaries.emp_id GROUP BY YEAR(paid_at);");
                insQuery.append("SELECT YEAR(paid_at) as year, SUM(ammount) as amount  FROM  emp_salaries INNER JOIN  temp_teach_salaries ON emp_salaries.sal_id = temp_teach_salaries.sal_id Inner JOIN payments ON payments.emp_id = emp_salaries.emp_id WHERE promotion_date < paid_at or promotion_date IS NULL GROUP BY YEAR(paid_at);");
            }
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            while (rs.next()){
                arr.add(new SumOfSal(rs.getString("year"),rs.getDouble("amount")));
            }
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return arr;
    }


    public static void updateAnnualBonus() throws ClassNotFoundException {
        ArrayList<Employee> employees = EmpDB.getEmployees();
        Statement stmt = null;
        Connection con = null;
        StringBuilder insQuery = new StringBuilder();
        PreparedStatement stmtIns;
        ResultSet rs;
        Employee emp;
        Date d = new Date(System.currentTimeMillis());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        int curyear = calendar.get(Calendar.YEAR);
        int curmonth = calendar.get(Calendar.MONTH)+1;
        int curday = calendar.get(Calendar.DAY_OF_MONTH);
        int empyear,empmonth,empday;
        double bonus;
        int sal_id = -1;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            for(int i = 0;i<employees.size();i++){
                insQuery.setLength(0);
                emp = EmpDB.EmployeeFullInfo(employees.get(i).getId());
                if(EmpDB.findEmployeeType(emp.getId()).equals("perm_admin")||EmpDB.findEmployeeType(emp.getId()).equals("temp_admin")) {
                    calendar.setTime(emp.getStartedAt());
                    empyear = calendar.get(Calendar.YEAR);
                    empmonth = calendar.get(Calendar.MONTH)+1;
                    empday = calendar.get(Calendar.DAY_OF_MONTH);
                    if (curyear>empyear && curmonth>=empmonth&&curday>=empday){
                        bonus = SalaryDB.getBonus(Bonus.ANNUAL) + SalaryDB.getBonus(Bonus.ANNUAL)*(curyear-empyear);
                    }else if((curyear>empyear && curmonth< empmonth) || (curyear>empyear && curmonth == empmonth && curday<empmonth)){
                        bonus = SalaryDB.getBonus(Bonus.ANNUAL) + SalaryDB.getBonus(Bonus.ANNUAL)*(curyear-empyear-1);
                    }else{
                        bonus = 0;
                    }
                    insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+emp.getId()+";");
                    stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeQuery();
                    rs = stmtIns.getResultSet();
                    if(rs.next()){
                        sal_id = rs.getInt("sal_id");
                    }
                    insQuery.setLength(0);
                    if(EmpDB.findEmployeeType(emp.getId()).equals("perm_admin")){
                        insQuery.append("UPDATE perm_admin_salaries SET annual_bonus = "+bonus+" WHERE sal_id = "+sal_id+";");
                    }else {
                        insQuery.append("UPDATE perm_teach_salaries SET annual_bonus = "+bonus+" WHERE sal_id = "+sal_id+";");
                    }
                    stmtIns = con.prepareStatement(insQuery.toString());
                    stmtIns.executeUpdate();
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }



}
