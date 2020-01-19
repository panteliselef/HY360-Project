package servlets;

import com.google.gson.Gson;
import db.PaymentDB;
import db.SalaryDB;
import model.JSONResponse;
import model.Payment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

@WebServlet("/payments")
public class PaymentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "PUT, OPTIONS, POST , DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");


        PrintWriter out = resp.getWriter();

        String category = req.getParameter("category");
        String dateFrom = req.getParameter("from");
        String dateUntil = req.getParameter("until");

        try {
            ArrayList<Payment> p =  PaymentDB.getPaymentInfo(category,new Date(Long.parseLong(dateFrom)),new Date(Long.parseLong((dateUntil))));
            resp.setStatus(200);
            out.print(new Gson().toJson(new JSONResponse("Payment has been completed",200,p)));
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            out.print(new Gson().toJson(new JSONResponse("Payment has NOT been completed",500)));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "PUT, OPTIONS, POST , DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.setContentType("application/json");


        PrintWriter out = resp.getWriter();

        try {
            PaymentDB.addPayment();
            resp.setStatus(200);
            out.print(new Gson().toJson(new JSONResponse("Payment has been completed",200)));
        } catch (ClassNotFoundException e) {
            resp.setStatus(500);
            out.print(new Gson().toJson(new JSONResponse("Payment has NOT been completed",500)));
            e.printStackTrace();
        }




    }
}
