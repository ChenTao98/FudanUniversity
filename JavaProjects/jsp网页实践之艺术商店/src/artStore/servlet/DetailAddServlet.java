package artStore.servlet;

import artStore.service.CartDoService;
import artStore.service.impl.CartDoServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/20.
 */
@WebServlet(name = "DetailAddServlet",value = "/DetailAddServlet")
public class DetailAddServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取信息，添加至购物车
        CartDoService cartDoService=new CartDoServiceImpl();
        String customerID=request.getParameter("customerId");
        String artworkId=request.getParameter("artworkId");
        String artworkName=request.getParameter("artworkName");
        Date date=new Date();
        boolean result=cartDoService.addToCart(parseInt(customerID),date,parseInt(artworkId),artworkName);
        if (result) {
            // json 表达式
            response.getWriter().print("{\"result\": \"OK\"}");
        } else {
            // json 表达式
            response.getWriter().print("{\"result\": \"ERROR\", \"error\": \"userName is already existed.\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
