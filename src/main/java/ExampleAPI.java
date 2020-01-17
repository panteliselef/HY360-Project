import db.ChildDB;
import db.DeptDB;
import db.EmpDB;
import db.SalaryDB;
import model.Child;
import model.Employee;
import model.Salary;

public class ExampleAPI {

    private static void createDepartments() throws ClassNotFoundException{
        DeptDB.addDepartment("Department of Mathematics & Applied Mathematics");
        DeptDB.addDepartment("Department of Computer Science");
        DeptDB.addDepartment("Department of Biology");
    }
    public static void main(String[] args) throws ClassNotFoundException {
//        createDepartments();
        DeptDB.getDepartments();
//        Employee employee = new Employee("Pantelis","elef","Athinon","69xxxxx","GR0000000","Alpha Bank");
//        employee.setDepartmentId(6);

//        Employee employee = new Employee("Jimis","aggelis","Athinon","69xxxxx","GR0000000","Alpha Bank");
//        employee.setDepartmentId(5);
//        EmpDB.addEmployee(employee);
//        Child c = new Child(19,employee.getId());
//        Child c2 = new Child(25,employee.getId());
//        employee.addChild(c);
//        employee.addChild(c2);
//
//        employee.getChildren().forEach(child -> {
//            try {
//                ChildDB.addChild(child);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        });


        ChildDB.getChildren(5).forEach(child -> {
            System.out.println("Child at age "+ child.getAge());
        });

//        Salary sal = new Salary(800,950,15,5,10,10);
//        Salary sal = SalaryDB.getBasicSalary();
//        sal.setAnnual_bonus(100);
//        SalaryDB.updateSalary(sal);

//        Employee e = EmpDB.getEmployee(13);
//        e.getChildren().get(0).setAge(69);
//        e.setIsMarried("yes");
//
//        EmpDB.updateEmployee(e);
        double r = SalaryDB.MMMSalaryForTypeOfEmployee("perm_admin","max");
        System.out.println(r);

    }
}
