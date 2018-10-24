package artStore.servlet;

import artStore.entity.CustomersEntity;
import artStore.service.CustomerService;
import artStore.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Bing Chen on 2017/7/19.
 */
@WebServlet(name = "RegisterServlet",value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取用户的密码、用户名、邮箱
        CustomerService customerService=new CustomerServiceImpl();
        String username = request.getParameter("username");
        String password = request.getParameter("hiddenPassword");
        String lastName=request.getParameter("lastName");
//        判断并选择是否可以注册
        CustomersEntity customersEntity=customerService.register(username,password,lastName);
        if (customersEntity != null) {
            request.getSession().setAttribute("user", customersEntity);
            // json 表达式
            response.getWriter().print("{\"result\": \"OK\"}");
        }
        else {
            // json 表达式
            response.getWriter().print("{\"result\": \"ERROR\", \"error\": \"userName is already existed.\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
