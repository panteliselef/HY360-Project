package servlets;


import com.google.gson.Gson;
import db.SalaryDB;
import model.Salary;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

@WebServlet("/sal_emp_type")
//http://localhost/hy360/sal_emp_type?mode=max&category=perm_admin
public class TypeOfSalaryAndEmpServlet extends HttpServlet {

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


//        String mode = req.getParameter("mode");
//        String category = req.getParameter("category");

        String [] modes = {"max","min","avg"};
        String [] categories = {"perm_admin","perm_teach","temp_admin","temp_teach"};
        double salary = 0;

        JSONArray jsonArray = new JSONArray();
        try {

            for (String cat: categories
                 ) {
                JSONObject jo = new JSONObject();
                jo.put("categoryName",cat);
                for(String mode: modes){
                    salary = SalaryDB.MMMSalaryForTypeOfEmployee(cat,mode);
                    jo.put(mode,salary);

                }
                jsonArray.put(jo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }







        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);
        out.flush();


    }




}
