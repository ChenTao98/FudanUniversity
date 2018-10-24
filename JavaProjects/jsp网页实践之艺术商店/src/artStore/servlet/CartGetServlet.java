package artStore.servlet;

import artStore.entity.OrderdetailEntity;
import artStore.service.CartDoService;
import artStore.service.impl.CartDoServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/20.
 */
@WebServlet(name = "CartGetServlet", value = "/CartGetServlet")
public class CartGetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取用户ID并通过ID获取购物车信息
        CartDoService cartDoService = new CartDoServiceImpl();
        String customerID = request.getParameter("customerId");
        List<OrderdetailEntity> orderdetailEntityList = cartDoService.cartGetByCustomerID(parseInt(customerID));
        request.setAttribute("CartArtwork", orderdetailEntityList);
//        重定向到购物车页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Cart.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
