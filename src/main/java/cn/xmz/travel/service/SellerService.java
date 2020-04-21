package cn.xmz.travel.service;

import cn.xmz.travel.domain.PageBean;
import cn.xmz.travel.domain.Seller;

import java.util.List;

public interface SellerService {
    int regist(Seller seller);

    Seller findOne(int sid);

    int update(Seller seller);

    boolean remove(int id);

    PageBean<Seller> pageQuery(int currentPage, int pageSize);

    List<Seller> findAll();
}
