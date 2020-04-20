package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.domain.User;

public interface SellerService {
    int regist(Seller seller);

    Seller findOne(int sid);

    int update(Seller seller);

    boolean remove(int id);

    PageBean<Seller> pageQuery(int currentPage, int pageSize);
}
