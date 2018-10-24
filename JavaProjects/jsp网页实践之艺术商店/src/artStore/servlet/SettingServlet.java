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

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/20.
 */
@WebServlet(name = "SettingServlet", value = "/SettingServlet")
public class SettingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        获取用户修改信息
        CustomerService customerService = new CustomerServiceImpl();
        String customerId = request.getParameter("customerId");
        String nameSetting = request.getParameter("nameSetting");
        String addressSetting = request.getParameter("addressSetting");
        String citySetting = request.getParameter("citySetting");
        String countrySetting = request.getParameter("countrySetting");
        String postalSetting = request.getParameter("postalSetting");
        String phoneSetting = request.getParameter("phoneSetting");
        String emailSetting = request.getParameter("emailSetting");
//        传送数据并处理修改
        CustomersEntity customersEntity = customerService.update(parseInt(customerId), nameSetting, citySetting, countrySetting, postalSetting, phoneSetting, emailSetting, addressSetting);
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

    }
}
