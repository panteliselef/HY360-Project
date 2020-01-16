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
import java.io.IOException;

@WebServlet("/checkExistance")
public class CheckExistanceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");

        try {
            boolean ans = EmpDB.alreadyExists(fname,lname);
            resp.getWriter().print(new Gson().toJson(new JSONResponse("nice",200,ans)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
