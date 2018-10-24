package artStore.servlet;

import artStore.entity.OrderrecordEntity;
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
 * Created by Bing Chen on 2017/7/21.
 */
@WebServlet(name = "RecordServlet",value = "/RecordServlet")
public class RecordServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        通过用户ID获取其完成订单
        CartDoService cartDoService=new CartDoServiceImpl();
        String customerID=request.getParameter("customerId");
        List<OrderrecordEntity> orderrecordEntityList=cartDoService.recordGetByCustomerID(parseInt(customerID));
//        设置request属性
        request.setAttribute("RecordArtwork",orderrecordEntityList);
//        重定向到订单页面
        RequestDispatcher dispatcher = request.getRequestDispatcher("./Record.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
