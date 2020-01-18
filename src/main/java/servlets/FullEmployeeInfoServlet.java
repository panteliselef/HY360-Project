package servlets;


import com.google.gson.Gson;
import db.EmpDB;
import model.Employee;
import model.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/fullinfo")
public class FullEmployeeInfoServlet extends HttpServlet {
    private String getJSON(BufferedReader br) throws IOException {
        StringBuilder stb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            stb.append(line);
        return stb.toString();
    }

    private Gson gson = new Gson();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");


        int id = Integer.parseInt(req.getParameter("id"));
        Employee emp = null;
        try {
            emp = EmpDB.EmployeeFullInfo(id);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder info = new StringBuilder();
        info.append("Fname:"+emp.getFname()+", Lname:"+emp.getLname()+", Started_at:"+emp.getStartedAt()+", Address:"+
                emp.getAddress()+", Phone:"+emp.getPhone()+", IBAN:"+emp.getIBAN()+", Bank Name:"+emp.getBankName()+
                ", Department ID:"+emp.getDepartmentId()+", Married:"+emp.isMarried()+", Position:"+emp.getType()+
                ", Basic Salary:"+emp.getB_sal()+", Family Bonus:"+emp.getFamily()+", After bonus salary:"+emp.getAfter_bonus_sal());
        if(emp.getType().equals("perm_admin")){
            info.append(", Annual Bonus:"+emp.getAnnual());
        }else if(emp.getType().equals("perm_teach")){
            info.append(", Annual Bonus:"+emp.getAnnual()+", Research Bonus:"+emp.getResearch());
        }else if(emp.getType().equals("temp_admin")){
            info.append(", Start date:"+emp.getStarts_at()+", End date:"+emp.getEnds_at());
        }else {
            info.append(", Start date:"+emp.getStarts_at()+", End date:"+emp.getEnds_at()+", Library Bonus:"+emp.getLibrary());
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(new JSONResponse("full info fetched successfully",200,emp)));
        out.flush();

    }

}
