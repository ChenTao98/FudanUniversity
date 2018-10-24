package artStore.service;

import artStore.entity.CustomersEntity;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public interface CustomerService {
    //    通过用户名与密码判定是否能够登录成功并返回CustomersEntity实例
    CustomersEntity Login(String user, String pass);

    //    完成注册功能，返回CustomersEntity实例
    CustomersEntity register(String user, String pass, String lastName);

    //    修改用户信息
    CustomersEntity update(int CustomerId, String lastName, String city, String country, String Postal, String phone, String email, String address);

    //    通过用户ID获取用户
    CustomersEntity getById(int CustomerID);
}
