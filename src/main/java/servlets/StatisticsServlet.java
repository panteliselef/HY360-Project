package servlets;


import db.SalaryDB;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/stats")
public class StatisticsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();

        String [] categories = {"perm_admin","perm_teach","temp_admin","temp_teach"};
        JSONArray jsonArray = new JSONArray();
        try {

            for (String cat: categories
            ) {
                JSONObject jo = new JSONObject();
                jo.put("categoryName",cat);
                jo.put("total_salaries",SalaryDB.calculateSumOfSal(cat));
                jsonArray.put(jo);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        out.println(jsonArray);
        out.flush();
    }
}