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
@WebServlet(name = "CartDeleteServlet", value = "/CartDeleteServlet")
public class CartDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取作家ID与删除的商品ID
        String customerID = request.getParameter("customerID");
        String orderId = request.getParameter("orderId");
        CartDoService cartDoService=new CartDoServiceImpl();
//        删除购物车中该用户的该订单
        boolean result=cartDoService.deleteByDoubleID(parseInt(customerID),parseInt(orderId));
//        获取用户的新购物车并返回
        List<OrderdetailEntity> orderdetailEntityList = cartDoService.cartGetByCustomerID(parseInt(customerID));
        request.setAttribute("CartArtwork", orderdetailEntityList);
//        重定向到购物车页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Cart.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
