package servlets;

import com.google.gson.Gson;
import db.EmpDB;
import model.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/promote")
public class PromoteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");

        PrintWriter out = resp.getWriter();
        String emp_id = req.getParameter("emp_id");

        try {
            EmpDB.promoteEmployee(Integer.parseInt(emp_id));
            resp.setStatus(200);
            out.println(new Gson().toJson(new JSONResponse("promotion successfull",200)));
        } catch (ClassNotFoundException e) {
            resp.setStatus(400);
            out.println(new Gson().toJson(new JSONResponse("promotion unsuccessfull",400)));
            e.printStackTrace();
        }
        out.flush();
    }
}
