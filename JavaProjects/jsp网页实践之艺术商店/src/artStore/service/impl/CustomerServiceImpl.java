package artStore.service.impl;

import artStore.dao.CustomerDao;
import artStore.dao.impl.CustomerDaoImpl;
import artStore.entity.CustomersEntity;
import artStore.service.CustomerService;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public class CustomerServiceImpl implements CustomerService {
    //    通过用户名与密码判定是否能够登录成功并返回CustomersEntity实例
    @Override
    public CustomersEntity Login(String user, String pass) {
        CustomerDao customerDao = new CustomerDaoImpl();
        return customerDao.Login(user, pass);
    }

    //    完成注册功能，返回CustomersEntity实例
    @Override
    public CustomersEntity register(String user, String pass, String lastName) {
        CustomerDao customerDao = new CustomerDaoImpl();
        return customerDao.register(user, pass, lastName);
    }

    //    修改用户信息
    @Override
    public CustomersEntity update(int CustomerId, String lastName, String city, String country, String Postal, String phone, String email, String address) {
        CustomerDao customerDao = new CustomerDaoImpl();
        return customerDao.update(CustomerId, lastName, city, country, Postal, phone, email, address);
    }

    //    通过用户ID获取用户
    @Override
    public CustomersEntity getById(int CustomerID) {
        return null;
    }
}
