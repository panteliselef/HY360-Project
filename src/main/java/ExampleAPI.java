import db.DeptDB;
import db.EmpDB;
import model.Employee;

public class ExampleAPI {

    private static void createDepartments() throws ClassNotFoundException{
        DeptDB.addDepartment("Department of Mathematics & Applied Mathematics");
        DeptDB.addDepartment("Department of Computer Science");
        DeptDB.addDepartment("Department of Biology");
    }
    public static void main(String[] args) throws ClassNotFoundException {
//        createDepartments();
        DeptDB.getDepartments();
        Employee employee = new Employee("Pantelis","elef","Athinon","69xxxxx","GR0000000","Alpha Bank");
        employee.setDepartmentId(6);

        EmpDB.addEmployee(employee);


    }
}
