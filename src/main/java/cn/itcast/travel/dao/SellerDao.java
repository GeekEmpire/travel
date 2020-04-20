package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

import java.util.List;

public interface SellerDao {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Seller findById(int id);

    int update(Seller seller);

    boolean remove(int id);

    int findTotalCount();

    List<Seller> findByPage(int start, int pageSize);

    void save(Seller seller);

    Seller findBySname(String sname);
}
