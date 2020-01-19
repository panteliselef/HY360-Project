package db;

import model.Child;
import model.Employee;
import model.Salary;

import javax.net.ssl.SSLException;
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
                System.out.println(res.getString("fname") + "  " + res.getString("lname"));
                return true;
            } else return false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return true;
    }

    protected static void updateSalaryAndBonus() throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();

            Salary basicSals = SalaryDB.getBasicSalary();


//            "UPDATE  employees INNER JOIN emp_salaries ON employees.emp_id = emp_salaries.emp_id INNER JOIN perm_admin_salaries ON emp_salaries.sal_id = perm_admin_salaries.sal_id SET phone = '82' WHERE employees.emp_id = 44;"
            String joinedTables = " employees INNER JOIN emp_salaries ON employees.emp_id = emp_salaries.emp_id INNER JOIN salaries ON emp_salaries.sal_id = salaries.sal_id ";
            insQuery.append("UPDATE  " + joinedTables + " INNER JOIN perm_admin_salaries ON emp_salaries.sal_id = perm_admin_salaries.sal_id ")
                    .append("SET ")
                    .append("annual_bonus = " + basicSals.getAnnual_bonus())
                    .append(" WHERE annual_bonus < " + basicSals.getAnnual_bonus() + " ;");
            System.out.println(insQuery);
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();


            insQuery.setLength(0);
            insQuery.append("UPDATE  " + joinedTables + " INNER JOIN perm_teach_salaries ON emp_salaries.sal_id = perm_teach_salaries.sal_id ")
                    .append(" SET ")
                    .append(" annual_bonus = " + basicSals.getAnnual_bonus() + ", " +
                            "research_bonus = " + basicSals.getResearch_bonus())
                    .append(" WHERE annual_bonus < " + basicSals.getAnnual_bonus() + " or ")
                    .append(" research_bonus < " + basicSals.getResearch_bonus() + " ;");


            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            insQuery.setLength(0);

            insQuery.append("UPDATE  " + joinedTables + " INNER JOIN temp_teach_salaries ON emp_salaries.sal_id = temp_teach_salaries.sal_id ")
                    .append(" SET ")
                    .append(" library_bonus = " + basicSals.getLibrary_bonus())
                    .append(" WHERE library_bonus < " + basicSals.getLibrary_bonus() + ";");

            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();


            insQuery.setLength(0);
            insQuery.append("UPDATE  " + joinedTables)
                    .append("SET ")
                    .append("b_salary = " + basicSals.getPerm_admin_salary() + ", " +
                            "family_bonus = " + basicSals.getFamily_bonus())
                    .append(" WHERE ")
                    .append(" b_salary < " + basicSals.getPerm_admin_salary() + " or ")
                    .append(" family_bonus < " + basicSals.getFamily_bonus() + ";");


            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static void addEmployee(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;

        if (alreadyExists(emp.getFname(), emp.getLname())) throw new ClassNotFoundException();

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

    public static void updateEmployee(Employee emp) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE employees ")
                    .append("SET ")
                    .append("fname = '" + emp.getFname() + "', " +
                            "lname = '" + emp.getLname() + "', " +
                            "started_at = '" + emp.getStartedAt() + "', " +
                            "address = '" + emp.getAddress() + "', " +
                            "phone = '" + emp.getPhone() + "', " +
                            "IBAN = '" + emp.getIBAN() + "', " +
                            "bank_name = '" + emp.getBankName() + "', " +
                            "department_id = " + emp.getDepartmentId() + ", " +
                            "is_married = '" + emp.isMarried())
                    .append("' WHERE emp_id = " + emp.getId() + ";");


            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();

            ArrayList<Child> children = (ArrayList<Child>) emp.getChildren();
            System.out.println(children);

            for (Child child : emp.getChildren()) {
                insQuery.setLength(0);
                if (child.getId() == 0) {
                    child.setEmp_id(emp.getId());
                    ChildDB.addChild(child);
                } else {
                    insQuery.append("UPDATE emp_children ")
                            .append("SET ")
                            .append("age = " + child.getAge())
                            .append(" WHERE child_id = " + child.getId() + " AND emp_id = " + emp.getId() + ";");

                }
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
    }

    public static Employee getEmployee(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        Employee emp;

        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();
            String fname = "", lname = "", address = "", phone = "", IBAN = "", bank_name = "", ismarried = "";
            Date started_at = null;
            Date left_at = null;
            int dep_id = -1;
            insQuery.append("SELECT * from employees WHERE emp_id = " + id + ";");


            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            if (res.next()) {
                fname = res.getString("fname");
                lname = res.getString("lname");
                started_at = res.getDate("started_at");
                address = res.getString("address");
                phone = res.getString("phone");
                IBAN = res.getString("IBAN");
                bank_name = res.getString("bank_name");
                dep_id = res.getInt("department_id");
                ismarried = res.getString("is_married");
                left_at = res.getDate("left_at");
            }
            emp = new Employee(fname, lname, address, phone, IBAN, bank_name);
            emp.setStartedAt(started_at);
            emp.setDepartmentId(dep_id);
            emp.setLeft_at(left_at);
            emp.setIsMarried(ismarried);
            insQuery.setLength(0);
            emp.setChildren((ArrayList<Child>) ChildDB.getChildren(id));
            System.out.println(ChildDB.getChildren(id));
            ArrayList<String> un = new ArrayList<>();
            ChildDB.getChildren(id).forEach(child -> {
                un.add(child.getAge() + "");
            });
            emp.setUn_children(un);
            emp.setId(id);
            return emp;


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return null;
    }


    public static ArrayList<Employee> getPermEmployees() throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT employees.emp_id,fname,lname FROM  employees INNER JOIN emp_salaries ON employees.emp_id= emp_salaries.emp_id INNER JOIN (SELECT sal_id from perm_teach_salaries UNION SELECT sal_id from perm_admin_salaries) as b ON emp_salaries.sal_id = b.sal_id WHERE left_at IS NULL;");
            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            while (res.next()) {
                Employee emp = new Employee();
                emp.setFname(res.getString("fname"));
                emp.setLname(res.getString("lname"));
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

    public static ArrayList<Employee> getTempEmloyees() throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();


            insQuery.append("SELECT employees.emp_id,fname,lname, emp_salaries.sal_id FROM  employees INNER JOIN emp_salaries ON employees.emp_id= emp_salaries.emp_id INNER JOIN (SELECT sal_id,promotion_date from temp_teach_salaries UNION SELECT sal_id ,promotion_date from temp_admin_salaries) as b ON emp_salaries.sal_id = b.sal_id WHERE promotion_date IS NULL;");
            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            while (res.next()) {
                Employee emp = new Employee();
                emp.setFname(res.getString("fname"));
                emp.setLname(res.getString("lname"));
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

    public static ArrayList<Employee> getEmployees() throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        ArrayList<Employee> emps = new ArrayList<>();
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * from employees;");
            stmt.executeQuery(insQuery.toString());

            ResultSet res = stmt.getResultSet();
            while (res.next()) {
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


    public static String findEmployeeType(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String type = "";
        int sal_id = -1;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            ResultSet rs = stmtIns.getResultSet();
            if (rs.next()) {
                sal_id = rs.getInt("sal_id");
            }
            insQuery.setLength(0);
            insQuery.append("SELECT * from perm_admin_salaries WHERE sal_id = " + sal_id + ";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if (rs.next()) {
                type = "perm_admin";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from perm_teach_salaries WHERE sal_id = " + sal_id + ";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if (rs.next()) {
                type = "perm_teach";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from temp_admin_salaries WHERE sal_id = " + sal_id + ";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if (rs.next()) {
                type = "temp_admin";
            }

            insQuery.setLength(0);
            insQuery.append("SELECT * from temp_teach_salaries WHERE sal_id = " + sal_id + ";");
            stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeQuery();
            rs = stmtIns.getResultSet();
            if (rs.next()) {
                type = "temp_teach";
            }

            return type;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return type;
    }

    public static void promoteEmployee(int id) throws ClassNotFoundException {
        String type = findEmployeeType(id);
        StringBuilder insQuery = new StringBuilder();
        Salary sal = SalaryDB.getBasicSalary();
        Employee emp = EmpDB.EmployeeFullInfo(id);
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            int sal_id = -1;
            Date d = new Date(System.currentTimeMillis());

            if (type.equals("temp_admin")) {
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if (rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("UPDATE temp_admin_salaries SET promotion_date = '" + d + "' WHERE sal_id = " + sal_id + ";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("UPDATE salaries SET b_salary = " + sal.getPerm_admin_salary() + " WHERE sal_id = " + sal_id + ";");
                con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("INSERT INTO perm_admin_salaries(sal_id,annual_bonus) VALUES(" +
                        sal_id + "," + emp.getAnnual() + ");");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            } else if (type.equals("temp_teach")) {
                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeQuery();
                ResultSet rs = stmtIns.getResultSet();
                if (rs.next()) {
                    sal_id = rs.getInt("sal_id");
                }
                insQuery.setLength(0);
                insQuery.append("UPDATE temp_teach_salaries SET promotion_date = '" + d + "' WHERE sal_id = " + sal_id + ";");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("UPDATE salaries SET b_salary = " + sal.getPerm_teach_salary() + " WHERE sal_id = " + sal_id + ";");
                con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
                insQuery.setLength(0);
                insQuery.append("INSERT INTO perm_teach_salaries(sal_id,annual_bonus,research_bonus) VALUES(" +
                        sal_id + "," + emp.getAnnual() + "," + sal.getResearch_bonus() + ");");
                stmtIns = con.prepareStatement(insQuery.toString());
                stmtIns.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
            insQuery.append("SELECT department_id FROM employees WHERE emp_id = "+id+";");
            int dep_id = 0;
            PreparedStatement statement = con.prepareStatement(insQuery.toString());
            statement.executeQuery();
            ResultSet result = statement.getResultSet();
            if(result.next()){
                dep_id = result.getInt("department_id");
            }
            String department = "";
            insQuery.setLength(0);
            insQuery.append("SELECT name FROM departments WHERE dep_id = "+dep_id+";");
            statement = con.prepareStatement(insQuery.toString());
            statement.executeQuery();
            result = statement.getResultSet();
            if(result.next()){
                department = result.getString("name");
            }
            emp.setDepartment(department);
            insQuery.setLength(0);
            if (type.equals("perm_admin")) {

//                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
//                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                ResultSet rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    sal_id = rs.getInt("sal_id");
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT annual_bonus FROM perm_admin_salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setAnnual(rs.getDouble("annual_bonus"));
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setFamily(rs.getDouble("family_bonus"));
//                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
//                    emp.setB_sal(rs.getDouble("b_salary"));
//                }
                insQuery.append("SELECT employees.*,salaries.*,perm_admin_salaries.annual_bonus FROM employees,emp_salaries,salaries,perm_admin_salaries WHERE employees.emp_id = emp_salaries.emp_id AND emp_salaries.sal_id = salaries.sal_id AND emp_salaries.sal_id = perm_admin_salaries.sal_id");
                statement = con.prepareStatement(insQuery.toString());
                statement.executeQuery();
                result = statement.getResultSet();
                if(result.next()){
                    emp.setFname(result.getString("fname"));
                    emp.setLname(result.getString("lname"));
                    emp.setStartedAt(result.getDate("started_at"));
                    emp.setLeft_at(result.getDate("left_at"));
                    emp.setAddress(result.getString("address"));
                    emp.setPhone(result.getString("phone"));
                    emp.setIban(result.getString("IBAN"));
                    emp.setBankName(result.getString("bank_name"));
                    emp.setDepartmentId(result.getInt("department_id"));
                    emp.setIsMarried(result.getString("is_married"));
                    emp.setB_sal(result.getDouble("b_salary"));
                    emp.setFamily(result.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(result.getDouble("after_bonus_sal"));
                    emp.setAnnual(result.getDouble("annual_bonus"));
                }
                return emp;
            } else if (type.equals("perm_teach")) {

//                emp.setResearch(sal.getResearch_bonus());
//                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
//                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                ResultSet rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    sal_id = rs.getInt("sal_id");
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT annual_bonus FROM perm_admin_salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setAnnual(rs.getDouble("annual_bonus"));
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setFamily(rs.getDouble("family_bonus"));
//                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
//                    emp.setB_sal(rs.getDouble("b_salary"));
//                }
//                return emp;
                insQuery.append("SELECT employees.*,salaries.*,perm_teach_salaries.annual_bonus,perm_teach_salaries.research_bonus FROM employees,emp_salaries,salaries,perm_teach_salaries WHERE employees.emp_id = emp_salaries.emp_id AND emp_salaries.sal_id = salaries.sal_id AND emp_salaries.sal_id = perm_teach_salaries.sal_id");
                statement = con.prepareStatement(insQuery.toString());
                statement.executeQuery();
                result = statement.getResultSet();
                if(result.next()){
                    emp.setFname(result.getString("fname"));
                    emp.setLname(result.getString("lname"));
                    emp.setStartedAt(result.getDate("started_at"));
                    emp.setLeft_at(result.getDate("left_at"));
                    emp.setAddress(result.getString("address"));
                    emp.setPhone(result.getString("phone"));
                    emp.setIban(result.getString("IBAN"));
                    emp.setBankName(result.getString("bank_name"));
                    emp.setDepartmentId(result.getInt("department_id"));
                    emp.setIsMarried(result.getString("is_married"));
                    emp.setB_sal(result.getDouble("b_salary"));
                    emp.setFamily(result.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(result.getDouble("after_bonus_sal"));
                    emp.setAnnual(result.getDouble("annual_bonus"));
                    emp.setResearch(result.getDouble("research_bonus"));
                }
                return emp;
            } else if (type.equals("temp_admin")) {
//                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
//                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                ResultSet rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    sal_id = rs.getInt("sal_id");
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setFamily(rs.getDouble("family_bonus"));
//                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
//                    emp.setB_sal(rs.getDouble("b_salary"));
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT promotion_date,start_date,end_date FROM temp_admin_salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setStarts_at(rs.getDate("start_date").getTime());
//                    emp.setEnds_at(rs.getDate("end_date").getTime());
//                    emp.setPromo_date(rs.getDate("promotion_date"));
//                }
//                return emp;
                insQuery.append("SELECT employees.*,salaries.*,temp_admin_salaries.start_date,temp_admin_salaries.end_date,temp_admin_salaries.promotion_date FROM employees,emp_salaries,salaries,temp_admin_salaries WHERE employees.emp_id = emp_salaries.emp_id AND emp_salaries.sal_id = salaries.sal_id AND emp_salaries.sal_id = temp_admin_salaries.sal_id");
                statement = con.prepareStatement(insQuery.toString());
                statement.executeQuery();
                result = statement.getResultSet();
                if(result.next()){
                    emp.setFname(result.getString("fname"));
                    emp.setLname(result.getString("lname"));
                    emp.setStartedAt(result.getDate("started_at"));
                    emp.setLeft_at(result.getDate("left_at"));
                    emp.setAddress(result.getString("address"));
                    emp.setPhone(result.getString("phone"));
                    emp.setIban(result.getString("IBAN"));
                    emp.setBankName(result.getString("bank_name"));
                    emp.setDepartmentId(result.getInt("department_id"));
                    emp.setIsMarried(result.getString("is_married"));
                    emp.setB_sal(result.getDouble("b_salary"));
                    emp.setFamily(result.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(result.getDouble("after_bonus_sal"));
                    emp.setStarts_at(result.getLong("start_date"));
                    emp.setEnds_at(result.getLong("end_date"));
                    emp.setPromo_date(result.getDate("promotion_date"));
                }
                return emp;
            } else {
//                insQuery.append("SELECT sal_id FROM emp_salaries WHERE emp_id = " + id + ";");
//                PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                ResultSet rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    sal_id = rs.getInt("sal_id");
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT b_salary,after_bonus_sal,family_bonus FROM salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                if (rs.next()) {
//                    emp.setFamily(rs.getDouble("family_bonus"));
//                    emp.setAfter_bonus_sal(rs.getDouble("after_bonus_sal"));
//                    emp.setB_sal(rs.getDouble("b_salary"));
//                }
//                insQuery.setLength(0);
//                insQuery.append("SELECT promotion_date,start_date,end_date FROM temp_teach_salaries WHERE sal_id = " + sal_id + ";");
//                stmtIns = con.prepareStatement(insQuery.toString());
//                stmtIns.executeQuery();
//                rs = stmtIns.getResultSet();
//                long start_d, end_d;
//                if (rs.next()) {
//                    emp.setStarts_at(rs.getDate("start_date").getTime());
//                    emp.setEnds_at(rs.getDate("end_date").getTime());
//                    emp.setPromo_date(rs.getDate("promotion_date"));
//                }
//                emp.setLibrary(sal.getLibrary_bonus());
//                return emp;
                insQuery.append("SELECT employees.*,salaries.*,temp_teach_salaries.start_date,temp_teach_salaries.end_date,temp_teach_salaries.promotion_date,temp_teach_salaries.library_bonus FROM employees,emp_salaries,salaries,temp_teach_salaries WHERE employees.emp_id = emp_salaries.emp_id AND emp_salaries.sal_id = salaries.sal_id AND emp_salaries.sal_id = term_teach_salaries.sal_id");
                statement = con.prepareStatement(insQuery.toString());
                statement.executeQuery();
                result = statement.getResultSet();
                if(result.next()){
                    emp.setFname(result.getString("fname"));
                    emp.setLname(result.getString("lname"));
                    emp.setStartedAt(result.getDate("started_at"));
                    emp.setLeft_at(result.getDate("left_at"));
                    emp.setAddress(result.getString("address"));
                    emp.setPhone(result.getString("phone"));
                    emp.setIban(result.getString("IBAN"));
                    emp.setBankName(result.getString("bank_name"));
                    emp.setDepartmentId(result.getInt("department_id"));
                    emp.setIsMarried(result.getString("is_married"));
                    emp.setB_sal(result.getDouble("b_salary"));
                    emp.setFamily(result.getDouble("family_bonus"));
                    emp.setAfter_bonus_sal(result.getDouble("after_bonus_sal"));
                    emp.setStarts_at(result.getLong("start_date"));
                    emp.setEnds_at(result.getLong("end_date"));
                    emp.setPromo_date(result.getDate("promotion_date"));
                    emp.setLibrary(result.getDouble("library_bonus"));
                }
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }
        return emp;
    }

    public static void Fire_Retire_Employee(int id) throws ClassNotFoundException {
        Date d = new Date(System.currentTimeMillis());
        Statement stmt = null;
        Connection con = null;
        try {
            con = CS360DB.getConnection();
            stmt = con.createStatement();
            StringBuilder insQuery = new StringBuilder();
            insQuery.append("UPDATE employees SET left_at = '" + d + "' WHERE emp_id = " + id + ";");
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString());
            stmtIns.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CS360DB.closeDBConnection(stmt, con);
        }

    }
}
