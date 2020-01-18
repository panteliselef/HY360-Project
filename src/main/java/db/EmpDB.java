package db;

import model.Child;
import model.Employee;
import model.Salary;

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

    public static ArrayList<Employee> getEmployees() throws  ClassNotFoundException{

        Statement stmt = null;
        Connection con = null;
        ArrayList<Employee> emps =  new ArrayList<>();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * from employees;");
            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            while(res.next()) {
                Employee emp = new Employee();
                emp.setFname(res.getString("fname"));
                emp.setLname(res.getString("lname"));
                emp.setStartedAt(res.getDate("started_at"));
                emp.setAddress(res.getString("address"));
                emp.setPhone(res.getString("phone"));
                emp.setIban(res.getString("IBAN"));
                emp.setBankName(res.getString("bank_name"));
                emp.setDepartmentId(res.getInt("department_id"));
                emp.setIsMarried(res.getString("is_married"));
                emp.setId(res.getInt("emp_id"));
                emps.add(emp);
            }
            return emps;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return null;
    }


    private static String findEmployeeType(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String type="";
        int sal_id = -1;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            ResultSet rs = stmtIns.getResultSet();
            if(rs.next()){
                sal_id = rs.getInt("sal_id");
            }
            insQuery.setLength(0);
            insQuery.append("SELECT * from perm_admin_salaries WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                type = "perm_admin";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from perm_teach_salaries WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                type = "perm_teach";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from temp_admin_salaries WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                type = "temp_admin";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from temp_teach_salaries WHERE sal_id = "+sal_id+";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if(rs.next()){
                type = "temp_teach";
            }

            return type;
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return type;
    }

    public static void promoteEmployee(int id) throws ClassNotFoundException {
        String type =  findEmployeeType(id);
        StringBuilder insQuery = new StringBuilder();
        Salary sal = SalaryDB.getBasicSalary();
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            int sal_id=-1;
//            double annual_bonus,research_bonus;
            if (type.equals("temp_admin")) {
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()){
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("DELETE FROM temp_admin_salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("UPDATE salaries SET b_salary = "+sal.getPerm_admin_salary()+" WHERE sal_id = "+sal_id+";");
                con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("INSERT INTO perm_admin_salaries(sal_id,annual_bonus) VALUES("+
                        sal_id+","+sal.getAnnual_bonus()+");");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            }else if(type.equals("temp_teach")){
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()){
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("DELETE FROM temp_teach_salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("UPDATE salaries SET b_salary = "+sal.getPerm_teach_salary()+" WHERE sal_id = "+sal_id+";");
                con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("INSERT INTO perm_teach_salaries(sal_id,annual_bonus,research_bonus) VALUES("+
                        sal_id+","+sal.getAnnual_bonus()+","+sal.getResearch_bonus()+");");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            CS360DB.closeDBConnection(stmt, con);
        }

    }

    public static Employee EmployeeFullInfo(int id) throws ClassNotFoundException {
        Employee emp = getEmployee(id);
        String type = findEmployeeType(id);
        Salary sal = SalaryDB.getBasicSalary();
        emp.setType(type);
        Statement stmt = null;
        Connection con = null;
        int sal_id = -1;
        StringBuilder insQuery = new StringBuilder();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            if (type.equals("perm_admin")) {
                emp.setAnnual(sal.getAnnual_bonus());
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                if(rs.next()) {
                    emp.setFamily(rs.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
                    emp.setB_sal(rs.getDouble("b_salary"));
                }
                return emp;
            }else if(type.equals("perm_teach")){
                emp.setAnnual(sal.getAnnual_bonus());
                emp.setResearch(sal.getResearch_bonus());
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                if(rs.next()) {
                    emp.setFamily(rs.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
                    emp.setB_sal(rs.getDouble("b_salary"));
                }
                return emp;
            }else if(type.equals("temp_admin")){
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                if(rs.next()) {
                    emp.setFamily(rs.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
                    emp.setB_sal(rs.getDouble("b_salary"));
                }
                insQuery.setLength(0);
                insQuery.append("SELECT start_date,end_date FROM temp_admin_salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                long start_d,end_d;
                if(rs.next()) {
                    emp.setStarts_at(rs.getDate("start_date").getTime());
                    emp.setEnds_at(rs.getDate("end_date").getTime());
                }
                return emp;
            }else{
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = "+id+";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if(rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                if(rs.next()) {
                    emp.setFamily(rs.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
                    emp.setB_sal(rs.getDouble("b_salary"));
                }
                insQuery.setLength(0);
                insQuery.append("SELECT start_date,end_date FROM temp_teach_salaries WHERE sal_id = "+sal_id+";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                rs = stmtIns.getResultSet();
                long start_d,end_d;
                if(rs.next()) {
                    emp.setStarts_at(rs.getDate("start_date").getTime());
                    emp.setEnds_at(rs.getDate("end_date").getTime());
                }
                emp.setLibrary(sal.getLibrary_bonus());
                return emp;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return emp;
    }
}
