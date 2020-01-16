package servlets;


import com.google.gson.Gson;
import db.ChildDB;
import db.EmpDB;
import db.SalaryDB;
import model.Child;
import model.Employee;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

@WebServlet("/hiring")
@MultipartConfig
public class HiringServlet extends HttpServlet {

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

        resp.getWriter().print("NICE");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");


        StringBuffer jb = new StringBuffer();

        PrintWriter out = resp.getWriter();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

//        System.out.println(jb.toString());

        Employee emp = gson.fromJson(jb.toString(), Employee.class);

        System.out.println(emp);

        JSONObject jsonObject = new JSONObject(jb.toString());

        System.out.println("it is"+jsonObject.get("salaryType"));

        String salaryType = jsonObject.getString("salaryType");

//        if(salaryType.equals("perm")){
//
//
//
//        }else if(salaryType.equals("term")){
//
//        }

        try {
            Date d = new Date(System.currentTimeMillis());
            emp.setStartedAt(d);
            emp.setChildren(new ArrayList<>());
            EmpDB.addEmployee(emp);
            SalaryDB.addSalary(emp);
            System.out.println(emp.getUn_children().size());

            emp.getUn_children().forEach(age -> {
                Child ch = new Child(Integer.parseInt(age),emp.getId());
                emp.addChild(ch);
            });
            emp.getChildren().forEach(child -> {
                try {
                    ChildDB.addChild(child);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            resp.setStatus(201);
            out.println(gson.toJson(emp));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println("JSON"+gson.toJson(emp));
//
//            JsonObject jsonObject =  new JsonObject().getAsJsonObject(getJSON(req.getReader()));
//            resp.getWriter().print(getJSON(req.getReader()));
//
//            gson.fromJson(getJSON(req.getReader()), Employee.class);
//            System.out.println(getJSON(req.getReader()));


    }
}
