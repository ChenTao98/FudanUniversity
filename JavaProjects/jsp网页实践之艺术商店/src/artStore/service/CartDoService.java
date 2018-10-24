package artStore.service;

import artStore.entity.OrderdetailEntity;
import artStore.entity.OrderrecordEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Bing Chen on 2017/7/20.
 */
public interface CartDoService {
    //通过用户ID获取该用户的所有购物车订单
    List<OrderdetailEntity> cartGetByCustomerID(int customerID);

    //通过用户ID与订单ID完成购物车的删除工作
    boolean deleteByDoubleID(int customerID, int orderID);

    //用户购买时通过商品ID删除所有该商品的订单，并删除商品，以做到商品只能被购买一次
    boolean deleteByArtworkID(int artworkID);

    //购买时操作，获取通过含有商品ID 的数组与用户ID完成购买操作
    boolean deleteByList(String[] artworkIDArray, int customerID);

    // 通过获取的数据，添加购物车订单
    boolean addToCart(int customerID, Date date, int artworkID, String artworkName);

    //购买时通过传入的参数往完成订单的表中插入数据
    boolean addToRecord(int customerID, Date dateCreate, int artworkID, String artworkName, Date dataComplete);

    //通过用户ID，获取该用户所有已完成订单
    List<OrderrecordEntity> recordGetByCustomerID(int CustomerID);
}
