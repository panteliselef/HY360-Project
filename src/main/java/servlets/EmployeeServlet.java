package servlets;

import com.google.gson.Gson;
import db.EmpDB;
import model.Employee;
import model.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/employee")
@MultipartConfig
public class EmployeeServlet  extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        String id = req.getParameter("id");
        if(id!=null){
            resp.setStatus(400);
            out.print(gson.toJson(new JSONResponse("invalid mode", 400,null)));
        }else{
            resp.setStatus(200);
            try {
                out.print(gson.toJson(new JSONResponse("employees recovered", 200,EmpDB.getEmployees())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "PUT, OPTIONS, POST , DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        super.doOptions(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "PUT, OPTIONS, POST , DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();
        StringBuffer jb = new StringBuffer();
        String line;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }
        Employee empFromClient = gson.fromJson(jb.toString(), Employee.class);
        try {
            Employee empFromDatabase = EmpDB.getEmployee(empFromClient.getId());
            empFromDatabase.setFname(empFromClient.getFname());
            empFromDatabase.setLname(empFromClient.getLname());
            empFromDatabase.setAddress(empFromClient.getAddress());
            empFromDatabase.setPhone(empFromClient.getPhone());
            empFromDatabase.setIban(empFromClient.getIBAN());
            empFromDatabase.setBankName(empFromClient.getBankName());
            empFromDatabase.setDepartmentId(empFromClient.getDepartmentId());
            empFromDatabase.setIsMarried(empFromClient.isMarried());

            EmpDB.updateEmployee(empFromDatabase);

            resp.setStatus(200);
            out.print(gson.toJson(new JSONResponse("Employee  has been updated" + empFromDatabase.getId(), 200,null)));

        } catch (ClassNotFoundException e) {
            resp.setStatus(400);
            out.print(gson.toJson(new JSONResponse("Error", 400,null)));
            e.printStackTrace();
        }

    }
}
