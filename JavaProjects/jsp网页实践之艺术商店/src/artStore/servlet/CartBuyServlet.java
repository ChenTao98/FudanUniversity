package artStore.servlet;

import artStore.service.CartDoService;
import artStore.service.impl.CartDoServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/21.
 */
@WebServlet(name = "CartBuyServlet", value = "/CartBuyServlet")
public class CartBuyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取用户购买的商品ID封装成数组
        CartDoService cartDoService = new CartDoServiceImpl();
        String[] artworkIdArray = request.getParameterValues("buyArtworkCheckbox");
        String customerId = request.getParameter("customerID");
//        对购买商品操作
        boolean result = cartDoService.deleteByList(artworkIdArray, parseInt(customerId));
        if (result) {
            // json 表达式
            response.getWriter().print("{\"result\": \"OK\"}");
        } else {
            // json 表达式
            response.getWriter().print("{\"result\": \"ERROR\", \"error\": \"userName is already existed.\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
